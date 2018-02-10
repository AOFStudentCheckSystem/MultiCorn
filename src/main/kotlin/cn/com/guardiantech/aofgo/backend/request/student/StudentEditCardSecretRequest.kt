package cn.com.guardiantech.aofgo.backend.request.student

import cn.com.guardiantech.aofgo.backend.annotation.NoArg
import javax.validation.constraints.NotNull

/**
 * Created by dedztbh on 18-1-18.
 * Project AOFGoBackend
 */
@NoArg
data class StudentEditCardSecretRequest(
        @field:NotNull
        val idNumber: String,
        @field:NotNull
        val cardSecret: String?
)