package cn.com.guardiantech.aofgo.backend.data.entity.authentication

import javax.persistence.*

/**
 * Created by Codetector on 30/11/2017.
 * Project aofgo-backend
 */
@Entity
@Table(uniqueConstraints = arrayOf(UniqueConstraint(columnNames = arrayOf("principal_type", "principal_owner"))))
class Principal(
        @Id
        @GeneratedValue
        val id: Long = -1,

        @Enumerated(EnumType.STRING)
        @Column(name = "principal_type")
        val type: PrincipalType,

        @Column(name = "principal_identification", unique = true)
        val identification: String,

        @ManyToOne
        @JoinColumn(name = "principal_owner")
        var owner: Subject
)