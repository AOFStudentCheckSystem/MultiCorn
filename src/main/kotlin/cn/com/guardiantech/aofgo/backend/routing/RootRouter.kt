package cn.com.guardiantech.aofgo.backend.routing

import cn.com.guardiantech.aofgo.backend.annotation.Controller
import cn.com.guardiantech.aofgo.backend.annotation.Handkerchief
import cn.com.guardiantech.aofgo.backend.annotation.RouteMapping
import cn.com.guardiantech.aofgo.backend.util.RoutingUtils
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
                parentMethod = HttpMethod.values()
            }
            logger.debug("@Controller class: ${clazz.simpleName} has rootRoute of: $parentRoute and Methods: ${Arrays.toString(parentMethod)}")

            val isClassValidController = RoutingUtils.processController(clazz, parentRoute, parentMethod, routeMapping, logger)

            // Has at least one(1) routing implementation, instantiate and record the class
            if (isClassValidController) {
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
        logger.debug("Accepted request: $processedRoute, ${request.method()} (${request.rawMethod()})")
        // Start Matching
        try {
            val routeMapping = routeMapping.entries.first {
                it.key.matchRoute(processedRoute) && it.key.method.contains(request.method())
            }
            logger.debug("Route found -> ${routeMapping.value.declaringClass.name}::${routeMapping.value.name}()")

            val handler = routeMapping.value
            val target = controllerMapping[handler.declaringClass]
            if (target != null) {
                // Resolve Arguments
                val paramValues = Array<Any?>(handler.parameterCount, {null})

                // Inject HttpRequest if Handkerchief annotation is found
                RoutingUtils.fillSimpleAnnotationParams(Handkerchief::class.java, request, handler, paramValues)

                RoutingUtils.fillPathParam(routeMapping.key, processedRoute, handler, paramValues)

                val rtn = handler.invoke(target, *paramValues)
                request.response().setStatusCode(200).end(rtn.toString())
            } else {
                request.response().setStatusCode(500).end("HTTP/1.1 - 500 INTERNAL SERVER ERROR")
            }

        } catch (e: NoSuchElementException) {
            // TODO: Locate Failure Handler (Not Implemented yet)
            logger.debug("Route NOT found, returning 404 by default")
            request.response().setStatusCode(404).end("HTTP/1.1 - 404 NOT FOUND")
        } catch (e: Throwable) {
            request.response().setStatusCode(500).end(e.message)
            logger.error("Exception during route processing: ${request.path()}", e)
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