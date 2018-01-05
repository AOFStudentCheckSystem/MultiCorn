package cn.com.guardiantech.aofgo.backend.annotation

annotation class Require(
        val permissions: Array<String> = []
)