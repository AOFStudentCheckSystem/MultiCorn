package cn.com.guardiantech.aofgo.backend.request.authentication

import cn.com.guardiantech.aofgo.backend.annotation.NoArg
import com.fasterxml.jackson.annotation.JsonProperty

@NoArg
data class RegisterRequest (
    val principal: PrincipalRequest,
    val credential: CredentialRequest,
    @JsonProperty(defaultValue = "")
    val subjectAttachedInfo: String
)
