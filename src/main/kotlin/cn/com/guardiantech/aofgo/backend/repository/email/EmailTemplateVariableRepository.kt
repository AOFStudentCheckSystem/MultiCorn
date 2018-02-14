package cn.com.guardiantech.aofgo.backend.repository.email

import cn.com.guardiantech.aofgo.backend.data.entity.email.EmailTemplateVariable
import org.springframework.data.repository.CrudRepository

/**
 * Created by dedztbh on 18-2-13.
 * Project AOFGoBackend
 */
interface EmailTemplateVariableRepository : CrudRepository<EmailTemplateVariable, Long> {
}