package cn.com.guardiantech.aofgo.backend.service.auth

import cn.com.guardiantech.aofgo.backend.data.entity.authentication.Permission
import cn.com.guardiantech.aofgo.backend.data.entity.authentication.Role
import cn.com.guardiantech.aofgo.backend.exception.EntityNotFoundException
import cn.com.guardiantech.aofgo.backend.repository.auth.PermissionRepository
import cn.com.guardiantech.aofgo.backend.repository.auth.RoleRepository
import cn.com.guardiantech.aofgo.backend.request.authentication.admin.RolePermissionRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@Service
class AuthorizationService @Autowired constructor(
        private val permissionRepository: PermissionRepository,
        private val roleRepository: RoleRepository
){
    @PersistenceContext
    private lateinit var entityManager: EntityManager

    @Transactional(propagation = Propagation.REQUIRED)
    fun createPermission(permissionKey: String): Permission {
        try {
            return permissionRepository.save(
                    Permission(permissionKey = permissionKey)
            )
        } catch (e: DataIntegrityViolationException) {
            throw IllegalArgumentException("Invalid or Duplicate permissionKey found. Aborting.")
        }
    }

    @Transactional(propagation = Propagation.MANDATORY)
    fun removePermission(permissionKey: String) {
        val pFind = permissionRepository.findByPermissionKey(permissionKey)
        if (pFind.isPresent) {
            permissionRepository.delete(pFind.get())
        } else {
            throw EntityNotFoundException("Failed to find Permission(permissionKey = $permissionKey), aborting.")
        }
    }

    fun listAllPermissionAsString(): List<String> =
            permissionRepository.findAllByOrderByPermissionKeyAsc().map {
            it.permissionKey
        }


    @Transactional(propagation = Propagation.REQUIRED)
    fun createRole(roleName: String, permissions_in: Set<String> = setOf()): Role{
        val permissions: List<Permission> = permissions_in.map {
            permissionRepository.findByPermissionKey(it).get()  // TODO: Maybe exception handling when not found? IDK for now
        }

        var role = roleRepository.save(
                Role(
                        roleName = roleName
                )
        )

        permissions.forEach {
            role.permissions.add(it)
        }

        role = roleRepository.save(role)

        permissions.forEach {
            entityManager.refresh(it)
        }

        return role
    }

    @Transactional(propagation = Propagation.MANDATORY)
    fun removeRole(roleName: String) {
        val roleFind = roleRepository.findByRoleName(roleName)

        if (roleFind.isPresent) {
            try {
                roleRepository.delete(roleFind.get())
            } catch (e: Throwable) {
                throw IllegalStateException("Selected role is currently being used by one or more user. Can not be deleted.")
            }
        } else {
            throw IllegalArgumentException("Role (roleName = $roleName) was not found on the server.")
        }
    }

    fun listAllRolesAsString(): List<String> {
        return roleRepository.findAllByOrderByRoleNameAsc().map {
            it.roleName
        }
    }

    @Transactional(propagation = Propagation.MANDATORY)
    fun addPermissionToRole(roleName: String, permissions_in: Set<String>): Role {
        val roleFind = roleRepository.findByRoleName(roleName)

        if (!roleFind.isPresent) {
            throw IllegalArgumentException("Failed to find Role(roleName = $roleName)")
        }

        val permissions: List<Permission> = permissions_in.map {
            permissionRepository.findByPermissionKey(it).get()  // TODO: Maybe exception handling when not found? IDK for now
        }


        val role = roleFind.get()

        permissions.forEach {
            role.permissions.add(it)
        }

        return roleRepository.save(role)
    }
}