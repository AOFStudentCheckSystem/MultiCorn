package cn.com.guardiantech.aofgo.backend.repository.email

import cn.com.guardiantech.aofgo.backend.data.entity.email.EmailDefaultTemplate
import cn.com.guardiantech.aofgo.backend.data.entity.email.EmailTemplateTypeEnum
import org.springframework.data.repository.CrudRepository
import java.util.*

/**
 * Created by dedztbh on 18-2-13.
 * Project AOFGoBackend
 */
interface EmailDefaultTemplateRepository : CrudRepository<EmailDefaultTemplate, EmailTemplateTypeEnum> {
    fun findByTemplateType(templateType: EmailTemplateTypeEnum): Optional<EmailDefaultTemplate>
}