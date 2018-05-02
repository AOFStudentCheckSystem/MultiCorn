package cn.com.guardiantech.aofgo.backend.request.checkin

import cn.com.guardiantech.aofgo.backend.annotation.NoArg
import cn.com.guardiantech.aofgo.backend.data.entity.checkin.EventStatus
import com.fasterxml.jackson.annotation.JsonProperty
import javax.validation.constraints.NotNull

/**
 * Created by dedztbh on 1/5/18.
 * Project AOFGoBackend
 */
@NoArg
data class RecordToUpload(
        @field:NotNull
        val timestamp: Long,
        @get:JsonProperty(defaultValue = "BOARDING")
        val status: EventStatus = EventStatus.BOARDING,
        @field:NotNull
        val studentId: String
)