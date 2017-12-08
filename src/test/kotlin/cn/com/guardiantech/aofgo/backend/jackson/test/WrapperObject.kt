package cn.com.guardiantech.aofgo.backend.jackson.test

import cn.codetector.jet.controller.annotation.NoArg

@NoArg
data class WrapperObject(
        val magic: SimpleTestObject,
        val test: String
)