package cn.com.guardiantech.aofgo.backend.service

import cn.com.guardiantech.aofgo.backend.BackendApplication
import cn.com.guardiantech.aofgo.backend.BackendApplicationTestConfiguration
import cn.com.guardiantech.aofgo.backend.repository.GuardianRepository
import cn.com.guardiantech.aofgo.backend.repository.StudentRepository
import cn.com.guardiantech.aofgo.backend.repository.auth.AccountRepository
import junit.framework.Assert.assertEquals
import junit.framework.Assert.fail
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.junit4.SpringRunner

/**
 * Created by dedztbh on 1/10/18.
 * Project AOFGoBackend
 */

@RunWith(SpringRunner::class)
@SpringBootTest(classes = [BackendApplication::class])
@DataJpaTest
@Import(BackendApplicationTestConfiguration::class)
@AutoConfigureMockMvc
class StudentServiceTest {
    @Autowired
    private lateinit var studentRepo: StudentRepository
    @Autowired
    private lateinit var accountRepo: AccountRepository
    @Autowired
    private lateinit var guardianRepository: GuardianRepository
    @Autowired
    private lateinit var studentService: StudentService

    //TODO: Use isPresent instead of try catch to prevent exception in repository
    @Test
    fun importStudentsFrom2DArrayTest() {
//        studentService.importStudentsFrom2DArray(
//                listOf(
//                        arrayOf("1", "3B880100030506649889C03C", "ELE 2-B7", "aaronw@avonoldfarms.com", "William", "11", "100127260", "Aaron", "Will", "1")
//                )
//        )
//        assertEquals(1L, studentRepo.count())
//        assertEquals(1L, accountRepo.count())
//        studentRepo.findByIdNumber("100127260").get().let {
//            assertEquals("3B880100030506649889C03C", it.cardSecret)
//            assertEquals("aaronw@avonoldfarms.com", it.account!!.email)
//        }
//
//        try {
//            studentService.importStudentsFrom2DArray(
//                    listOf(
//                            arrayOf("2", "3B880100030506649889C03C", "ELE 2-B7", "baronw@avonoldfarms.com", "William", "11", "200127260", "Baron", "Will", "1")
//                    )
//            )
//            fail()
//        } catch (e: Throwable) {
//            e.printStackTrace()
//        }
//        assertEquals(1L, studentRepo.count())
//        assertEquals(1L, accountRepo.count())
    }
}