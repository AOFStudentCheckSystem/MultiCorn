package cn.com.guardiantech.aofgo.backend.request.authentication.registraion

import org.hibernate.validator.constraints.Length
import javax.validation.constraints.NotNull

data class UsernameValidationRequest(
    @field: NotNull
    @field: Length(min = 4, max = 32)
    val username: String
)