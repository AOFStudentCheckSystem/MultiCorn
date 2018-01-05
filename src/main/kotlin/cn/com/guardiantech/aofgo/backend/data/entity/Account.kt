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
        val subject: Subject,

        @Column(name = "first_name")
        val firstName: String,

        @Column(name = "last_name")
        val lastName: String,

        @Column(name = "email")
        val email: String,

        @Column(name = "phone")
        val phone: String,

        @Enumerated(EnumType.STRING)
        @Column(name = "type")
        val type: AccountType,

        @Column(name = "preferred_name")
        val preferredName: String
)