package cn.com.guardiantech.aofgo.backend.request.authentication.admin

import cn.com.guardiantech.aofgo.backend.annotation.NoArg
import org.hibernate.validator.constraints.Length
import javax.validation.constraints.NotNull

@NoArg
class RolePermissionRequest(

        @field:NotNull
        @field:Length(min = 1)
        val roleName: String,

        val permissions: List<String>?,
        val permission: String?
) {
    fun combinedPermissions(): MutableSet<String> {
        val permissions: MutableSet<String> = this.permissions.orEmpty().toMutableSet()
        this.permission?.let {
            if (!permissions.contains(it)) {
                permissions.add(it)
            }
        }
        return permissions
    }
}