package cn.com.guardiantech.aofgo.backend.data.entity.authentication

import com.fasterxml.jackson.annotation.JsonIgnore
import org.hibernate.annotations.UpdateTimestamp
import java.util.*
import javax.persistence.*

/**
 * Created by Codetector on 30/11/2017.
 * Project aofgo-backend
 */
@Entity
class Credential (
        @Id
        @GeneratedValue
        val id: Long = -1,

        @Enumerated(EnumType.STRING)
        @Column(name = "credential_type")
        val type: CredentialType,

        @Lob
        @Column(name = "credential_secret", nullable = false)
        var secret: String,


        @ManyToOne
        @JoinColumn(name = "credential_owner")
        var owner: Subject,

        @ElementCollection
        @CollectionTable(name = "credential_secret_history", joinColumns = [(JoinColumn(name = "id"))])
        @Column(name = "secret")
        var historySecrets: Set<String> = hashSetOf(),

        @UpdateTimestamp
        var lastChange: Date = Date()

)