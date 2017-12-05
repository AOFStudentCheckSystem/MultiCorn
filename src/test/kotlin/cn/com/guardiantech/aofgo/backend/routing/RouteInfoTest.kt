package cn.com.guardiantech.aofgo.backend.routing

import cn.com.guardiantech.aofgo.backend.exception.InvalidRouteException
import org.junit.Test

import org.junit.Assert.*

/**
 * Created by Codetector on 04/12/2017.
 * Project aofgo-backend
 */
class RouteInfoTest {

    @Test
    fun testRouteRegularExpressionGeneration () {

        // Test Basic backslash replacement ( '/' -> '/' && '//' -> '/')
        val test1 = "/this//is/such//////a/hell"
        val test1Result = RouteInfo.generatePatternForURL(test1).toString()
        assertEquals("Error replacing duplicate slashes","/this/is/such/a/hell", test1Result)

        // Test Trailing slash removal
        val test2 = "/this/hello/world/is/cool/"
        val test2Result = RouteInfo.generatePatternForURL(test2).toString()
        assertEquals("Error removing trailing slash", "/this/hello/world/is/cool", test2Result)

        // Test PathVariable RegEx Generation
        val test3 = "/this/:is/very/:cool"
        val test3Result = RouteInfo.generatePatternForURL(test3).toString()
        assertEquals("Failed to replace pathVariables", "/this/(?<is>[^/]+)/very/(?<cool>[^/]+)", test3Result)

        // Test PathVariable Duplicate Detection
        try {
            val test3_1 = "/this/:is/:is/an/invalid/route"
            RouteInfo.generatePatternForURL(test3_1)
            fail("Failed to detect duplicate variable")
        } catch (e: InvalidRouteException){
            // Expected
        }
    }
}