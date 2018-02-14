package cn.com.guardiantech.aofgo.backend.request.checkin

import cn.com.guardiantech.aofgo.backend.annotation.NoArg
import javax.validation.constraints.NotNull

/**
 * Created by dedztbh on 1/5/18.
 * Project AOFGoBackend
 */
@NoArg
data class RecordToUpload(
        @field:NotNull
        val timestamp: Long,
        @get:JsonProperty(defaultValue = "1")
        val status: Int = 1,
        @field:NotNull
        val studentId: String
)