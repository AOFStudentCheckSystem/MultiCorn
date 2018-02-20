package cn.com.guardiantech.aofgo.backend.request.student

import cn.com.guardiantech.aofgo.backend.annotation.NoArg
import cn.com.guardiantech.aofgo.backend.data.entity.GuardianType
import javax.validation.constraints.NotNull

@NoArg
data class GuardianRequest(
        @field:NotNull
        val accountId: Long,
        @field:NotNull
        val relation: GuardianType
)