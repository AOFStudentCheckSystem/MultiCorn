package cn.com.guardiantech.aofgo.backend.routing

import cn.com.guardiantech.aofgo.backend.exception.InvalidRouteException
import io.vertx.core.http.HttpMethod
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * Created by Codetector on 02/12/2017.
 * Project aofgo-backend
 */
class RouteInfo(val route: String, val method: Array<HttpMethod>) {
    internal var compiledPattern: Pattern

    init {
        // Compile Regular Expression
        try {
            this.compiledPattern = generatePatternForURL(route)
        } catch (throwable: Throwable) {
            throw InvalidRouteException(this.route, "Failed to parse route, ${throwable.message}")
        }
    }

    internal fun getMatcher(string: String): Matcher = compiledPattern.matcher(string)
//
//    internal fun matchRoute(route: String): Boolean {
//        val matcher = getMatcher(route)
////        if (compiledPattern.)
//    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RouteInfo

        if (route != other.route) return false
        if (!Arrays.equals(method, other.method)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = route.hashCode()
        result = 31 * result + Arrays.hashCode(method)
        return result
    }

    override fun toString(): String {
        return "RouteInfo{route: '$route', method: ${Arrays.toString(method)}}"
    }

    companion object {
        internal fun generatePatternForURL(rawUrl: String): Pattern {
            var sb: String = rawUrl.toLowerCase()
            sb = sb.replace("/+".toRegex(), "/") // Replace redundant backslash
            sb = sb.replace("/\\z".toRegex(), "")

            // (?<variable1>[^/]*)
            val variableList = hashSetOf<String>()
            val matcher = "/:(?<varName>[^/]*)".toRegex().toPattern().matcher(sb)
            while (matcher.find()) {
                if (matcher.groupCount() == 1) {
                    val varName = matcher.group("varName")
                    if (!variableList.contains(varName)) {
                        variableList.add(matcher.group("varName"))
                    } else {
                        throw InvalidRouteException(rawUrl, "Duplicate pathVariable: $varName")
                    }
                }
            }
            variableList.forEach {
                sb = sb.replace(":$it", "(?<$it>[^/]+)", true)
            }
            return Pattern.compile(sb)
        }
    }
}