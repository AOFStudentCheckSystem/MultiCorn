package cn.com.guardiantech.aofgo.backend.jackson.deserializer

import cn.com.guardiantech.aofgo.backend.request.checkin.RecordToUpload
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.IntNode
import com.fasterxml.jackson.databind.node.LongNode

class RecordToUploadDeserializer: JsonDeserializer<RecordToUpload>() {
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext?): RecordToUpload {
        val root: JsonNode = p.codec.readTree(p)
        val ts = (root["timestamp"] as LongNode).longValue()
        val status = (root["status"] as? IntNode)?.intValue() ?: 1
        val studentId = root["studentId"].asText()
        return RecordToUpload(
                timestamp = ts,
                status = status,
                studentId = studentId
        )
    }
}