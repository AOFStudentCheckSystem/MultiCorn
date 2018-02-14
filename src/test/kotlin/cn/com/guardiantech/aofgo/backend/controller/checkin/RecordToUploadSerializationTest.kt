package cn.com.guardiantech.aofgo.backend.controller.checkin

import cn.com.guardiantech.aofgo.backend.request.checkin.RecordToUpload
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.json.JsonTest
import org.springframework.boot.test.json.JacksonTester
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@JsonTest
class RecordToUploadSerializationTest {

    @Autowired
    lateinit var jacksonTest: JacksonTester<RecordToUpload>

    @Before
    fun preTest() {
        assertNotNull(jacksonTest)
    }

    @Test
    fun testDeserialization() {
        val jsonWithStatus = """
            {
            "timestamp": 123456789,
            "studentId": "123456798",
            "status": 1
            }
        """.trimIndent()
        val resultWithStatus = jacksonTest.parseObject(jsonWithStatus)
        assertNotNull(resultWithStatus)
        assertEquals(123456789, resultWithStatus.timestamp)
        assertEquals(1, resultWithStatus.status)

        val jsonWithOutStatus = """
            {
            "timestamp": 123456789,
            "studentId": "123456798"
            }
        """.trimIndent()
        val resultWithOutStatus = jacksonTest.parseObject(jsonWithOutStatus)
        assertNotNull(resultWithOutStatus)
        assertEquals(123456789, resultWithOutStatus.timestamp)
        assertEquals(1, resultWithOutStatus.status)
    }

}