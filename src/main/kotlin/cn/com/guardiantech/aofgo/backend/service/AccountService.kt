package cn.com.guardiantech.aofgo.backend.service

import cn.com.guardiantech.aofgo.backend.data.entity.Account
import cn.com.guardiantech.aofgo.backend.data.entity.authentication.Subject
import cn.com.guardiantech.aofgo.backend.repository.auth.AccountRepository
import cn.com.guardiantech.aofgo.backend.repository.auth.SubjectRepository
import cn.com.guardiantech.aofgo.backend.request.account.AccountRequest
import cn.com.guardiantech.aofgo.backend.service.auth.AuthenticationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AccountService @Autowired constructor(
        private val accountRepo: AccountRepository,
        private val authService: AuthenticationService,
        private val subjectRepo: SubjectRepository
) {
    /**
     * @throws NoSuchElementException Account Not Found
     */
    fun getAccountById(id: Long): Account {
        return accountRepo.findById(id).get()
    }

    /**
     * Cannot save account
     * @throws NoSuchElementException Subject Not Found
     */
    @Transactional
    fun createAccount(accountRequest: AccountRequest): Account {
        val subject: Subject? = when {
            accountRequest.subject !== null ->
                authService.registerSubject(accountRequest.subject)
            accountRequest.subjectId !== null ->
                subjectRepo.findById(accountRequest.subjectId).get()
            else -> null
        }

        return accountRepo.save(
                Account(
                        firstName = accountRequest.firstName,
                        lastName = accountRequest.lastName,
                        email = accountRequest.email,
                        phone = accountRequest.phone,
                        type = accountRequest.type!!,
                        preferredName = accountRequest.preferredName,
                        subject = subject
                )
        )
    }
}