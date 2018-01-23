package cn.com.guardiantech.aofgo.backend.controller.checkin

import cn.com.guardiantech.aofgo.backend.BackendApplication
import cn.com.guardiantech.aofgo.backend.BackendApplicationTestConfiguration
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.Assert.*
import org.junit.Before
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.data.web.config.EnableSpringDataWebSupport
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest(classes = [BackendApplication::class])
@DataJpaTest
@Import(BackendApplicationTestConfiguration::class)
@AutoConfigureMockMvc
@EnableSpringDataWebSupport
class RecordToUploadSerializationTest {

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Before
    fun preTest() {
        assertNotNull(this.objectMapper)
    }

    fun test() {
//        objectMapper.readvalue
    }

}