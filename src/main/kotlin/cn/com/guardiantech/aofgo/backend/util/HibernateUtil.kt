package cn.com.guardiantech.aofgo.backend.util

import cn.com.guardiantech.aofgo.backend.AOFGOBackendMain
import org.reflections.Reflections
import javax.persistence.Entity
import javax.persistence.EntityManager
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

suspend fun <T> entityManager(f: (em: EntityManager) -> T): T = suspendCoroutine {
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