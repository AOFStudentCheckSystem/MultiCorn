package cn.com.guardiantech.aofgo.backend.data.entity.email

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.*

/**
 * Created by dedztbh on 18-2-13.
 * Project AOFGoBackend
 */
@Entity
class EmailTemplateVariable(
        @Id
        @GeneratedValue
        @JsonIgnore
        val id: Long = -1,

        @Column
        val name: String,

        @Column
        @Enumerated(EnumType.STRING)
        val type: EmailTemplateVariableType
) {
    @ManyToOne
    @JoinColumn
    @JsonBackReference
    lateinit var master: EmailTemplateType
}