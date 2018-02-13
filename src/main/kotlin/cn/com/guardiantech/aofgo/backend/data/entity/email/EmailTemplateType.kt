package cn.com.guardiantech.aofgo.backend.data.entity.email

import javax.persistence.*

/**
 * Created by dedztbh on 18-2-13.
 * Project AOFGoBackend
 */

@Entity
class EmailTemplateType(
        @Id
        val templateType: String,

        @OneToMany(mappedBy = "master", orphanRemoval = true, fetch = FetchType.EAGER)
        private val variables: MutableSet<EmailTemplateVariable>
) {
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