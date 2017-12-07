package cn.com.guardiantech.aofgo.backend.util

import cn.com.guardiantech.aofgo.backend.AOFGOBackendMain
import org.hibernate.Session
import org.reflections.Reflections
import javax.persistence.Entity
import javax.persistence.EntityManager
import javax.persistence.FlushModeType
import kotlin.coroutines.experimental.suspendCoroutine

/**
 * Created by Codetector on 30/11/2017.
 * Project aofgo-backend
 */
object HibernateUtil {
    fun findEntityClassesInPackage(packageName: String? = null, annotation: Class<out Annotation> = Entity::class.java): Set<Class<*>> {
        val reflect = if (packageName == null) Reflections() else Reflections(packageName)
        return reflect.getTypesAnnotatedWith(annotation)
    }
}

suspend fun <T> entityManagerSuspend(f: (em: EntityManager) -> T): T = suspendCoroutine {
    val em = AOFGOBackendMain.sessionFactory.createEntityManager()
    try {
        em.transaction.begin()
        it.resume(f(em))
        em.transaction.commit()
    } catch (t: Throwable) {
        em.transaction.rollback()
        it.resumeWithException(t)
    } finally {
        em.close()
    }
}

fun <T> entityManager(f: (em: EntityManager) -> T): T {
    val em = AOFGOBackendMain.sessionFactory.createEntityManager()
    em.flushMode = FlushModeType.COMMIT
    val rtn = f(em)
    em.close()
    return rtn
}

fun <T> session(f: (s: Session) -> T): T {
    val session = AOFGOBackendMain.sessionFactory.openSession()
    val rtn = f(session)
    session.close()
    return rtn
}

fun <T> transactional(em: EntityManager, f: (em: EntityManager) -> T): T {
    try {
        em.transaction.begin()
        val rtn = f(em)
        em.transaction.commit()
        return rtn
    } catch (e: Throwable) {
        em.transaction.rollback()
        throw e
    }
}