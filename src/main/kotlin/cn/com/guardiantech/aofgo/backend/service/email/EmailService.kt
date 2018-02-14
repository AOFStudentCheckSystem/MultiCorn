package cn.com.guardiantech.aofgo.backend.service.email

import cn.com.guardiantech.aofgo.backend.data.entity.email.EmailTemplate
import cn.com.guardiantech.aofgo.backend.data.entity.email.EmailTemplateTypeEnum
import cn.com.guardiantech.aofgo.backend.repository.email.EmailTemplateRepository
import cn.com.guardiantech.aofgo.backend.repository.email.EmailTemplateTypeRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service
import java.util.regex.Matcher
import java.util.regex.Pattern
import javax.mail.Address
import javax.mail.Message
import javax.mail.internet.InternetAddress

/**
 * Created by Codetector on 2017/4/12.
 * Project backend
 */
@Service
class EmailService {
    @Value("\${spring.mail.from:}")
    lateinit var from: String

    @Autowired
    lateinit var mailSender: JavaMailSender

    @Autowired
    lateinit var emailTemplateRepository: EmailTemplateRepository

    @Autowired
    lateinit var emailTemplateTypeRepository: EmailTemplateTypeRepository

    fun sendMail(templateName: String, values: HashMap<String, String>, vararg recipients: String) {
        val template = MailTemplateFactory.createTemplateByFileName(if (templateName.endsWith(".template", true)) templateName else templateName + ".template")
        values.forEach {
            template.setStringValue(it.key, it.value)
        }
        sendMail(template, *recipients)
    }

    fun sendMail(template: MailTemplate, vararg recipients: String) {
        sendMailWithTitle(template, "AOF Check In System", *recipients)
    }

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

    fun submitTemplate(type: EmailTemplateTypeEnum, title: String, body: String): EmailTemplate {
        val templateType = emailTemplateTypeRepository.findEmailTemplateTypeByTemplateType(type).get()
        val listFound = Pattern.compile("\\{\\{ *([0-9A-Za-z]+) *}}").matcher(body).let {
            val list = mutableListOf<String>()
            while (it.find()) {
                list.add(it.group(1))
            }
            list
        }
        val setFound = listFound.toSet()
        if (listFound.size > setFound.size) {
            // Duplicate names
            throw IllegalArgumentException("Duplicate names")
        }
        val standardSet = templateType.variables.map { it.name }.toSet()
        if (setFound == standardSet) {
            // Validated
            return emailTemplateRepository.save(EmailTemplate(
                    templateType = templateType,
                    title = title,
                    body = body
            ))
        }
        // Invalid
        throw IllegalArgumentException("Invalid")
    }
}