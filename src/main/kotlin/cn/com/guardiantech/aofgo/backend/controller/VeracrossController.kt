package cn.com.guardiantech.aofgo.backend.controller

import cn.com.guardiantech.aofgo.backend.authentication.AuthContext
import cn.com.guardiantech.aofgo.backend.data.entity.veracross.VeracrossCookie
import cn.com.guardiantech.aofgo.backend.service.VitaExtract
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/veracross")
class VeracrossController @Autowired constructor(
        @Autowired
        val vitaExtract: VitaExtract
) {
//    @PutMapping("/")
//    fun initialAuthentication(authContext: AuthContext): VeracrossCookie = try {
//
//    }
}