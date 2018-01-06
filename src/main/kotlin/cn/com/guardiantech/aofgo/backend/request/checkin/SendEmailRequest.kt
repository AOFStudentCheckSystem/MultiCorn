package cn.com.guardiantech.aofgo.backend.request.checkin

import cn.com.guardiantech.aofgo.backend.annotation.NoArg

/**
 * Created by dedztbh on 1/6/18.
 * Project AOFGoBackend
 */
@NoArg
data class SendEmailRequest(
        val eventId: String,
        val address: String
)