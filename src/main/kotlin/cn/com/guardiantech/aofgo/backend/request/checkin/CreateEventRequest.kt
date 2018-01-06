package cn.com.guardiantech.aofgo.backend.request.checkin

import cn.com.guardiantech.aofgo.backend.annotation.NoArg
import java.util.*
import javax.validation.constraints.NotNull

/**
 * Created by dedztbh on 1/6/18.
 * Project AOFGoBackend
 */
@NoArg
data class CreateEventRequest (
        @NotNull
        val name: String,
        val description: String = "",
        val time: Date = Date()
)