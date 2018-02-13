package cn.com.guardiantech.aofgo.backend.repository.auth

import cn.com.guardiantech.aofgo.backend.data.entity.Account
import org.springframework.data.repository.PagingAndSortingRepository

interface AccountPageableRepository : PagingAndSortingRepository<Account, Long> {
}