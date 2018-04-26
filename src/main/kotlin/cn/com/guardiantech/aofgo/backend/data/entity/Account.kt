package cn.com.guardiantech.aofgo.backend.data.entity

import cn.com.guardiantech.aofgo.backend.data.entity.authentication.Subject
import javax.persistence.*

@Entity
class Account(
        @Id
        @GeneratedValue
        val id: Long = -1,

        @OneToOne
        @JoinColumn(name = "subject", unique = true)
        var subject: Subject? = null,

        @Column(name = "first_name")
        var firstName: String = "unknown",

        @Column(name = "last_name")
        var lastName: String = "unknown",

        @Column(name = "email", unique = true)
        var email: String? = null,

        @Column(name = "phone")
        var phone: String? = null,

        @Enumerated(EnumType.STRING)
        @Column(name = "account_type")
        var type: AccountType = AccountType.OTHER,

        @Column(name = "preferred_name")
        var preferredName: String = firstName,

        @Enumerated(EnumType.STRING)
        var accountStatus: AccountStatus = AccountStatus.INACTIVE
)