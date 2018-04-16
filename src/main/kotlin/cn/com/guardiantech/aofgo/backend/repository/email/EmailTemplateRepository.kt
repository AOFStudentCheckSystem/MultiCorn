package cn.com.guardiantech.aofgo.backend.repository.email

import cn.com.guardiantech.aofgo.backend.data.entity.email.EmailTemplate
import cn.com.guardiantech.aofgo.backend.data.entity.email.EmailTemplateTypeEnum
import org.springframework.data.repository.CrudRepository
import java.util.*

/**
 * Created by dedztbh on 18-2-13.
 * Project AOFGoBackend
 */
interface EmailTemplateRepository : CrudRepository<EmailTemplate, Long> {
    fun findByName(name: String): Optional<EmailTemplate>
    fun findByNameAndTemplateType(name: String, templateType: EmailTemplateTypeEnum): Optional<EmailTemplate>
}