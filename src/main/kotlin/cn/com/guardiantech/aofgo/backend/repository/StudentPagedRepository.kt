package cn.com.guardiantech.aofgo.backend.repository

import cn.com.guardiantech.aofgo.backend.data.entity.Student
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.PagingAndSortingRepository
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

    @Query("SELECT s FROM Student s WHERE ((s.account IS NOT NULL) AND (LOWER(s.account.firstName) like LOWER(?1%)))")
    fun searchByFirstName(firstName: String): Page<Student>

    @Query("SELECT s FROM Student s WHERE ((s.account IS NOT NULL) AND (s.account.idNumber like ?1%))")
    fun searchByIdNumber(idNumber: String): Page<Student>

    @Query("SELECT s FROM Student s WHERE ((s.account IS NOT NULL) AND (LOWER(s.account.lastName) like LOWER(?1%)))")
    fun searchByLastName(lastName: String): Page<Student>

    @Query("SELECT s FROM Student s WHERE ((s.account IS NOT NULL) AND (LOWER(s.account.preferredName) like LOWER(?1%)))")
    fun searchByPreferredName(preferredName: String): Page<Student>

    @Query("SELECT s FROM Student s WHERE ((s.account IS NOT NULL) AND ((LOWER(s.account.firstName) LIKE LOWER(?1%)) OR " +
            "(LOWER(s.account.lastName) LIKE LOWER(?1%)) OR (LOWER(s.account.preferredName) LIKE LOWER(?1%)) OR (LOWER(s.idNumber) LIKE LOWER(?1%)) OR )")
    fun fuzzySearch(searchKey: String): Page<Student>
}