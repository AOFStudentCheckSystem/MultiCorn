package cn.com.guardiantech.aofgo.backend.routing

import cn.com.guardiantech.aofgo.backend.annotation.Controller
import cn.com.guardiantech.aofgo.backend.annotation.RouteMapping
import io.vertx.core.Vertx
import io.vertx.core.http.HttpMethod
import io.vertx.core.http.HttpServerRequest
import org.reflections.Reflections
import org.slf4j.LoggerFactory
import java.lang.reflect.Method
import java.util.*
import kotlin.NoSuchElementException

/**
 * Created by Codetector on 01/12/2017.
 * Project aofgo-backend
 */
class RootRouter(val vertx: Vertx, private val basePackage: String? = null) {
    companion object {
        private val logger = LoggerFactory.getLogger(RootRouter::class.java)
    }

    internal val routeMapping: MutableMap<RouteInfo, Method> = hashMapOf()

    internal val controllerMapping: MutableMap<Class<*>, Any> = hashMapOf()

    fun initialize() {
        logger.info("Initializing RootRouter Mappings...")
        val reflection = if (basePackage != null) Reflections(basePackage) else Reflections()

        logger.info("Locating @Controller Classes")
        val classes = reflection.getTypesAnnotatedWith(Controller::class.java)
        logger.info("${classes.size} Class(es) Found.")
        classes.forEach { clazz ->
            logger.debug("Routing @Controller class: ${clazz.simpleName}")
            val routeMappingAnnotation: RouteMapping? = clazz.getDeclaredAnnotation(RouteMapping::class.java)

            // Check For Parent RequestMapping Configurations
            val parentRoute: String
            val parentMethod: Array<HttpMethod>
            if (routeMappingAnnotation != null) {
                parentRoute = routeMappingAnnotation.path
                parentMethod = routeMappingAnnotation.method
            } else {
                parentRoute = ""
                parentMethod = arrayOf()
            }
            logger.debug("@Controller class: ${clazz.simpleName} has rootRoute of: $parentRoute and Methods: ${Arrays.toString(parentMethod)}")

            var hasRoutingFunction = false

            clazz.declaredMethods.forEach { function ->
                logger.trace("Processing Method: ${function.name}")
                val annotation = function.getDeclaredAnnotation(RouteMapping::class.java)
                if (annotation != null) {
                    logger.debug("Method ${function.name} found with @RouteMapping annotation present.")
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
                    logger.debug("Method ${function.name} has resolved to $routeInfo")
                    routeMapping.put(routeInfo, function)
                }
            }

            // Has at least one(1) routing implementation, instantiate and record the class
            if (hasRoutingFunction) {
                // TODO: Autowire after implementing service system
                try {
                    val instance = clazz.newInstance()
                    controllerMapping.put(clazz, instance)
                } catch (e: Throwable) {
                    logger.warn("Failed to instantiate a controller: ${clazz.simpleName}", e)
                }
            }

        }
    }

    fun routeRequest(request: HttpServerRequest) {
        var processedRoute = request.path().replace("/\\z".toRegex(), "").replace("/+".toRegex(), "/")
        if (processedRoute.isEmpty()) {
            processedRoute = "/"
        }
        logger.debug("Accepted request: $processedRoute")
        // Start Matching
        try {
            val routeMapping = routeMapping.entries.first {
                it.key.matchRoute(processedRoute)
            }
            logger.debug("Route found -> ${routeMapping.value.name}")

        } catch (e: NoSuchElementException) {
            // TODO: Locate Failure Handler (Not Implemented yet)
            logger.debug("Route NOT found, returning 404 by default")
            request.response().setStatusCode(404).end("HTTP/1.1 - 404 NOT FOUND")
        }

    }


    /**
     * Start HttpServer on the specified port
     * @param port, the port value range 1 - 65535 (unsigned short max)
     */
    fun listen(port: Int) {
        vertx.createHttpServer().requestHandler { request ->
            this.routeRequest(request)
            // TODO: Implement Request Handling Processer
        }.listen(port)
    }
}