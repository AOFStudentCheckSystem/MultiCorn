package cn.com.guardiantech.aofgo.backend.controller

import cn.com.guardiantech.aofgo.backend.annotation.Require
import cn.com.guardiantech.aofgo.backend.authentication.AuthContext
import cn.com.guardiantech.aofgo.backend.exception.CannotLoginException
import cn.com.guardiantech.aofgo.backend.exception.EntityNotFoundException
import cn.com.guardiantech.aofgo.backend.request.authentication.VeracrossInitialAuthRequest
import cn.com.guardiantech.aofgo.backend.service.VeracrossCaptureService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/veracross")
class VeracrossController @Autowired constructor(
        @Autowired
        val veracrossCapture: VeracrossCaptureService
) {
    @PutMapping("/")
    @Require()
    fun vInitialAuth(authContext: AuthContext, @RequestBody vInitialAuthRequest: VeracrossInitialAuthRequest) = try {
        val subjectID: Long = authContext.session!!.subject.id
        val authCookie = veracrossCapture.authenticate(vInitialAuthRequest.username, vInitialAuthRequest.password, subjectID)
    } catch (e: NoSuchElementException) {
        throw EntityNotFoundException("Student Not Found")
    } catch (e: Throwable) {
        e.printStackTrace()
        throw CannotLoginException("Cannot login for some reason. Blame Veracross.")
    }
}