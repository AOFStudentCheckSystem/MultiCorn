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
import cn.com.guardiantech.aofgo.backend.request.account.AccountRequest
import cn.com.guardiantech.aofgo.backend.request.authentication.admin.*
import cn.com.guardiantech.aofgo.backend.service.AccountService
import cn.com.guardiantech.aofgo.backend.service.auth.AuthorizationService
import com.fasterxml.jackson.annotation.JsonView
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import javax.validation.Valid


@RestController
@RequestMapping("/auth/admin")
@Require(["SYSTEM_ADMIN"])
class AuthenticationAdminController @Autowired constructor(
        private val authorizationService: AuthorizationService,
        private val roleRepository: RoleRepository,
        private val subjectRepository: SubjectPageableRepository,
        private val accountPageableRepository: AccountPageableRepository,
        private val accountService: AccountService
) {

    @PutMapping("/permission")
    @Require(["PERMISSION_WRITE"])
    fun addPermission(@RequestBody @Valid permissionRequest: PermissionRequest): Permission {
        try {
            return authorizationService.createPermission(permissionRequest.permissionKey)
        } catch (e: IllegalArgumentException) {
            throw BadRequestException(e.message)
        }
    }

    @DeleteMapping("/permission/{permissionKey}")
    @Require(["PERMISSION_WRITE"])
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun removePermission(@PathVariable("permissionKey") key: String) {
        authorizationService.removePermission(key)
    }

    @GetMapping("/permission")
    @Require(["PERMISSION_READ"])
    fun listAllPermission(): List<String> {
        return authorizationService.listAllPermissionAsString()
    }

    @PutMapping("/role")
    @Require(["ROLE_WRITE"])
    fun createRole(@RequestBody @Valid roleRequest: RoleRequest): Role {
        return authorizationService.createRole(roleName = roleRequest.roleName, permissions_in = roleRequest.permissions ?: setOf())
    }

    @DeleteMapping("/role/{roleName}")
    @Require(["ROLE_WRITE"])
    fun deleteRole(@PathVariable("roleName") roleName: String) {
        // TODO: Handle Exceptions
        authorizationService.removeRole(roleName = roleName)
    }

    @GetMapping("/role")
    @Require(["ROLE_READ"])
    fun listAllRolesString(): List<String> {
        return authorizationService.listAllRolesAsString()
    }

    @GetMapping("/role-all")
    @Require(["ROLE_READ"])
    fun listAllRoles(): List<Role> {
        return roleRepository.findAllByOrderByRoleNameAsc()
    }

    @PostMapping("/role/permission")
    @Require(["ROLE_WRITE"])
    fun setPermission(@RequestBody @Valid rolePermRequest: RolePermissionRequest): Role {
        val permissions: MutableSet<String> = rolePermRequest.combinedPermissions()
        try {
            return authorizationService.modifyRole(roleName = rolePermRequest.roleName, permissions = permissions)
        } catch (e: NoSuchElementException) {
            throw BadRequestException("One or more requested permission was not found on the server.")
        }
    }

    @PutMapping("/role/permission")
    @Require(["ROLE_WRITE"])
    fun addPermission(@RequestBody @Valid rolePermRequest: RolePermissionRequest): Role {

        val permissions: MutableSet<String> = rolePermRequest.combinedPermissions()

        try {
            return authorizationService.addPermissionToRole(roleName = rolePermRequest.roleName, permissions_in = permissions)
        } catch (e: NoSuchElementException) {
            throw BadRequestException("One or more requested permission was not found on the server.")
        }
    }

    @PostMapping("/role/permission/remove")
    @Require(["ROLE_WRITE"])
    fun removePermission(@RequestBody @Valid rolePermRequest: RolePermissionRequest): Role {
        val permissions: MutableSet<String> = rolePermRequest.combinedPermissions()
        try {
            return authorizationService.removePermissionFromRole(rolePermRequest.roleName, permissions)
        } catch (e: NoSuchElementException) {
            throw BadRequestException("One or more requested permission was not found on the server.")
        }
    }

    @PutMapping("/subject/role")
    @Require(["SUBJECT_WRITE"])
    fun attachRoleToSubject(@RequestBody @Valid subjectRoleRequest: SubjectRoleRequest): Subject {
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

    @PatchMapping("/subject/role")
    @Require(["SUBJECT_WRITE"])
    fun removeRoleFromSubject(@RequestBody @Valid subjectRoleRequest: SubjectRoleRequest): Subject {
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
    @Require(["SUBJECT_READ"])
    @JsonView(SubjectView.BriefView::class)
    fun listAllSubjects(p: Pageable): Page<Subject> {
        return subjectRepository.findAll(p)
    }

    @GetMapping("/account")
    @Require(["ACCOUNT_READ"])
    @JsonView(SubjectView.BriefView::class)
    fun listAllAccounts(p: Pageable): Page<Account> {
        return accountPageableRepository.findAll(p)
    }

    @GetMapping("/account-admin")
    @Require(["ACCOUNT_READ"])
    @JsonView(SubjectView.AdminView::class)
    fun listAllAccountsAdminView(p: Pageable): Page<Account> {
        return accountPageableRepository.findAll(p)
    }

    @PutMapping("/account")
    @Require(["ACCOUNT_WRITE", "SUBJECT_WRITE"])
    @JsonView(SubjectView.AdminView::class)
    fun createAccountWithSubject(@RequestBody @Valid accountRequest: AccountRequest): Account =
        try {
            accountService.createAccount(accountRequest)
        } catch (e: Throwable) {
            //TODO: Finish Exception Handle
            throw BadRequestException("naive request")
        }

    @PostMapping("/subject")
    @Require(["SUBJECT_WRITE"])
    @JsonView(SubjectView.AdminView::class)
    fun editSubjectSetRole(@RequestBody @Valid subjectEditRequest: SubjectEditRequest): Subject =
        try {
            authorizationService.editSubjectSetRole(subjectEditRequest)
        } catch (e: Throwable) {
            //TODO: Finish Exception Handle
            throw BadRequestException("naive request")
        }

    @PostMapping("/account")
    @JsonView(SubjectView.BriefView::class)
    @Require(["ACCOUNT_WRITE", "SUBJECT_WRITE"])
    fun editAccount(@RequestBody @Valid accountRequest: AccountRequest): Account =
            try {
                if (accountRequest.id === null) throw BadRequestException("naive request")
                authorizationService.editAccount(accountRequest)
            } catch (e: Throwable) {
                throw BadRequestException("naive request")
            }
}