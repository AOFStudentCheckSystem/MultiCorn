package cn.com.guardiantech.aofgo.backend.annotation

/**
 * Created by Codetector on 2017/12/06.
 * Project aofgo-backend
 */
@Target(allowedTargets = [AnnotationTarget.VALUE_PARAMETER])
annotation class PathVariable(val name: String = "")