package cn.com.guardiantech.aofgo.backend.data.entity.checkin

import cn.com.guardiantech.aofgo.backend.data.entity.Student
import cn.com.guardiantech.aofgo.backend.data.entity.authentication.Permission

interface Token {
    var tokenSecret: String
    fun isAuthenticated(permission: Permission): Boolean
    fun student(): Student?
}