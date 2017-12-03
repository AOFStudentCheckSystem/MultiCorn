package cn.com.guardiantech.aofgo.backend.routing

import io.vertx.core.Vertx
import org.junit.Test

import org.junit.Assert.*
import org.mockito.Mockito.*
/**
 * Created by Codetector on 03/12/2017.
 * Project aofgo-backend
 */
open class RootRouterTest {

    @Test
    fun testInitialize() {
        val vertxMock = mock(Vertx::class.java)
        val rootRouter = RootRouter(basePackage = "cn.com.guardiantech.aofgo.backend.routing.testPackage", vertx = vertxMock)
        rootRouter.initialize()
        assertEquals(1, rootRouter.controllerMapping.size)
    }
}