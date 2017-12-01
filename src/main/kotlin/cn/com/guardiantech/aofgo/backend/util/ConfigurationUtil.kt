package cn.com.guardiantech.aofgo.backend.util

/**
 * Created by Codetector on 01/12/2017.
 * Project aofgo-backend
 */
object ConfigurationUtil {
    fun getSystemProperty(name: String, default: String = ""): String {
        return System.getProperty(name)?:default
    }
}