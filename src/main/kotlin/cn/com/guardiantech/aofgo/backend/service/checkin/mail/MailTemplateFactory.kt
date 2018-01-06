package cn.com.guardiantech.aofgo.backend.service.checkin.mail

import com.google.common.io.Resources
import java.io.File
import java.util.*

/**
 * Created by Codetector on 2017/4/12.
 * Project backend
 */
class MailTemplateFactory {
    companion object {
        fun createTemplateByFileName(fileName: String): MailTemplate {
            println(Arrays.toString(File(this::class.java.getResource("/email").path).list()))
            return MailTemplate(Resources.toString(this::class.java.getResource("/email/$fileName.template"), Charsets.UTF_8))
        }
    }
}