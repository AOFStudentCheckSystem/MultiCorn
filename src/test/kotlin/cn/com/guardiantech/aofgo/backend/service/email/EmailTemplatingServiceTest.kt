package cn.com.guardiantech.aofgo.backend.service.email

import cn.com.guardiantech.aofgo.backend.BackendApplication
import cn.com.guardiantech.aofgo.backend.BackendApplicationTestConfiguration
import cn.com.guardiantech.aofgo.backend.data.entity.email.*
import cn.com.guardiantech.aofgo.backend.repository.email.EmailTemplateRepository
import cn.com.guardiantech.aofgo.backend.repository.email.EmailTemplateTypeRepository
import cn.com.guardiantech.aofgo.backend.repository.email.EmailTemplateVariableRepository
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.junit4.SpringRunner

/**
 * Created by dedztbh on 18-2-14.
 * Project AOFGoBackend
 */
@RunWith(SpringRunner::class)
@SpringBootTest(classes = [BackendApplication::class])
@DataJpaTest
@Import(BackendApplicationTestConfiguration::class)
class EmailTemplatingServiceTest {

    @Autowired
    lateinit var emailService: EmailService
    @Autowired
    lateinit var emailTemplatingService: EmailTemplatingService
    @Autowired
    lateinit var emailTemplateRepository: EmailTemplateRepository
    @Autowired
    lateinit var emailTemplateTypeRepository: EmailTemplateTypeRepository
    @Autowired
    lateinit var emailTemplateVariableRepository: EmailTemplateVariableRepository

    @Test
    fun compileTemplate() {
        val checkin = emailTemplateRepository.save(EmailTemplate(
                name = "test",
                title = "This is a {{ test }}",
                body = "This should pass {{ 001}} the {{ test }} aaa{{a}}",
                templateType = emailTemplateTypeRepository.save(
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
                                                        type = EmailTemplateVariableType.LIST
                                                ),
                                                EmailTemplateVariable(
                                                        name = "a",
                                                        type = EmailTemplateVariableType.LINK
                                                )
                                        )
                                ).toMutableSet()
                        )
                )
        ))
        assertEquals(3, checkin.templateType.variables.size)
        assertEquals(3, emailTemplateTypeRepository.findAll().first().variables.size)

        val compiledPair = emailTemplatingService.compileTemplate(checkin, mutableMapOf<String, Any>().let {
            it["test"] = "www"
            it["001"] = mutableSetOf("a", "b")
            it["a"] = Pair("a", "b")
            it
        })
        assertEquals("This is a www", compiledPair.first)
        assertEquals("This should pass a<br>b<br> the www aaa<a href=\"b\">a</a>", compiledPair.second)
    }
}