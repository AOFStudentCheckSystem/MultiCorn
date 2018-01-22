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
        val subject: Subject? = null,

        @Column(name = "first_name")
        val firstName: String = "unknown",

        @Column(name = "last_name")
        val lastName: String = "unknown",

        @Column(name = "email", unique = true)
        val email: String? = null,

        @Column(name = "phone")
        val phone: String? = null,

        @Enumerated(EnumType.STRING)
        @Column(name = "account_type")
        val type: AccountType = AccountType.OTHER,

        @Column(name = "preferred_name")
        val preferredName: String = firstName
)