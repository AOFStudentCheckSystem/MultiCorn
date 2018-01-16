package cn.com.guardiantech.aofgo.backend.service.auth

import cn.com.guardiantech.aofgo.backend.data.entity.authentication.Permission
import cn.com.guardiantech.aofgo.backend.exception.EntityNotFoundException
import cn.com.guardiantech.aofgo.backend.repository.auth.PermissionRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class AuthorizationService @Autowired constructor(
        private val permissionRepository: PermissionRepository
){
    @Transactional
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
}