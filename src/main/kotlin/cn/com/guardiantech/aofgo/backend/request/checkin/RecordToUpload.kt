package cn.com.guardiantech.aofgo.backend.request.checkin

import javax.validation.constraints.NotNull

/**
 * Created by dedztbh on 1/5/18.
 * Project AOFGoBackend
 */
data class RecordToUpload(
        @NotNull
        val timestamp: Long,
        val status: Int = 1,
        @NotNull
        val studentId: String
)