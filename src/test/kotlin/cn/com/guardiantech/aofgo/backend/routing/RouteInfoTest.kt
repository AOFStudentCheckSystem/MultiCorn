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

        val test4 = "/"
        assertEquals("/", RouteInfo.generatePatternForURL(test4).toString())
    }

    @Test
    fun testMatchRoute() {
        // Simple Normal URL Test
        val test1 = RouteInfo("/this/is/cool")
        assertFalse("This should not match",  test1.matchRoute("/of/course"))
        assertFalse("This should not match",  test1.matchRoute("/of/course/this"))
        assertFalse("This should not match",  test1.matchRoute("/this"))
        assertFalse("This should not match",  test1.matchRoute("/this/should/not"))
        assertTrue("This should match",  test1.matchRoute("/this/is/cool"))

        // URL With Path Variable in the middle
        val test2 = RouteInfo("/this/:is/cool")
        assertFalse("This should not match",  test2.matchRoute("/of/course"))
        assertFalse("This should not match",  test2.matchRoute("/of/course/this"))
        assertFalse("This should not match",  test2.matchRoute("/this"))
        assertFalse("This should not match",  test2.matchRoute("/this/should/not"))
        assertFalse("This should not match",  test2.matchRoute("/this//cool"))
        assertTrue("This should match",  test2.matchRoute("/this/is/cool"))
        assertTrue("This should match",  test2.matchRoute("/this/very/cool"))
        assertTrue("This should match",  test2.matchRoute("/this/:fucks/cool"))

        //URL With PathVariable in the ened
        val test3 = RouteInfo("/this/is/:cool")
        assertFalse("This should not match",  test3.matchRoute("/of/course"))
        assertFalse("This should not match",  test3.matchRoute("/of/course/this"))
        assertFalse("This should not match",  test3.matchRoute("/this"))
        assertFalse("This should not match",  test3.matchRoute("/this/should/not"))
        assertFalse("This should not match",  test3.matchRoute("/this/is/"))
        assertFalse("This should not match",  test3.matchRoute("/this/is"))
        assertTrue("This should match",  test3.matchRoute("/this/is/cool"))
        assertTrue("This should match",  test3.matchRoute("/this/is/asdfasd"))
        assertTrue("This should match",  test3.matchRoute("/this/is/:cool"))
    }
}