package cn.com.guardiantech.aofgo.backend.service.auth

import cn.com.guardiantech.aofgo.backend.data.entity.authentication.Permission
import cn.com.guardiantech.aofgo.backend.data.entity.authentication.Role
import cn.com.guardiantech.aofgo.backend.exception.EntityNotFoundException
import cn.com.guardiantech.aofgo.backend.repository.auth.PermissionRepository
import cn.com.guardiantech.aofgo.backend.repository.auth.RoleRepository
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

    fun removePermission(permissionKey: String) {
        val pFind = permissionRepository.findByPermissionKey(permissionKey)
        if (pFind.isPresent) {
            permissionRepository.delete(pFind.get())
        } else {
            throw EntityNotFoundException("Failed to find Permission(permissionKey = $permissionKey), aborting.")
        }
    }

    fun listAllPermissionAsString(): List<String> =
        permissionRepository.findAllByOrderByPermissionKeyDesc().map {
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
}