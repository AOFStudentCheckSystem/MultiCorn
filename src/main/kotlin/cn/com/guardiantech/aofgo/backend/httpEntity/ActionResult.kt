package cn.com.guardiantech.aofgo.backend.httpEntity

/**
 * Created by calvinx on 2018/01/05.
 * Project AOFGoBackend
 */
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

class ActionResult(val success: Boolean) {
    var t: Throwable? = null
    var errorStatus = HttpStatus.INTERNAL_SERVER_ERROR

    constructor(result: Boolean, errorCode: HttpStatus) : this(result) {
        this.errorStatus = errorCode
    }

    constructor(t: Throwable) : this(false) {
        this.t = t
    }

    override fun toString(): String {

        val returnObject: ReturnObject

        if (!success && t != null) {
            returnObject = ReturnObject("error", (t as Throwable).message)
        } else {
            returnObject = ReturnObject("success", "success")
        }

        return returnObject.status + returnObject.message
    }

    fun encode(): ResponseEntity<String> {
        return ResponseEntity(this.toString(), if (success) HttpStatus.OK else errorStatus)
    }
}