package cn.com.guardiantech.aofgo.backend.request.authentication

import cn.com.guardiantech.aofgo.backend.annotation.NoArg
import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.NotNull

@NoArg
data class SubjectRequest(
        @field:NotNull
        val principal: PrincipalRequest,
        @field:NotNull
        val credential: CredentialRequest,
        val subjectAttachedInfo: String = "",
        val roles: Set<String> = setOf()
)
