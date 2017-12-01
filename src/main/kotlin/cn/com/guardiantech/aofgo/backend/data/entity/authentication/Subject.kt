package cn.com.guardiantech.aofgo.backend.data.entity.authentication

import cn.com.guardiantech.aofgo.backend.annotation.NoArg
import javax.persistence.*

/**
 * Created by Codetector on 29/11/2017.
 * Project aofgo-backend
 */
@NoArg
@Entity
class Subject (
    @Id
    @GeneratedValue
    val id: Long = -1,

    @OneToMany(mappedBy = "owner", orphanRemoval = true)
    val principals: MutableSet<Principal> = hashSetOf(),

    @OneToMany(mappedBy = "owner", orphanRemoval = true)
    val credentials: MutableSet<Principal> = hashSetOf(),

    @Lob
    @Column(nullable = true)
    val subjectAttachedInfo: String? = null
)