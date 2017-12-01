package cn.com.guardiantech.aofgo.backend

import cn.com.guardiantech.aofgo.backend.util.ConfigurationUtil
import cn.com.guardiantech.aofgo.backend.util.HibernateUtil
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
    lateinit var sf: SessionFactory

    @JvmStatic
    fun main(args: Array<String>) {
        val beginTS = System.currentTimeMillis()
        logger.info("Starting Application...")

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

        logger.debug("Hibernate Configuration: ${config.properties}")
        this.sf = config.buildSessionFactory()
        val session = this.sf.openSession()
        session.beginTransaction()
        session.createNativeQuery("SELECT 1;")
        session.transaction.commit()
        session.close()

    }
}