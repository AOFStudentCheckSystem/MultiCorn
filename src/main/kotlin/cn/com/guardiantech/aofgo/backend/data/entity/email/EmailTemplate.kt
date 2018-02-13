package cn.com.guardiantech.aofgo.backend.data.entity.email

import javax.persistence.*

/**
 * Created by dedztbh on 18-2-13.
 * Project AOFGoBackend
 */

@Entity
class EmailTemplate (
        @Id
        @GeneratedValue
        val id: Long = -1,

        @ManyToOne
        @JoinColumn
        val templateType: EmailTemplateType,

        @Column(unique = true)
        val title: String,

        @Lob
        val body: String
)