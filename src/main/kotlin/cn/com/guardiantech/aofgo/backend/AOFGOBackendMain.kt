package cn.com.guardiantech.aofgo.backend

import cn.com.guardiantech.aofgo.backend.util.ConfigurationUtil
import cn.com.guardiantech.aofgo.backend.util.HibernateUtil
import cn.com.guardiantech.aofgo.backend.util.*
import kotlinx.coroutines.experimental.runBlocking
import org.hibernate.SessionFactory
import org.hibernate.cfg.Configuration
import org.hibernate.cfg.Environment
import org.slf4j.LoggerFactory

/**
 * Created by Codetector on 29/11/2017.
 * Project aofgo-backend
 */

object AOFGOBackendMain {
    private val logger = LoggerFactory.getLogger(AOFGOBackendMain::class.java)
    lateinit var sessionFactory: SessionFactory

    @JvmStatic
    fun main(args: Array<String>) {
        System.setProperty("org.jboss.logging.provider", "slf4j");
        val beginTS = System.currentTimeMillis()
        logger.info("Starting Application...")

        // Configure and Initialize Hibernate
        logger.info("Initializing Hibernate...")
        logger.info("Loading Hibernate Configuration...")
        val config = Configuration().configure(this.javaClass.getResource("/persistence.xml"))
        // Register Entity Mapping
        logger.info("============= BEGIN Entity Mapping =============")
        val entities = HibernateUtil.findEntityClassesInPackage()
        entities.forEach {
            logger.info("${it.simpleName} => ${it.typeName}")
            config.addAnnotatedClass(it)
        }
        logger.info("============== END Entity Mapping ==============")

        config.setProperty(Environment.URL, "jdbc:mysql://127.0.0.1:3306/aofgo?useSSL=false")
        config.setProperty(Environment.USER, ConfigurationUtil.getSystemProperty("dbuser"))
        config.setProperty(Environment.PASS, ConfigurationUtil.getSystemProperty("dbpass"))
        logger.trace("Hibernate Configuration: ${config.properties}")
        this.sessionFactory = config.buildSessionFactory()
        runBlocking {
            entityManager {
                val hibernateTest = it.createNativeQuery("SELECT 1;").singleResult
            }
        }
        logger.info("Hibernate Initialized")
    }
}