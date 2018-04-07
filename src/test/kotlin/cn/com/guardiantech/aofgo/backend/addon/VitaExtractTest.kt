package cn.com.guardiantech.aofgo.backend.addon

import cn.com.guardiantech.aofgo.backend.BackendApplication
import cn.com.guardiantech.aofgo.backend.BackendApplicationTestConfiguration
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest(classes = [BackendApplication::class])
@Import(BackendApplicationTestConfiguration::class)
class VitaExtractTest {

    private var vitaExtract = VitaExtract()

    @Before
    fun setUp() {
        val cookie = vitaExtract.authenticate("fengy", "Peter206")
        print("\n\n\n\n\n\n\n")
        print("\n\n\n\n\n\n\n")
        print(cookie)
        print("\n\n\n\n\n\n\n")
        print(vitaExtract.extractCourseData(cookie))
        print("\n\n\n\n\n\n\n")
        print(vitaExtract.extractCalendarData(cookie))
        print("\n\n\n\n\n\n\n")
        print("\n\n\n\n\n\n\n")
    }

    @Test
    fun test() {}
}