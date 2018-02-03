package cn.com.guardiantech.aofgo.backend.controller

import cn.com.guardiantech.aofgo.backend.annotation.Require
import cn.com.guardiantech.aofgo.backend.data.entity.Account
import cn.com.guardiantech.aofgo.backend.data.entity.authentication.Permission
import cn.com.guardiantech.aofgo.backend.data.entity.authentication.Role
import cn.com.guardiantech.aofgo.backend.data.entity.authentication.Subject
import cn.com.guardiantech.aofgo.backend.exception.BadRequestException
import cn.com.guardiantech.aofgo.backend.jsonview.SubjectView
import cn.com.guardiantech.aofgo.backend.repository.auth.AccountPageableRepository
import cn.com.guardiantech.aofgo.backend.repository.auth.SubjectPageableRepository
import cn.com.guardiantech.aofgo.backend.repository.auth.RoleRepository
import cn.com.guardiantech.aofgo.backend.request.authentication.admin.PermissionRequest
import cn.com.guardiantech.aofgo.backend.request.authentication.admin.RolePermissionRequest
import cn.com.guardiantech.aofgo.backend.request.authentication.admin.RoleRequest
import cn.com.guardiantech.aofgo.backend.request.authentication.admin.SubjectRoleRequest
import cn.com.guardiantech.aofgo.backend.service.auth.AuthorizationService
import com.fasterxml.jackson.annotation.JsonView
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/auth/admin")
@Require(["SYSTEM_ADMIN"])
class AuthenticationAdminController @Autowired constructor(
        private val authorizationService: AuthorizationService,
        private val roleRepository: RoleRepository,
        private val subjectRepository: SubjectPageableRepository,
        private val accountPageableRepository: AccountPageableRepository
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
    fun createRole(@RequestBody roleRequest: RoleRequest): Role {
        return authorizationService.createRole(roleName = roleRequest.roleName, permissions_in = roleRequest.permissions ?: setOf())
    }

    @DeleteMapping("/role")
    fun deleteRole(@RequestBody roleRequest: RoleRequest) {
        // TODO: Handle Exceptions
        authorizationService.removeRole(roleName = roleRequest.roleName)
    }

    @GetMapping("/role")
    fun listAllRolesString(): List<String> {
        return authorizationService.listAllRolesAsString()
    }

    @GetMapping("/role-all")
    fun listAllRoles(): List<Role> {
        return roleRepository.findAllByOrderByRoleNameAsc()
    }

    @PutMapping("/role/permission")
    fun addPermission(@RequestBody rolePermRequest: RolePermissionRequest): Role {

        val permissions: MutableSet<String> = rolePermRequest.permissions.orEmpty().toMutableSet()

        rolePermRequest.permission?.let {
            if (!permissions.contains(it)) {
                permissions.add(it)
            }
        }

        try {
            return authorizationService.addPermissionToRole(roleName = rolePermRequest.roleName, permissions_in = permissions) // TODO: Not use request object in service
        } catch (e: NoSuchElementException) {
            throw BadRequestException("One or more requested permission was not found on the server.")
        }
    }

    @DeleteMapping("/role/permission")
    fun removePermission(@RequestBody rolePermRequest: RolePermissionRequest): Role {
        val permissions: MutableSet<String> = rolePermRequest.permissions.orEmpty().toMutableSet()

        rolePermRequest.permission?.let {
            permissions.add(it)
        }
        try {
            return authorizationService.removePermissionFromRole(rolePermRequest.roleName, permissions)
        } catch (e: NoSuchElementException) {
            throw BadRequestException("One or more requested permission was not found on the server.")
        }
    }

    @PutMapping("/subject/role")
    fun attachRoleToSubject(@RequestBody subjectRoleRequest: SubjectRoleRequest): Subject {
        val roles: MutableSet<String> = subjectRoleRequest.roles.orEmpty().toMutableSet()

        subjectRoleRequest.role?.let {
            roles.add(it)
        }

        try {
            return authorizationService.addRoleToSubject(subjectRoleRequest.subjectId, roles)
        } catch (e: NoSuchElementException) {
            throw BadRequestException(e.message)
        }
    }

    @DeleteMapping("/subject/role")
    fun removeRoleFromSubject(@RequestBody subjectRoleRequest: SubjectRoleRequest): Subject {
        val roles: MutableSet<String> = subjectRoleRequest.roles.orEmpty().toMutableSet()
        subjectRoleRequest.role?.let {
            roles.add(it)
        }
        try {
            return authorizationService.removeRoleFromSubject(subjectRoleRequest.subjectId, roles)
        } catch (e: NoSuchElementException) {
            throw BadRequestException(e.message)
        }
    }

    @GetMapping("/subject")
    @JsonView(SubjectView.BriefView::class)
    fun listAllSubjects(p: Pageable): Page<Subject> {
        return subjectRepository.findAll(p)
    }

    @GetMapping("/account")
    @JsonView(SubjectView.BriefView::class)
    fun listAllAccounts(p: Pageable): Page<Account> {
        return accountPageableRepository.findAll(p)
    }
}