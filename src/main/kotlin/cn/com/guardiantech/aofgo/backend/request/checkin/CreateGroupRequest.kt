package cn.com.guardiantech.aofgo.backend.request.checkin

import cn.com.guardiantech.aofgo.backend.annotation.NoArg
import cn.com.guardiantech.aofgo.backend.data.entity.checkin.EventStatus
import java.util.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

/**
 * Created by dedztbh on 1/6/18.
 * Project AOFGoBackend
 */
@NoArg
class CreateGroupRequest(
        @NotNull
        val name: String,
        val groupItems: Array<String> = arrayOf()
)