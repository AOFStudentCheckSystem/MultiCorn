package cn.com.guardiantech.aofgo.backend.exception

/**
 * Created by Codetector on 04/12/2017.
 * Project aofgo-backend
 */
class InvalidRouteException(routeName: String, cause: String): RuntimeException("Failed to parse route: $routeName, cause: $cause")