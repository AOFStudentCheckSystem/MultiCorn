package cn.com.guardiantech.aofgo.backend.util

import cn.com.guardiantech.aofgo.backend.annotation.PathVariable
import cn.com.guardiantech.aofgo.backend.annotation.RouteMapping
import cn.com.guardiantech.aofgo.backend.routing.RouteInfo
import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.ObjectMapper
import io.vertx.core.http.HttpMethod
import org.slf4j.Logger
import java.io.IOException
import java.lang.reflect.Method
import java.net.URLDecoder

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

    fun fillPathParam(route: RouteInfo, processedRoute: String, method: Method, arrayOfParams: Array<Any?>) {
        val matcher = route.getMatcher(processedRoute)
        if (matcher.find()) {
            // Valid Route

            method.parameters.forEachIndexed { index, parameter ->
                if (parameter.isAnnotationPresent(PathVariable::class.java)) {
                    val annotation = parameter.getDeclaredAnnotation(PathVariable::class.java)

                    val paramName = if (annotation.name.isNotEmpty()) annotation.name.toLowerCase() else parameter.name.toLowerCase()

                    try {
                        var matchResult: Any = URLDecoder.decode((matcher.group(paramName) ?: ""), "UTF-8")
                        if (!parameter.type.isAssignableFrom(String::class.java)) {
                            matchResult = ObjectMapper().readValue(matchResult as String, parameter.type.javaClass)
                        }
                        arrayOfParams[index] = matchResult
                    } catch (e: IllegalArgumentException) {
                        // No Capture Group Found
                    } catch (e: JsonMappingException) {

                    } catch (e: JsonParseException) {

                    } catch (e: IOException) {

                    }
                    // TODO Handle Exceptions
                }
            }

        } else {
            throw IllegalArgumentException("Failed to match route")
        }

    }
}