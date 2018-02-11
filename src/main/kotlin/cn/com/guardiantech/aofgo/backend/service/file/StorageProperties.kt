package cn.com.guardiantech.aofgo.backend.service.file

import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * Created by Codetector on 2018/02/11.
 * Project AOFGoBackend
 */
@ConfigurationProperties("storage")
open class StorageProperties {

    var location = "upload-dir"
}
