package cn.com.guardiantech.aofgo.backend.request.authentication.registraion

data class EmailValidationResponse(
        val address: String,
        val result: EmailValidationResult
)