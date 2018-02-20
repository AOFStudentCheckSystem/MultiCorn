package cn.com.guardiantech.aofgo.backend.service.email

import cn.com.guardiantech.aofgo.backend.data.entity.email.EmailTemplate
import cn.com.guardiantech.aofgo.backend.data.entity.email.EmailTemplateVariableType
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.regex.Pattern


/**
 * Created by Codetector on 2017/4/12.
 * Project backend
 */

@Service
class EmailTemplatingService {

    private val logger: Logger = LoggerFactory.getLogger(EmailTemplatingService::class.java)

    fun compileTemplate(template: EmailTemplate, values: Map<String, Any>): Pair<String, String> {
        var compiledTitle = template.title
        var compiledBody = template.body
        template.templateType.variables.forEach {
            val regex = Pattern.compile("\\{\\{ *${it.name} *}}")
            val value = values[it.name]
            val compiledVar = when (it.type) {
                EmailTemplateVariableType.STRING -> value as String
                EmailTemplateVariableType.LIST -> {
                    val sb = StringBuilder()
                    @Suppress("UNCHECKED_CAST")
                    (value as Iterable<String>).forEach {
                        sb.append("$it<br>")
                    }
                    sb.toString()
                }
                EmailTemplateVariableType.LINK -> {
                    val processedPair = if (value is String) {
                        Pair(value, value)
                    } else {
                        @Suppress("UNCHECKED_CAST")
                        value as Pair<String, String>
                    }
                    """<a href="${processedPair.second}">${processedPair.first}</a>"""
                }
            }
            compiledTitle = regex.matcher(compiledTitle).replaceAll(compiledVar)
            compiledBody = regex.matcher(compiledBody).replaceAll(compiledVar)
        }
        return Pair(compiledTitle, compiledBody)
    }
}