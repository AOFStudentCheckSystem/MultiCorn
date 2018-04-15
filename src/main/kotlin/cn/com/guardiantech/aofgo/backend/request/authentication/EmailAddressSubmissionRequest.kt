package cn.com.guardiantech.aofgo.backend.request.authentication

import org.hibernate.validator.constraints.Length
import javax.validation.constraints.NotNull

data class EmailAddressSubmissionRequest(
        @NotNull
        val address: String
)