package cn.com.guardiantech.aofgo.backend.controller

import cn.com.guardiantech.aofgo.backend.service.PinkSlipService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Required
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Created by dedztbh on 18-1-24.
 * Project AOFGoBackend
 */

@RestController
@RequestMapping("/slip")
class PinkSlipController @Autowired constructor(
        val pinkSlipService: PinkSlipService
) {

}