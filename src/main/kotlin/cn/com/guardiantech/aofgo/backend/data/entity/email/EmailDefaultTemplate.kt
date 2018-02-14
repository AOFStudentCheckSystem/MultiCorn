package cn.com.guardiantech.aofgo.backend.data.entity.email

import javax.persistence.*

/**
 * Created by dedztbh on 18-2-13.
 * Project AOFGoBackend
 */
@Entity
class EmailDefaultTemplate (
        @Id
        @Enumerated(EnumType.STRING)
        val templateType: EmailTemplateTypeEnum,

        @OneToOne
        @JoinColumn
        val defaultTemplate: EmailTemplate? = null
)