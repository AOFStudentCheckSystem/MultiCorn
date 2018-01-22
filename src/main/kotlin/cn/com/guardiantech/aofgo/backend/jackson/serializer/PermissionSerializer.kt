package cn.com.guardiantech.aofgo.backend.jackson.serializer

import cn.com.guardiantech.aofgo.backend.data.entity.authentication.Permission
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider

class PermissionSerializer : JsonSerializer<Permission>() {
    override fun serialize(value: Permission?, gen: JsonGenerator?, serializers: SerializerProvider?) {
        gen?.writeString(value?.permissionKey)
    }
}