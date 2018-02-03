package cn.com.guardiantech.aofgo.backend.service.auth

import cn.com.guardiantech.aofgo.backend.authentication.SharedAuthConfiguration
import cn.com.guardiantech.aofgo.backend.data.entity.authentication.Permission
import cn.com.guardiantech.aofgo.backend.data.entity.authentication.PermissionType
import cn.com.guardiantech.aofgo.backend.data.entity.authentication.Role
import cn.com.guardiantech.aofgo.backend.data.entity.authentication.Subject
import cn.com.guardiantech.aofgo.backend.exception.EntityNotFoundException
import cn.com.guardiantech.aofgo.backend.repository.auth.PermissionRepository
import cn.com.guardiantech.aofgo.backend.repository.auth.RoleRepository
import cn.com.guardiantech.aofgo.backend.repository.auth.SubjectRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import javax.annotation.PostConstruct
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@Service
class AuthorizationService @Autowired constructor(
        private val permissionRepository: PermissionRepository,
        private val roleRepository: RoleRepository,
        private val subjectRepository: SubjectRepository
) {
    @PersistenceContext
    private lateinit var entityManager: EntityManager

    companion object {
        private val logger = LoggerFactory.getLogger(AuthorizationService::class.java)
    }

    fun initializePermissions() {
        val permissions = HashSet(SharedAuthConfiguration.declaredPermissions)
        var repoPermissions = permissionRepository.findByPermissionType(PermissionType.SYSTEM)
        val removalQueue = repoPermissions.filter {
            !permissions.contains(it.permissionKey)
        }
        removalQueue.forEach {
            try {
                permissionRepository.delete(it)
            } catch (e: Throwable) {
                logger.error("Failed to remove SYSTEM permission (${it.permissionKey})", e)
            }
        }
        repoPermissions = permissionRepository.findByPermissionType(PermissionType.SYSTEM)
        repoPermissions.forEach {
            permissions.remove(it.permissionKey)
        }
        permissions.forEach { permissionString ->
            val find = permissionRepository.findByPermissionKey(permissionString)
            if (find.isPresent) {
                val p = find.get()
                if (p.permissionType == PermissionType.USER) {
                    p.permissionType = PermissionType.SYSTEM
                    permissionRepository.save(p)
                } else {
                    logger.warn("Warning: Unexpected permission state.")
                }
            } else {
                this.createPermission(permissionString)
            }
        }
    }

    @Transactional
    fun createPermission(permissionKey: String, type: PermissionType = PermissionType.USER): Permission {
        try {
            return permissionRepository.save(
                    Permission(permissionKey = permissionKey, permissionType = type)
            )
        } catch (e: DataIntegrityViolationException) {
            if (type == PermissionType.SYSTEM) {
                logger.error("Failed creating system permission `$permissionKey`", e)
            }
            throw IllegalArgumentException("Invalid or Duplicate permissionKey found. Aborting.")
        }
    }

    @Transactional
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
    fun createRole(roleName: String, permissions_in: Set<String> = setOf()): Role {
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

    @Transactional
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

    @Transactional
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
            if (!role.permissions.contains(it)) {
                role.permissions.add(it)
            }
        }

        return roleRepository.save(role)
    }

    @Transactional
    fun removePermissionFromRole(roleName: String, permissions_in: Set<String>): Role {
        val roleFind = roleRepository.findByRoleName(roleName)

        if (!roleFind.isPresent) {
            throw IllegalArgumentException("Failed to find Role(roleName = $roleName)")
        }

        val permissions: List<Permission> = permissions_in.map {
            permissionRepository.findByPermissionKey(it).get()  // TODO: Maybe exception handling when not found? IDK for now
        }


        val role = roleFind.get()

        permissions.forEach {
            role.permissions.remove(it)
        }

        return roleRepository.save(role)
    }

    @Transactional
    fun addRoleToSubject(subjectId: Long, role_in: Set<String>): Subject {
        val subjectFind = subjectRepository.findById(subjectId)
        if (!subjectFind.isPresent) {
            throw NoSuchElementException("No Subject with id($subjectId) was found on the server.")
        }

        val roles = role_in.map {
            roleRepository.findByRoleName(it).get()
        }

        val subject = subjectFind.get()
        roles.forEach {
            if (!subject.roles.contains(it)) {
                subject.roles.add(it)
            }
        }

        return subjectRepository.save(subject)
    }

    @Transactional
    fun removeRoleFromSubject(subjectId: Long, role_in: Set<String>): Subject {
        val subjectFind = subjectRepository.findById(subjectId)
        if (!subjectFind.isPresent) {
            throw NoSuchElementException("No Subject with id($subjectId) was found on the server.")
        }

        val roles = role_in.map {
            roleRepository.findByRoleName(it).get()
        }

        val subject = subjectFind.get()
        roles.forEach {
            if (subject.roles.contains(it)) {
                subject.roles.remove(it)
            }
        }

        return subjectRepository.save(subject)
    }
}