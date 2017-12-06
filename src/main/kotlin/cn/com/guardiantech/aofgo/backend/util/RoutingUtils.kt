package cn.com.guardiantech.aofgo.backend.util

import cn.com.guardiantech.aofgo.backend.annotation.RouteMapping
import cn.com.guardiantech.aofgo.backend.routing.RouteInfo
import io.vertx.core.http.HttpMethod
import org.slf4j.Logger
import java.lang.reflect.Method

/**
 * Created by Codetector on 05/12/2017.
 * Project aofgo-backend
 */

object RoutingUtils {
    /**
     * Process class add handler to the map.
     * @return does the class have at least one handler method (a valid controller)
     */
    fun processController(clazz: Class<*>, parentRoute: String, parentMethod: Array<HttpMethod>, routeMapping: MutableMap<RouteInfo, Method>, logger: Logger? = null): Boolean {
        var hasRoutingFunction = false
        clazz.declaredMethods.forEach { function ->
            logger?.trace("Processing Method: ${function.name}")
            val annotation = function.getDeclaredAnnotation(RouteMapping::class.java)
            if (annotation != null) {
                logger?.debug("Method ${function.name} found with @RouteMapping annotation present.")
                hasRoutingFunction = true
                val routeMethod = if (annotation.method.isEmpty()) parentMethod else annotation.method
                val routePath: String = {
                    val sb = StringBuilder()
                    if (parentRoute.isNotEmpty()) {
                        if (!parentRoute.startsWith("/")) {
                            sb.append("/")
                        }
                        sb.append(parentRoute)
                    }
                    if (annotation.path.isNotEmpty()) {
                        if (!annotation.path.startsWith("/")) {
                            sb.append('/')
                        }
                        sb.append(annotation.path)
                    }
                    sb.toString()
                }()
                val routeInfo = RouteInfo(routePath, routeMethod)
                logger?.debug("Method ${function.name} has resolved to $routeInfo")
                routeMapping.put(routeInfo, function)
            }
        }
        return hasRoutingFunction
    }

    fun fillSimpleAnnotationParams(annotation: Class<out Annotation>, objectToFill: Any, method: Method, arrayOfParams: Array<Any?>) {
        method.parameters.forEachIndexed { index, it ->
            if (it.isAnnotationPresent(annotation)) {
                if (it.type.isAssignableFrom(objectToFill::class.java)) {
                    arrayOfParams[index] = objectToFill
                } else {
                    IllegalArgumentException("Failed to Insert ${objectToFill.javaClass.name} is not compatable with ${it.type.simpleName}")
                }
            }
        }
    }
}