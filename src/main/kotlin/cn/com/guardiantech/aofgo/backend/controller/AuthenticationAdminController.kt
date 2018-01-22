package cn.com.guardiantech.aofgo.backend.controller

import cn.com.guardiantech.aofgo.backend.annotation.Require
import cn.com.guardiantech.aofgo.backend.data.entity.authentication.Permission
import cn.com.guardiantech.aofgo.backend.data.entity.authentication.Role
import cn.com.guardiantech.aofgo.backend.exception.BadRequestException
import cn.com.guardiantech.aofgo.backend.repository.auth.PermissionRepository
import cn.com.guardiantech.aofgo.backend.request.authentication.admin.PermissionRequest
import cn.com.guardiantech.aofgo.backend.request.authentication.admin.RolePermissionRequest
import cn.com.guardiantech.aofgo.backend.request.authentication.admin.RoleRequest
import cn.com.guardiantech.aofgo.backend.service.auth.AuthorizationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/auth/admin")
@Require
class AuthenticationAdminController @Autowired constructor(
        private val authorizationService: AuthorizationService,
        private val permissionRepository: PermissionRepository
) {

    @PutMapping("/permission")
    fun addPermission(@RequestBody permissionRequest: PermissionRequest): Permission {
        try {
            return authorizationService.createPermission(permissionRequest.permissionKey)
        } catch (e: IllegalArgumentException) {
            throw BadRequestException(e.message)
        }
    }

    @DeleteMapping("/permission")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun removePermission(@RequestBody permissionRequest: PermissionRequest) {
        authorizationService.removePermission(permissionRequest.permissionKey)
    }

    @GetMapping("/permission")
    fun listAllPermission(): List<String> {
        return authorizationService.listAllPermissionAsString()
    }

    @PutMapping("/role")
    fun createRole(roleRequest: RoleRequest): Role {
        return authorizationService.createRole(roleName = roleRequest.roleName, permissions_in = roleRequest.permissions ?: setOf())
    }

    @DeleteMapping("/role")
    fun deleteRole(roleRequest: RoleRequest) {
        // TODO: Handle Exceptions
        authorizationService.removeRole(roleName = roleRequest.roleName)
    }

    @GetMapping("/role")
    fun listAllRoles(): List<String> {
        return authorizationService.listAllRolesAsString()
    }

    @PutMapping("/role/permission")
    fun addPermission(rolePermRequest: RolePermissionRequest): Role {

        val permissions: MutableSet<String> = rolePermRequest.permissions.orEmpty().toMutableSet()

        rolePermRequest.permission?.let {
            permissions.add(it)
        }

        return authorizationService.addPermissionToRole(roleName = rolePermRequest.roleName, permissions_in = permissions) // TODO: Not use request object in service
    }


}