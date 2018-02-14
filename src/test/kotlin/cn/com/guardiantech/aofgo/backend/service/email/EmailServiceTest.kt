package cn.com.guardiantech.aofgo.backend.service.email

import cn.com.guardiantech.aofgo.backend.BackendApplication
import cn.com.guardiantech.aofgo.backend.BackendApplicationTestConfiguration
import cn.com.guardiantech.aofgo.backend.data.entity.email.EmailTemplateType
import cn.com.guardiantech.aofgo.backend.data.entity.email.EmailTemplateTypeEnum
import cn.com.guardiantech.aofgo.backend.data.entity.email.EmailTemplateVariable
import cn.com.guardiantech.aofgo.backend.data.entity.email.EmailTemplateVariableType
import cn.com.guardiantech.aofgo.backend.repository.email.EmailTemplateRepository
import cn.com.guardiantech.aofgo.backend.repository.email.EmailTemplateTypeRepository
import cn.com.guardiantech.aofgo.backend.repository.email.EmailTemplateVariableRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.junit4.SpringRunner

/**
 * Created by dedztbh on 18-2-13.
 * Project AOFGoBackend
 */
@RunWith(SpringRunner::class)
@SpringBootTest(classes = [BackendApplication::class])
@DataJpaTest
@Import(BackendApplicationTestConfiguration::class)
class EmailServiceTest {

    @Autowired
    lateinit var emailService: EmailService

    @Autowired
    lateinit var emailTemplateRepository: EmailTemplateRepository

    @Autowired
    lateinit var emailTemplateTypeRepository: EmailTemplateTypeRepository

    @Autowired
    lateinit var emailTemplateVariableRepository: EmailTemplateVariableRepository

    @Before
    fun prepare() {

    }

    @Test
    fun submitTemplate() {
        val checkin = emailTemplateTypeRepository.save(
                EmailTemplateType(
                        templateType = EmailTemplateTypeEnum.CHECKIN,
                        variables = emailTemplateVariableRepository.save(
                                setOf(
                                        EmailTemplateVariable(
                                                name = "test",
                                                type = EmailTemplateVariableType.STRING
                                        ),
                                        EmailTemplateVariable(
                                                name = "001",
                                                type = EmailTemplateVariableType.STRING
                                        )
                                )
                        ).toMutableSet()
                )
        )
        assertEquals(2, checkin.variables.size)
        assertEquals(2, emailTemplateTypeRepository.findAll().first().variables.size)

        emailService.submitTemplate(checkin.templateType, "This is a test", "This should pass {{001}} the {{ test }}")
        assertEquals(1, emailTemplateRepository.count())

        emailService.submitTemplate(checkin.templateType, "This is a {{ 001 }}{{", "g}}This should also pass the {{ test }}")
        assertEquals(2, emailTemplateRepository.count())

        try {
            emailService.submitTemplate(checkin.templateType, "This is a test3", "This should not pass the test {{001}}")
            fail()
        } catch (e: IllegalArgumentException) {
            assertEquals("Invalid", e.message)
        }
    }
}