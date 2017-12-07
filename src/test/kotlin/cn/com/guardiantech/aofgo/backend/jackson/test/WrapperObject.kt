package cn.com.guardiantech.aofgo.backend.jackson.test

import cn.com.guardiantech.aofgo.backend.annotation.NoArg

@NoArg
data class WrapperObject(
        val magic: SimpleTestObject,
        val test: String
)