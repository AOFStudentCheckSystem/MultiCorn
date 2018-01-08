package cn.com.guardiantech.aofgo.backend.request.authentication

import cn.com.guardiantech.aofgo.backend.annotation.NoArg
import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.NotNull

@NoArg
data class RegisterRequest(
        @NotNull
        val principal: PrincipalRequest,
        @NotNull
        val credential: CredentialRequest,
        @JsonProperty(defaultValue = "")
        val subjectAttachedInfo: String
)
