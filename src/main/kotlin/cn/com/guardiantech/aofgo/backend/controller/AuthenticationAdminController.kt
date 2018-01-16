package cn.com.guardiantech.aofgo.backend.controller

import cn.com.guardiantech.aofgo.backend.data.entity.authentication.Permission
import cn.com.guardiantech.aofgo.backend.exception.BadRequestException
import cn.com.guardiantech.aofgo.backend.repository.auth.PermissionRepository
import cn.com.guardiantech.aofgo.backend.request.authentication.admin.PermissionRequest
import cn.com.guardiantech.aofgo.backend.service.auth.AuthorizationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/auth/admin")
class AuthenticationAdminController @Autowired constructor(
        private val authorizationService: AuthorizationService,
        private val permissionRepository: PermissionRepository
){

    @PutMapping("/permission")
    fun addPermission(permissionRequest: PermissionRequest): Permission {
        try {
            return authorizationService.createPermission(permissionRequest.permissionKey)
        } catch (e: IllegalArgumentException) {
            throw BadRequestException(e.message)
        }
    }

    @DeleteMapping("/permission")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun removePermission(permissionRequest: PermissionRequest) {
        authorizationService.removePermission(permissionRequest.permissionKey)
    }

    @GetMapping("/permission")
    fun listAllPermission(): List<Permission> {
        return permissionRepository.findAllByOrderByPermissionKeyDesc()
    }
}