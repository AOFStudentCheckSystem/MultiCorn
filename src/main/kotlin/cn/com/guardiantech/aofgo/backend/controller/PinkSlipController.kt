package cn.com.guardiantech.aofgo.backend.controller

import cn.com.guardiantech.aofgo.backend.data.entity.slip.CampusLeaveRequest
import cn.com.guardiantech.aofgo.backend.request.slip.LocalLeaveRequestRequest
import cn.com.guardiantech.aofgo.backend.service.PinkSlipService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

/**
 * Created by dedztbh on 18-1-24.
 * Project AOFGoBackend
 */

@RestController
@RequestMapping("/slip")
class PinkSlipController @Autowired constructor(
        val pinkSlipService: PinkSlipService
) {
    @GetMapping("/{id}")
    fun getPinkSlip (@PathVariable id: Long): CampusLeaveRequest {
        return pinkSlipService.getPinkSlip(id)
    }

    @PutMapping("/")
    fun addLocalLeaveRequest (@RequestBody pinkSlipRequest: LocalLeaveRequestRequest) {

    }
}