package cn.com.guardiantech.aofgo.backend.request.student

import cn.com.guardiantech.aofgo.backend.annotation.NoArg
import cn.com.guardiantech.aofgo.backend.data.entity.Gender
import java.util.*
import javax.validation.constraints.NotNull

@NoArg
data class StudentRequest(
        @NotNull
        val idNumber: String,
        val cardSecret: String?,
        val grade: Int?,
        val gender: Gender?,
        val dateOfBirth: Date?,
        val dorm: String?,
        val dormInfo: String?,
        //Id
        val accountId: Long?
)