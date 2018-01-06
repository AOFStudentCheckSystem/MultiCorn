package cn.com.guardiantech.aofgo.backend.request.checkin

import cn.com.guardiantech.aofgo.backend.annotation.NoArg

/**
 * Created by dedztbh on 1/5/18.
 * Project AOFGoBackend
 */

@NoArg
class CheckInSubmissionResponse(
        val targetEvent: String,
        val totalRecordsReceived: Int,
        val validRecords: Int,
        val effectiveRecords: Int
)