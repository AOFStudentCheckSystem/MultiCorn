package cn.com.guardiantech.aofgo.backend.service.email

import cn.com.guardiantech.aofgo.backend.data.entity.email.*
import cn.com.guardiantech.aofgo.backend.repository.email.EmailTemplateRepository
import cn.com.guardiantech.aofgo.backend.repository.email.EmailTemplateTypeRepository
import cn.com.guardiantech.aofgo.backend.repository.email.EmailTemplateVariableRepository
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

    var defaultTemplate: MutableMap<EmailTemplateTypeEnum, Pair<String, String>> = mutableMapOf()

//    fun sendMail(templateName: String, values: HashMap<String, String>, vararg recipients: String) {
//        val template = MailTemplateFactory.createTemplateByFileName(if (templateName.endsWith(".template", true)) templateName else templateName + ".template")
//        values.forEach {
//            template.setStringValue(it.key, it.value)
//        }
//        sendMail(template, *recipients)
//    }

    fun sendMailWithTitle(template: MailTemplate, title: String, vararg recipients: String) {
        val msg = mailSender.createMimeMessage()
        msg.setFrom(from)
        msg.subject = title
        msg.setText(template.encode(), "utf-8", "html")
        recipients.mapTo(HashSet<Address>(), ::InternetAddress).forEach {
            msg.addRecipient(Message.RecipientType.TO, it)
        }
        mailSender.send(msg)
    }

    fun sendEmail(type: EmailTemplateTypeEnum, values: Map<String, Any>) {

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

    @PostConstruct
    fun initialize() {
        val checkinDefault = emailTemplateTypeRepository.findByTemplateType(EmailTemplateTypeEnum.CHECKIN)
        val gotCheckin = if (!checkinDefault.isPresent) {
            emailTemplateTypeRepository.save(EmailTemplateType(
                    templateType = EmailTemplateTypeEnum.CHECKIN,
                    variables = emailTemplateVariableRepository.save(
                            setOf(
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
                    ).toMutableSet()
            ))
        } else {
            checkinDefault.get()
        }
        defaultTemplate[EmailTemplateTypeEnum.CHECKIN] = if (gotCheckin.defaultTemplate !== null){
            Pair(gotCheckin.defaultTemplate.title, gotCheckin.defaultTemplate.body)
        } else {
            Pair(resrcToString("/template/checkin/default_title"), resrcToString("/template/checkin/default_body"))
        }

        logger.debug("Active templates:" + defaultTemplate.toString())
    }

    private fun resrcToString(path: String) = DefaultResourceLoader()
            .getResource(path)
            .inputStream.bufferedReader().use { it.readText() }
}