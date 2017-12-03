package cn.com.guardiantech.aofgo.backend.routing

import com.nhaarman.mockito_kotlin.mock
import io.vertx.core.Vertx
import io.vertx.core.impl.VertxImpl
import org.junit.Test

import org.junit.Assert.*
import org.junit.runner.RunWith
import org.mockito.Mockito

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
}