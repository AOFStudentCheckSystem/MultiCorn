package cn.com.guardiantech.aofgo.backend.data.entity.emailvalidation

import cn.com.guardiantech.aofgo.backend.data.entity.Account
import org.hibernate.annotations.CreationTimestamp
import java.util.*
import javax.persistence.*

@Entity
@Table(uniqueConstraints = [(UniqueConstraint(columnNames = ["validation_code", "owner"])), (UniqueConstraint(columnNames = ["validation_code"]))])
class EmailValidationCode (
        @GeneratedValue
        @Id
        val id: Long = 0,

        @Column(name = "validation_code")
        val code: String,

        @OneToOne
        @JoinColumn(name = "owner")
        val owner: Account,

        var additionalInformation: String? = null,

        @CreationTimestamp
        val created: Date = Date()
)