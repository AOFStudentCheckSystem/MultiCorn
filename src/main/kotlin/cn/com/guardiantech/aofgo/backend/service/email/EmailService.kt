package cn.com.guardiantech.aofgo.backend.service.email

import cn.com.guardiantech.aofgo.backend.data.entity.email.*
import cn.com.guardiantech.aofgo.backend.repository.email.EmailTemplateRepository
import cn.com.guardiantech.aofgo.backend.repository.email.EmailTemplateTypeRepository
import cn.com.guardiantech.aofgo.backend.repository.email.EmailTemplateVariableRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.DefaultResourceLoader
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.regex.Pattern
import javax.annotation.PostConstruct
import javax.mail.Address
import javax.mail.Message
import javax.mail.internet.InternetAddress


/**
 * Created by Codetector on 2017/4/12.
 * Project backend
 */

@Service
class EmailService {

    private val logger: Logger = LoggerFactory.getLogger(EmailService::class.java)

    @Value("\${spring.mail.from:}")
    lateinit var from: String
    @Autowired
    lateinit var mailSender: JavaMailSender
    @Autowired
    lateinit var emailTemplateRepository: EmailTemplateRepository
    @Autowired
    lateinit var emailTemplateTypeRepository: EmailTemplateTypeRepository
    @Autowired
    lateinit var emailTemplateVariableRepository: EmailTemplateVariableRepository

    var objectMapper = ObjectMapper()

    var defaultTemplate: MutableMap<EmailTemplateTypeEnum, EmailTemplate> = mutableMapOf()

    fun sendEmail(title: String, body: String, vararg recipients: String) {
        mailSender.send(mailSender.createMimeMessage().let { msg ->
            msg.setFrom(from)
            msg.subject = title
            msg.setText(body, "utf-8", "html")
            recipients.mapTo(HashSet<Address>(), ::InternetAddress).forEach {
                msg.addRecipient(Message.RecipientType.TO, it)
            }
            msg
        })
    }

    @Transactional
    fun submitTemplate(name: String, type: EmailTemplateTypeEnum, title: String, body: String): EmailTemplate {
        val templateType = emailTemplateTypeRepository.findByTemplateType(type).get()

        val setFound = Pattern.compile("\\{\\{ *([0-9A-Za-z]+) *}}").let {
            val set = mutableSetOf<String>()
            it.matcher(title).let {
                while (it.find()) {
                    set.add(it.group(1))
                }
            }
            it.matcher(body).let {
                while (it.find()) {
                    set.add(it.group(1))
                }
            }
            set
        }
        if (setFound == templateType.variables.map { it.name }.toSet()) {
            return emailTemplateRepository.save(EmailTemplate(
                    templateType = templateType,
                    title = title,
                    body = body,
                    name = name
            ))
        }

        throw IllegalArgumentException("Invalid")
    }

    @Transactional
    fun appointDefaultTemplate(name: String?, type: EmailTemplateTypeEnum) {
        if (name != null) {
            val foundTemplate = emailTemplateRepository.findByNameAndTemplateType(name, type).get()
            foundTemplate.templateType.let {
                it.defaultTemplate = foundTemplate
                emailTemplateTypeRepository.save(it)
            }
            setDefaultTemplate(foundTemplate)
        } else {
            emailTemplateTypeRepository.findByTemplateType(type).get().let {
                it.defaultTemplate = null
                emailTemplateTypeRepository.save(it)
            }
            loadDefaultTemplate(type)
        }

    }

    /**
     *  A TemplateType will be regenerate when variables whenever its saved variables is different from initialization code!
     */
    @PostConstruct
    fun initialize() {
        val checkinDefault = emailTemplateTypeRepository.findByTemplateType(EmailTemplateTypeEnum.CHECKIN)
        val templateVariableSet = setOf(
                EmailTemplateVariable(
                        name = "eventName",
                        type = EmailTemplateVariableType.STRING
                ),
                EmailTemplateVariable(
                        name = "count",
                        type = EmailTemplateVariableType.STRING
                ),
                EmailTemplateVariable(
                        name = "studentList",
                        type = EmailTemplateVariableType.LIST
                ),
                EmailTemplateVariable(
                        name = "eventId",
                        type = EmailTemplateVariableType.STRING
                )
        )
        var variablesValid = false
        var gotCheckin = if (checkinDefault.isPresent) {
            checkinDefault.get().let {
                variablesValid = it.variables.map {
                    Pair(it.name, it.type)
                }.toSet() == templateVariableSet.map {
                    Pair(it.name, it.type)
                }.toSet()
            }
            checkinDefault.get()
        } else {
            emailTemplateTypeRepository.save(EmailTemplateType(
                    templateType = EmailTemplateTypeEnum.CHECKIN
            ))
        }

        logger.debug("CHECKIN variables valid: $variablesValid")
        if (!variablesValid) {
            gotCheckin = emailTemplateTypeRepository.save(
                    gotCheckin.let {
                        it.variables.clear()
                        it
                    }
            )
            templateVariableSet.let {
                it.forEach {
                    gotCheckin.addVariable(it)
                }
                emailTemplateVariableRepository.save(it)
            }
        }

        if (gotCheckin.defaultTemplate !== null) {
            setDefaultTemplate(gotCheckin.defaultTemplate!!)
        } else {
            loadDefaultTemplate(EmailTemplateTypeEnum.CHECKIN)
        }

        logger.debug("Active templates:" + objectMapper.writeValueAsString(defaultTemplate))
    }

    private fun setDefaultTemplate(template: EmailTemplate) {
        defaultTemplate[template.templateType.templateType] = template
    }

    private fun loadDefaultTemplate(type: EmailTemplateTypeEnum) {
        defaultTemplate[type] = EmailTemplate(
                name = "",
                title = resrcToString("/template/${type.toString().toLowerCase()}/default_title"),
                body = resrcToString("/template/${type.toString().toLowerCase()}/default_body"),
                templateType = emailTemplateTypeRepository.findByTemplateType(type).get()
        )
    }

    private fun resrcToString(path: String) =
            DefaultResourceLoader()
                    .getResource(path)
                    .inputStream.bufferedReader().use { it.readText() }
}