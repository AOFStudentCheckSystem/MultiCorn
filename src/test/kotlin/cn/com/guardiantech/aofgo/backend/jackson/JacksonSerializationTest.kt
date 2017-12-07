package cn.com.guardiantech.aofgo.backend.jackson

import cn.com.guardiantech.aofgo.backend.jackson.test.SimpleTestObject
import cn.com.guardiantech.aofgo.backend.jackson.test.WrapperObject
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class JacksonSerializationTest {
    @Test
    fun serialize() {
        assertEquals("{\"magic\":\"MAGIC\",\"test\":\"\"}", ObjectMapper().writeValueAsString(WrapperObject(SimpleTestObject.MAGIC, "")))
    }

    @Test
    fun deserialize() {
        val magicInput = "{\"magic\":\"MAGIC\",\"test\":\"\"}"
        val objW = ObjectMapper().readValue(magicInput, WrapperObject::class.java)
        assertNotNull(objW)
        assertNotNull(objW.magic)

//        val magic = ObjectMapper().enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING).readValue("{\"principal\":{\"type\":\"PHONE\",\"identification\":\"magicPhone\"},\"credential\":{\"type\":\"PASSWORD\",\"secret\":\"MAGIC\"},\"subjectAttachedInfo\":\"\"}", RegisterRequest::class.java)
//        assertNotNull(magic)
    }
}