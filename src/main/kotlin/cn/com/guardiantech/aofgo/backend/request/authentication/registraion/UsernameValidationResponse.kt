package cn.com.guardiantech.aofgo.backend.request.authentication.registraion

data class UsernameValidationResponse (
        val username: String,
        val result: UsernameValidationResult
)