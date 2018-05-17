package cn.com.guardiantech.aofgo.backend.service

import cn.com.guardiantech.aofgo.backend.data.entity.GuardianType
import cn.com.guardiantech.aofgo.backend.data.entity.Student
import cn.com.guardiantech.aofgo.backend.data.entity.email.EmailTemplateTypeEnum
import cn.com.guardiantech.aofgo.backend.data.entity.slip.*
import cn.com.guardiantech.aofgo.backend.exception.BadRequestException
import cn.com.guardiantech.aofgo.backend.exception.EntityNotFoundException
import cn.com.guardiantech.aofgo.backend.jsonview.SlipView
import cn.com.guardiantech.aofgo.backend.repository.slip.CampusLeaveRequestRepository
import cn.com.guardiantech.aofgo.backend.repository.slip.PermissionRequestRepository
import cn.com.guardiantech.aofgo.backend.repository.slip.PermissionRequestTokenRepository
import cn.com.guardiantech.aofgo.backend.service.email.EmailService
import cn.com.guardiantech.aofgo.backend.service.email.EmailTemplatingService
import cn.com.guardiantech.aofgo.backend.util.keyGenerator.Base32RandomString
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.commons.lang3.StringUtils
import org.json.JSONObject
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.security.SecureRandom
import java.util.*
import kotlin.collections.HashMap

/**
 * Created by dedztbh on 18-1-24.
 * Project AOFGoBackend
 */

