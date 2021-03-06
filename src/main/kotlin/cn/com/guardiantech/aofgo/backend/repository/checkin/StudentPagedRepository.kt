package cn.com.guardiantech.aofgo.backend.repository.checkin

import cn.com.guardiantech.aofgo.backend.data.entity.Student
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.repository.query.Param
import java.util.*

/**
 * Created by Codetector on 2017/4/6.
 * Project backend
 */

interface StudentPagedRepository : PagingAndSortingRepository<Student, Long> {
    fun findByIdNumberIgnoreCase(idNumber: String): Optional<Student>

    fun findByCardSecretIgnoreCase(card: String): Optional<Student>

    @Query("SELECT s FROM Student s WHERE ((s.account IS NOT NULL) AND (s.account.email = ?1))")
    fun findByEmailIgnoreCase(email: String): Optional<Student>

    @Query("SELECT s FROM Student s WHERE ((s.account IS NOT NULL) AND (s.account.email IS NOT NULL))")
    fun findByEmailIsNotNull(): List<Student>

    override fun findAll(p0: Pageable): Page<Student>
}