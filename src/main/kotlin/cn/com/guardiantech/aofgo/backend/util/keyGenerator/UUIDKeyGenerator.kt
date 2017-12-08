package cn.com.guardiantech.aofgo.backend.util.keyGenerator

import org.hibernate.engine.spi.SharedSessionContractImplementor
import org.hibernate.id.IdentifierGenerator
import java.io.Serializable
import java.util.*

/**
 * Created by Codetector on 2017/4/24.
 * Project classroom_backend
 */
class UUIDKeyGenerator: IdentifierGenerator {
    override fun generate(session: SharedSessionContractImplementor?, `object`: Any?): Serializable {
        return UUID.randomUUID().toString()
    }
}