package cn.com.guardiantech.aofgo.backend.service.checkin

import cn.com.guardiantech.aofgo.backend.repository.checkin.*
import cn.com.guardiantech.aofgo.backend.service.email.EmailService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Created by dedztbh on 1/6/18.
 * Project AOFGoBackend
 */

@Service
class SignupService @Autowired constructor(
        private val sheetRepository: SignUpSheetRepository,
        private val signupSheetEntryRepository: SignupSheetEntryRepository,
        private val eventRepository: EventRepository,
        private val eventRecordRepository: EventRecordRepository,
        private val groupRepository: EventGroupRepository,
        private val emailService: EmailService
) {
    @Transactional
    fun removeSheet(id: Long): Long =
            sheetRepository.removeById(id)


}