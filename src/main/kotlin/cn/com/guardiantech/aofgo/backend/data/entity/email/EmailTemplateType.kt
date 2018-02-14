package cn.com.guardiantech.aofgo.backend.data.entity.email

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
        val titleVariables: MutableSet<EmailTemplateVariable> = mutableSetOf(),

        @OneToMany(mappedBy = "master", orphanRemoval = true, fetch = FetchType.EAGER)
        val bodyVariables: MutableSet<EmailTemplateVariable> = mutableSetOf()
) {
    fun addBodyVariable(variable: EmailTemplateVariable) {
        bodyVariables.add(variable)
        variable.master = this
    }

    fun addTitleVariable(variable: EmailTemplateVariable) {
        titleVariables.add(variable)
        variable.master = this
    }

    fun getBodyVariableMapping(repeatKeyCheck: Boolean = true): MutableMap<String, EmailTemplateVariableType> {
        return mutableMapOf<String, EmailTemplateVariableType>().let { map ->
            bodyVariables.forEach {
                if (map.containsKey(it.name) && repeatKeyCheck) throw IllegalArgumentException("Repeat Key: ${it.name}")
                map[it.name] = it.type
            }
            map
        }
    }

    fun getTitleVariableMapping(repeatKeyCheck: Boolean = true): MutableMap<String, EmailTemplateVariableType> {
        return mutableMapOf<String, EmailTemplateVariableType>().let { map ->
            titleVariables.forEach {
                if (map.containsKey(it.name) && repeatKeyCheck) throw IllegalArgumentException("Repeat Key: ${it.name}")
                map[it.name] = it.type
            }
            map
        }
    }
}