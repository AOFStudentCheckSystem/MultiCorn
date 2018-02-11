package cn.com.guardiantech.aofgo.backend.data.entity.file

import javax.persistence.*

/**
 * Created by Codetector on 2018/02/10.
 * Project AOFGoBackend
 */
@Entity
class VirtualFile (
        @GeneratedValue
        @Id
        val id: Long = -1,

        var fileName: String,
        var fileExtension: String,
        var removed: Boolean = false,
        @ManyToOne
        @JoinColumn
        val storedFile: StoredFile
)