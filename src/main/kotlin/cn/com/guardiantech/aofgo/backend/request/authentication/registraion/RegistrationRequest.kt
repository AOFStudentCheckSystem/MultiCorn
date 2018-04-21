package cn.com.guardiantech.aofgo.backend.request.authentication.registraion

import cn.com.guardiantech.aofgo.backend.annotation.NoArg
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

@NoArg
data class RegistrationRequest(
        @field: NotNull
        val email: String,
        @field: NotNull
        val password: String,
        @field: Size(min = 4, max = 32)
        @field: Pattern(regexp = "^[A-Za-z][-.0-9A-Za-z_]*$")
        val username: String?
)
