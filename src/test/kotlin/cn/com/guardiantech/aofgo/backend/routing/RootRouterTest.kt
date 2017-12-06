package cn.com.guardiantech.aofgo.backend.routing

import cn.com.guardiantech.aofgo.backend.annotation.PathVariable
import cn.com.guardiantech.aofgo.backend.util.RoutingUtils
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.vertx.core.impl.VertxImpl
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito
import java.lang.reflect.Method
import java.lang.reflect.Parameter

/**
 * Created by Codetector on 03/12/2017.
 * Project aofgo-backend
 */
open class RootRouterTest {

    @Test
    fun testInitialize() {
        val vertxMock = Mockito.mock(VertxImpl::class.java)
        val rootRouter = RootRouter(basePackage = "cn.com.guardiantech.aofgo.backend.routing.testPackage", vertx = vertxMock)
        rootRouter.initialize()
        assertEquals(1, rootRouter.controllerMapping.size)
        assertEquals(1, rootRouter.routeMapping.size)
        assertEquals("/test/subPath", rootRouter.routeMapping.keys.first().route)
    }

    @Test
    fun testMethodPathVariableInjection() {
        val routeInfo = RouteInfo("/test/:testVariable")
        val mockMethod = mock<Method>()
        val mockParam = mock<Parameter>()
        val mockAnnotation = mock<PathVariable>()

        whenever(mockMethod.parameters).thenReturn(arrayOf(mockParam))

        whenever(mockParam.isAnnotationPresent(PathVariable::class.java)).thenReturn(true)
        whenever(mockParam.getDeclaredAnnotation(PathVariable::class.java)).thenReturn(mockAnnotation)

        whenever(mockAnnotation.name).thenReturn("")
        whenever(mockParam.name).thenReturn("testvariable")
        whenever(mockParam.type).thenReturn(String::class.java)

        val testArray = Array<Any?>(1, { null })

        RoutingUtils.fillPathParam(routeInfo, "/test/hello", mockMethod, testArray)

        assertEquals(testArray[0], "hello")
    }
}