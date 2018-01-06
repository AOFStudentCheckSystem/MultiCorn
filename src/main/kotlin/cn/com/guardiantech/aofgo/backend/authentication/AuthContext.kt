package cn.com.guardiantech.aofgo.backend.authentication

import cn.com.guardiantech.aofgo.backend.data.entity.authentication.Session

/**
 * Created by dummy on 4/28/17.
 */
class AuthContext(private val internalSession: Session? = null) {

    fun isAuthenticated():Boolean {
        return (this.internalSession?.isAuthenticated()?:false) && (this.internalSession?.isAuthorized()?:false)
    }

    companion object {
        private var threadLocalContext = ThreadLocal<AuthContext>()

        internal var currentContextInternal: AuthContext?
            get() = threadLocalContext.get()
            set(value) = threadLocalContext.set(value)

        val currentContext: AuthContext
            get() = threadLocalContext.get() ?: AuthContext()

        internal fun clear() {
            threadLocalContext.remove()
        }
    }
}