@Service
class PinkSlipService @Autowired constructor(
        val leaveRequestRepository: CampusLeaveRequestRepository,
        val permissionRequestRepository: PermissionRequestRepository,
        val emailService: EmailService,
        val emailTemplatingService: EmailTemplatingService,
        val permissionRequestTokenRepository: PermissionRequestTokenRepository
) {
    fun getPinkSlip(id: Long) = leaveRequestRepository.findById(id).get()

    fun getLocalLeaveRequestsBySubjectIdPaged(subjectId: Long, pageable: Pageable) =
            leaveRequestRepository.findBySubjectIdPaged(subjectId, pageable)

    fun getLeaveRequestByStatus(status: LeaveStatus) =
            leaveRequestRepository.findByStatus(status)

    @Transactional
    fun addLocalLeaveRequest(
            student: Student,
            type: LeaveType,
            visitPerson: String?,
            description: String,
            statusMessage: String,
            transportationMethod: TransportationMethod,
            transportationNote: String,
            dateOfLeave: Date,
            dateOfReturn: Date,
            missClass: Boolean,
            missSport: Boolean,
            missJob: Boolean,
            contactName: String,
            contactPhone: String,
            contactAddress: String
    ): CampusLeaveRequest {
        val unsavedPermissionRequests = student.guardians.filter {
            when (it.relation) {
                GuardianType.ACADEMIC_DEAN -> missClass
                GuardianType.COACH -> missSport
                GuardianType.FACULTY_SUPERVISOR -> missJob
                else -> true
            }
        }.map {
            PermissionRequest(
                    acceptor = it
            )
        }

        val appointedStatus =
                if (unsavedPermissionRequests.isEmpty())
                    LeaveStatus.WAITINGAPPROVAL
                else
                    LeaveStatus.PENDING

        return leaveRequestRepository.save(
                CampusLeaveRequest(
                        student = student,
                        type = type,
                        visitPerson = visitPerson,
                        description = description,
                        statusMessage = statusMessage,
                        transportationMethod = transportationMethod,
                        transportationNote = transportationNote,
                        permissionRequests = permissionRequestRepository.save(
                                unsavedPermissionRequests
                        ).toMutableSet(),
                        dateOfLeave = dateOfLeave,
                        dateOfReturn = dateOfReturn,
                        missClass = missClass,
                        missSport = missSport,
                        missJob = missJob,
                        contactName = contactName,
                        contactPhone = contactPhone,
                        contactAddress = contactAddress,
                        status = appointedStatus
                )
        ).let { campusLeaveRequest ->
            campusLeaveRequest.permissionRequests.forEach {
                it.campusLeaveRequest = campusLeaveRequest
            }
            permissionRequestRepository.save(campusLeaveRequest.permissionRequests)
            campusLeaveRequest
        }
    }

    fun getLeaveRequestOwnedBySubject(leaveRequestId: Long, subjectId: Long) =
            leaveRequestRepository.findByIdAndSubjectId(leaveRequestId, subjectId)


    @Transactional
    fun studentSendPermissionRequests(request: CampusLeaveRequest) {
        sendPermissionRequestEmails(
                request.permissionRequests.filter {
                    it.accepted != true
                }
                , request.getStudentName())
//        request.evalWaitingStatus()
//        return leaveRequestRepository.save(request)

    }

    private val pinkSlipJsonMapper = ObjectMapper().disable(MapperFeature.DEFAULT_VIEW_INCLUSION)

    private fun sendPermissionRequestEmails(permissionRequests: List<PermissionRequest>, studentName: String) {

        if (permissionRequests.isEmpty()) return

        emailService.defaultTemplate[EmailTemplateTypeEnum.PINKSLIP]!!.let { emailTemplate ->
            val pinkslip = permissionRequests[0].campusLeaveRequest
            val jsonPinkSlip = JSONObject(pinkSlipJsonMapper
                    .writerWithView(SlipView.FacultyView::class.java)
                    .writeValueAsString(pinkslip))

//            val compiledPinkSlip = StringBuilder()
//            jsonPinkSlip.keys().forEach {
//                if (it !== "id" && it !== "status") {
//                    compiledPinkSlip.append("${processKey(it)}: ")
//                    val value = jsonPinkSlip.get(it)
//                    val appendValue =
//                            if (value !== null) {
//                                if (it !== "type" && it !== "transportationMethod") {
//                                    if (it !== "dateOfLeave" && it !== "dateOfReturn") {
//                                        when (value) {
//                                            is Boolean -> if (value) "Yes" else "No"
//                                            else -> value.toString()
//                                        }
//                                    } else {
//                                        value as Long
//                                        // Process time
//                                        DateFormat.getDateInstance(DateFormat.DEFAULT)
//                                                .format(Date(value * 1000))
//                                    }
//                                } else {
//                                    //Enum
//                                    value.toString().replace("_", " ").toLowerCase().capitalize()
//                                }
//                            } else {
//                                "None"
//                            }
//                    compiledPinkSlip.append("$appendValue</br>")
//                }
//            }
//
//            val compiledPinkSlipStr = compiledPinkSlip.toString()
            val compiledPinkSlipStr = "<br/>"

            permissionRequests.forEach {
                val guardianEmail = it.acceptor.guardianAccount.email
                        ?: throw BadRequestException("Guardian ${it.acceptor.guardianAccount.lastName} has no email")

                val compiledEmail = emailTemplatingService.compileTemplate(
                        emailTemplate,
                        HashMap<String, Any>().apply {
                            put("studentName", studentName)
                            put("permissionRequestToken", assignPermissionRequestToken(it).token)
                            put("requestBody", compiledPinkSlipStr)
                        }
                )

                Thread(Runnable {
                    emailService.sendEmail(compiledEmail.first, compiledEmail.second, guardianEmail)
                }).start()
            }
        }
    }

    private fun processKey(key: String): String {
        return StringUtils.join(
                StringUtils.splitByCharacterTypeCamelCase(key),
                " "
        ).capitalize()
    }

    fun getPermissionRequestWithCorrectSubject(permissionRequestId: Long, subjectId: Long) =
            permissionRequestRepository.findByIdAndSubjectId(permissionRequestId, subjectId)

    @Transactional
    fun setPermissionRequestAccepted(permissionRequest: PermissionRequest, accepted: Boolean, ip: String): PermissionRequest {
        permissionRequest.accepted = accepted
        permissionRequest.acceptTime = Date()
        permissionRequest.acceptorIp = ip
        return permissionRequestRepository.save(permissionRequest)
    }

    fun findLeaveRequestsByStatus(status: LeaveStatus) =
            leaveRequestRepository.findByStatus(status)

    @Transactional
    fun setLeaveRequestStatus(leaveRequestId: Long, leaveStatus: LeaveStatus): CampusLeaveRequest {
        val foundRequest = leaveRequestRepository.findById(leaveRequestId).get()
        foundRequest.setStatus(leaveStatus)
        return leaveRequestRepository.save(foundRequest)
    }


    private val randomGen = Base32RandomString(20, SecureRandom())

    @Transactional
    fun assignPermissionRequestToken(permissionRequest: PermissionRequest): PermissionRequestToken {
        var existingToken: PermissionRequestToken? = null
        permissionRequestTokenRepository.findByPermissionRequest(permissionRequest).ifPresent {
            it.createdTime = Date()
            existingToken = permissionRequestTokenRepository.save(it)
        }
        if (existingToken !== null) return existingToken as PermissionRequestToken

        var token: String
        do {
            token = randomGen.nextString()
        } while (permissionRequestTokenRepository.findByToken(token).isPresent)

        return permissionRequestTokenRepository.save(
                PermissionRequestToken(token = token, permissionRequest = permissionRequest)
        )
    }

    fun getPermissionRequestByToken(token: String): PermissionRequest {
        return getPermissionRequestToken(token).permissionRequest
    }

    fun getPermissionRequestToken(token: String): PermissionRequestToken {
        return (permissionRequestTokenRepository.findByToken(token).orElseGet { null }
                ?: throw EntityNotFoundException("Token does not exists"))
    }

    fun invalidatePermissionRequestTokenByPermissionRequest(permissionRequest: PermissionRequest) {
        permissionRequestTokenRepository.findByPermissionRequest(permissionRequest).ifPresent {
            permissionRequestTokenRepository.delete(it)
        }
    }
}