package cn.com.guardiantech.aofgo.backend.exception

class UndefinedPermissionException(name: String) : RuntimeException("Permission $name is not defined in the database") {
}