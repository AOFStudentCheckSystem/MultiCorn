package cn.com.guardiantech.aofgo.backend.request.authentication

import cn.com.guardiantech.aofgo.backend.annotation.NoArg
import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.NotNull

@NoArg
data class RegisterRequest(
        @field:NotNull
        val principal: PrincipalRequest,
        @field:NotNull
        val credential: CredentialRequest,
        @field:JsonProperty(defaultValue = "")
        val subjectAttachedInfo: String
)
