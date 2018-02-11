package cn.com.guardiantech.aofgo.backend.data.entity.file

import org.hibernate.annotations.CreationTimestamp
import java.util.*
import javax.persistence.*

/**
 * Created by Codetector on 2018/02/10.
 * Project AOFGoBackend
 */
@Entity
class StoredFile (
        @GeneratedValue
        @Id
        val fileId: Long = -1,

        @Column(unique = true)
        val hash: String,

        @OneToMany
        val instances: MutableSet<VirtualFile> = hashSetOf(),

        @CreationTimestamp
        val createdDate: Date = Date()
)