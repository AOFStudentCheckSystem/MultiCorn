package cn.com.guardiantech.aofgo.backend.request.checkin

import cn.com.guardiantech.aofgo.backend.annotation.NoArg
import javax.validation.constraints.NotNull

/**
 * Created by dedztbh on 1/5/18.
 * Project AOFGoBackend
 */

@NoArg
class CheckInSubmissionRequest(
        @field:NotNull
        val targetEvent: String,
        @field:NotNull
        val recordsToUpload: Array<RecordToUpload>
)