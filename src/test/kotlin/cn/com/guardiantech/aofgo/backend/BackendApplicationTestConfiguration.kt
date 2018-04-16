package cn.com.guardiantech.aofgo.backend

import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.autoconfigure.mail.MailSenderAutoConfiguration
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.EnableWebMvc


/**
 * Created by dedztbh on 1/6/18.
 * Project AOFGoBackend
 */
@Configuration
@ComponentScan("cn.com.guardiantech.aofgo.backend")
@ImportAutoConfiguration(classes = [MailSenderAutoConfiguration::class])
//@Transactional(transactionManager = "hibernateTransactionManager")
//@TestExecutionListeners
@EnableWebMvc
class BackendApplicationTestConfiguration {
//    @Autowired
//    private val env: Environment? = null
//
//    @Bean
//    fun sessionFactory(): LocalSessionFactoryBean {
//        val sessionFactory = LocalSessionFactoryBean()
//        sessionFactory.setDataSource(applicationDataSource())
//        sessionFactory.setPackagesToScan("com.hibernate.query.performance.persistence.model")
//        sessionFactory.hibernateProperties = hibernateProperties()
//
//        return sessionFactory
//    }
//
//    @Bean
//    fun entityManagerFactory(): LocalContainerEntityManagerFactoryBean {
//        val emf = LocalContainerEntityManagerFactoryBean()
//        emf.dataSource = applicationDataSource()
//        emf.setPackagesToScan("com.hibernate.query.performance.persistence.model")
//
//        val vendorAdapter = HibernateJpaVendorAdapter()
//        emf.jpaVendorAdapter = vendorAdapter
//        emf.setJpaProperties(hibernateProperties())
//
//        return emf
//    }
//
//    @Bean
//    fun applicationDataSource(): DataSource {
//        val ds = JdbcDataSource()
//        ds.setURL("jdbc:h2:˜/test")
//        ds.user = "sa"
//        ds.password = "sa"
//        val ctx = InitialContext()
//        ctx.bind("jdbc/dsName", ds)
//        return ds
//    }
//
//    @Bean
//    fun hibernateTransactionManager(): PlatformTransactionManager { // TODO: Really need this?
//        val transactionManager = HibernateTransactionManager()
//        transactionManager.setSessionFactory(sessionFactory().getObject())
//        return transactionManager
//    }
//
//    @Bean
//    fun jpaTransactionManager(): PlatformTransactionManager { // TODO: Really need this?
//        val transactionManager = JpaTransactionManager() // http://stackoverflow.com/questions/26562787/hibernateexception-couldnt-obtain-transaction-synchronized-session-for-current
//        transactionManager.entityManagerFactory = entityManagerFactory().`object`
//        return transactionManager
//    }
//
//    @Bean
//    fun exceptionTranslation(): PersistenceExceptionTranslationPostProcessor {
//        return PersistenceExceptionTranslationPostProcessor()
//    }
//
//    private fun hibernateProperties(): Properties {
//        val hibernateProperties = Properties()
//        hibernateProperties.setProperty("hibernate.hbm2ddl.auto", env!!.getProperty("hibernate.hbm2ddl.auto"))
//        hibernateProperties.setProperty("hibernate.dialect", env!!.getProperty("hibernate.dialect"))
//
//        hibernateProperties.setProperty("hibernate.show_sql", "true")
//        hibernateProperties.setProperty("hibernate.format_sql", "true")
//        // hibernateProperties.setProperty("hibernate.globally_quoted_identifiers", "true");
//        hibernateProperties.setProperty("hibernate.cache.region.factory_class", "org.hibernate.cache.ehcache.EhCacheRegionFactory")
//
//        // Envers properties
//        hibernateProperties.setProperty("org.hibernate.envers.audit_table_suffix", env!!.getProperty("envers.audit_table_suffix")) // TODO: Really need this?
//
//        return hibernateProperties
//    }
}