package cn.com.guardiantech.aofgo.backend.request.student

import cn.com.guardiantech.aofgo.backend.annotation.NoArg
import cn.com.guardiantech.aofgo.backend.data.entity.Gender
import java.util.*

@NoArg
data class StudentCreationRequest(
        val idNumber: String,
        val cardSecret: String,
        val grade: Int,
        val gender: Gender,
        val dateOfBirth: Date,
        val dorm: String,
        val dormInfo: String,
        //Id
        val accountId: Long?
)