package cn.com.guardiantech.aofgo.backend.data.entity.email

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonManagedReference
import javax.persistence.*

/**
 * Created by dedztbh on 18-2-13.
 * Project AOFGoBackend
 */

@Entity
class EmailTemplateType(
        @Id
        @Enumerated(EnumType.STRING)
        val templateType: EmailTemplateTypeEnum,

        @OneToMany(mappedBy = "master", orphanRemoval = true, fetch = FetchType.EAGER)
        @JsonManagedReference
        val variables: MutableSet<EmailTemplateVariable> = mutableSetOf(),

        @OneToOne(cascade = [(CascadeType.DETACH)])
        @JoinColumn
        @JsonIgnore
        var defaultTemplate: EmailTemplate? = null
) {
    fun addVariable(variable: EmailTemplateVariable) {
        variables.add(variable)
    }

    fun getVariableMapping(repeatKeyCheck: Boolean = true): MutableMap<String, EmailTemplateVariableType> {
        return mutableMapOf<String, EmailTemplateVariableType>().let { map ->
            variables.forEach {
                if (map.containsKey(it.name) && repeatKeyCheck) throw IllegalArgumentException("Repeat Key: ${it.name}")
                map[it.name] = it.type
            }
            map
        }
    }
}