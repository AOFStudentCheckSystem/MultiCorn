package cn.com.guardiantech.aofgo.backend.exception

/**
 * Created by dedztbh on 1/6/18.
 * Project AOFGoBackend
 */
open class ControllerException(message: String?) : RuntimeException(message ?: "Unexpected Error")