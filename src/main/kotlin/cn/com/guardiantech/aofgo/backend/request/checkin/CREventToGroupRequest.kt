package cn.com.guardiantech.aofgo.backend.request.checkin

import cn.com.guardiantech.aofgo.backend.annotation.NoArg
import javax.validation.constraints.NotNull

/**
 * Created by dedztbh on 1/6/18.
 * Project AOFGoBackend
 */
@NoArg
class CREventToGroupRequest(
        @field:NotNull
        val eventId: String
)