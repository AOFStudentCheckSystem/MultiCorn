package cn.com.guardiantech.aofgo.backend.util

import org.reflections.Reflections
import javax.persistence.Entity

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