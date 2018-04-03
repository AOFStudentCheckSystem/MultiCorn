package cn.com.guardiantech.aofgo.backend.request.slip

import cn.com.guardiantech.aofgo.backend.annotation.NoArg
import cn.com.guardiantech.aofgo.backend.data.entity.slip.LeaveStatus
import javax.validation.constraints.NotNull

/**
 * Created by dedztbh on 18-3-27.
 * Project AOFGoBackend
 */
@NoArg
class LeaveRequestStatusRequest(
        @field: NotNull
        val status: LeaveStatus
)