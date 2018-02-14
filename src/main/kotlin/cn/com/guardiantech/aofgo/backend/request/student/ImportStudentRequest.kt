package cn.com.guardiantech.aofgo.backend.request.student

import cn.com.guardiantech.aofgo.backend.annotation.NoArg
import java.io.File
import javax.validation.constraints.NotNull

@NoArg
data class ImportStudentRequest(
    @field:NotNull
    val csvContent: File
)