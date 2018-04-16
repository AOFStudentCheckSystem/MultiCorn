package cn.com.guardiantech.aofgo.backend.request.student

import cn.com.guardiantech.aofgo.backend.annotation.NoArg
import javax.validation.constraints.NotNull

@NoArg
data class StudentSearchRequest(
        @field: NotNull
        val column: StudentSearchColumn = StudentSearchColumn.FUZZY,
        @field: NotNull
        val search: String
)