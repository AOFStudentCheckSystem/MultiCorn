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

        @Column(unique = true, name = "template_name")
        val name: String,

        @ManyToOne
        @JoinColumn
        val templateType: EmailTemplateType,

        val title: String,

        @Lob
        val body: String
)