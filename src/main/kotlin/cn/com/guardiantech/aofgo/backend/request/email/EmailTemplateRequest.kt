package cn.com.guardiantech.aofgo.backend.request.email

import cn.com.guardiantech.aofgo.backend.annotation.NoArg
import cn.com.guardiantech.aofgo.backend.data.entity.email.EmailTemplateTypeEnum
import javax.validation.constraints.NotNull

/**
 * Created by dedztbh on 18-2-13.
 * Project AOFGoBackend
 */
@NoArg
class EmailTemplateRequest(
        @field:NotNull
        val templateType: EmailTemplateTypeEnum,
        @field:NotNull
        val title: String,
        @field:NotNull
        val body: String
)