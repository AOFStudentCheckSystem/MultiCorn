package cn.com.guardiantech.aofgo.backend.data.entity

import javax.persistence.*

@Entity
class Guardian(
        @Id
        @GeneratedValue
        val id: Long = -1,

        @OneToOne
        @JoinColumn
        val guardianAccount: Account,

        @Enumerated(EnumType.STRING)
        @Column
        val relation: GuardianType
)