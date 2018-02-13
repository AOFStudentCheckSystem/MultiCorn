package cn.com.guardiantech.aofgo.backend.service

import cn.com.guardiantech.aofgo.backend.repository.slip.CampusLeaveRequestRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * Created by dedztbh on 18-1-24.
 * Project AOFGoBackend
 */

@Service
class PinkSlipService @Autowired constructor(
        val localLeaveRequestRepo: CampusLeaveRequestRepository
) {
}