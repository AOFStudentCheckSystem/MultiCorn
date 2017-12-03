package cn.com.guardiantech.aofgo.backend.annotation

/**
 * Created by Codetector on 02/12/2017.
 * Project aofgo-backend
 */
@Target(allowedTargets = [AnnotationTarget.CLASS])
annotation class Controller(val basePath:String = "")