package cn.com.guardiantech.aofgo.backend.controller

import org.springframework.web.bind.annotation.RequestMapping
import cn.com.guardiantech.aofgo.backend.annotation.Require
import cn.com.guardiantech.aofgo.backend.data.entity.Student
import cn.com.guardiantech.aofgo.backend.request.student.StudentCreationRequest
import cn.com.guardiantech.aofgo.backend.service.StudentService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMethod

@Controller
@RequestMapping("/student")
class StudentController {
    val studentService = StudentService()

    @Require
    @RequestMapping("/", method = [RequestMethod.POST])
    fun createStudent(@RequestBody studentCreationRequest: StudentCreationRequest): Student {
        return studentService.createStudent(studentCreationRequest)
    }


}