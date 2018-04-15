package cn.com.guardiantech.aofgo.backend.request.authentication

import javax.validation.constraints.NotNull

data class EmailAddressSubmissionRequest(
        @NotNull
        val address: String
)