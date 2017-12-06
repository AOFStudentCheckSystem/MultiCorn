package cn.com.guardiantech.aofgo.backend.annotation

/**
 * Created by Codetector on 2017/12/06.
 * Project aofgo-backend
 */
/**
 * Annotate on handler parameter to obtain the raw ByteArray of RequestBody
 */
@Target(allowedTargets = [AnnotationTarget.VALUE_PARAMETER])
annotation class RawRequestBody