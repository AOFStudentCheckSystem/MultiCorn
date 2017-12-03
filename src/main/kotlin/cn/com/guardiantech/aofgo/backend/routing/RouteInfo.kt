package cn.com.guardiantech.aofgo.backend.routing

import io.vertx.core.http.HttpMethod
import java.util.*

/**
 * Created by Codetector on 02/12/2017.
 * Project aofgo-backend
 */
data class RouteInfo(val route: String, val method: Array<HttpMethod>) {
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
}