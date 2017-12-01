package cn.com.guardiantech.aofgo.backend

import cn.com.guardiantech.aofgo.backend.util.HibernateUtil
import org.hibernate.SessionFactory
import org.hibernate.cfg.Configuration
import org.hibernate.cfg.Environment
import org.slf4j.LoggerFactory
import javax.persistence.EntityManagerFactory
import javax.persistence.Persistence

/**
 * Created by Codetector on 29/11/2017.
 * Project aofgo-backend
 */

object AOFGOBackendMain {
    private val logger = LoggerFactory.getLogger("Main")
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



        config.setProperty(Environment.JPA_JDBC_USER, "")
        config.setProperty(Environment.JPA_JDBC_PASSWORD, "")

        logger.debug("Hibernate Configuration: ", config.properties)
        this.sf = config.buildSessionFactory()
    }
}