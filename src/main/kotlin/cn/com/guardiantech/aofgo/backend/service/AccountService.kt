package cn.com.guardiantech.aofgo.backend.service

import cn.com.guardiantech.aofgo.backend.data.entity.Account
import cn.com.guardiantech.aofgo.backend.repository.auth.AccountRepository
import cn.com.guardiantech.aofgo.backend.request.account.AccountCreationRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class AccountService @Autowired constructor(
        private val accountRepo: AccountRepository
) {
    /**
     * @throws NoSuchElementException Account Not Found
     */
    fun getAccountById(id: Long): Account {
        return accountRepo.findById(id).get()
    }

    /**
     * Cannot save account
     */
    fun createAccount(request: AccountCreationRequest): Account {
        return accountRepo.save(
                Account(
                        firstName = request.firstName,
                        lastName = request.lastName,
                        email = request.email,
                        phone = request.phone,
                        type = request.type!!,
                        preferredName = request.preferredName
                )
        )
    }
}