package cn.com.guardiantech.aofgo.backend.repository.auth

import cn.com.guardiantech.aofgo.backend.data.entity.authentication.Subject
import org.springframework.data.repository.PagingAndSortingRepository

interface SubjectPageableRepository : PagingAndSortingRepository<Subject, Long> {

}