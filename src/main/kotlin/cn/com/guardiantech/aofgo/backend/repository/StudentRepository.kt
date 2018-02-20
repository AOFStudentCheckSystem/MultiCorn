package cn.com.guardiantech.aofgo.backend.repository

import cn.com.guardiantech.aofgo.backend.data.entity.Student
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import java.util.*

interface StudentRepository : CrudRepository<Student, Long> {
    fun findById(id: Long): Optional<Student>
    fun findByIdNumber(idNumber: String): Optional<Student>
    @Query("select s from #{#entityName} s WHERE s.account.subject.id = ?1")
    fun findStudentBySubjectId(subjectId: Long): Optional<Student>
}