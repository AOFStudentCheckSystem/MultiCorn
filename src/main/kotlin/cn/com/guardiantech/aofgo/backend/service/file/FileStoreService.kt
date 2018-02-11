package cn.com.guardiantech.aofgo.backend.service.file

import cn.com.guardiantech.aofgo.backend.data.entity.file.StoredFile
import cn.com.guardiantech.aofgo.backend.exception.InvalidFileException
import javassist.NotFoundException
import org.apache.commons.codec.digest.DigestUtils
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.util.FileCopyUtils
import java.io.File
import java.io.InputStream
import java.io.OutputStream
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import java.security.DigestInputStream
import java.util.*
import javax.annotation.PostConstruct

/**
 * Created by Codetector on 2018/02/11.
 * Project AOFGoBackend
 */
@Service
class FileStoreService @Autowired constructor(
        private val storgeProperties: StorageProperties
){

    lateinit var rootDirectory: File
    companion object {
        private val logger = LoggerFactory.getLogger(FileStoreService::class.java)
    }

    @PostConstruct
    fun init() {
        rootDirectory = File(storgeProperties.location)

        if (!rootDirectory.exists()){
            rootDirectory.mkdirs()
        }
    }


    fun getTempFile(): File {
        File(rootDirectory, UUID.randomUUID().toString() + ".tmp").let {
            it.createNewFile()
            return it
        }
    }

    private fun getFileObject(instance: StoredFile): File {
        return File(rootDirectory, "${instance.fileId}.fsar")
    }

    fun storeStream(instance: StoredFile): OutputStream {
        val file = getFileObject(instance)
        try {
            if (file.exists()) {
                throw FileSystemException(file)
            }
            return file.outputStream()
        } catch (e: Throwable) {
            throw FileSystemException(file)
        }
    }

    fun storeFile(instance: StoredFile, file: File, removeOriginal: Boolean = true): Boolean {
        if (file.exists() && file.isFile) {
            try {
                if (removeOriginal) {
                    Files.move(file.toPath(), getFileObject(instance).toPath(), StandardCopyOption.REPLACE_EXISTING)
                } else {
                    FileCopyUtils.copy(file.inputStream(), storeStream(instance))
                }
                return true
            } catch (t: Throwable) {
                logger.error("File service unknown error occurred", t)
                // Ignored
            }
        }
        return false
    }

    fun loadAsStream(instance: StoredFile): InputStream {
        return getFileObject(instance).inputStream()
    }

    fun exists(fileName: String): Boolean {
        return File(rootDirectory, fileName).exists()
    }

    fun exists(instance: StoredFile): Boolean {
        return getFileObject(instance).exists()
    }

    fun delete(instance: StoredFile) {
        TODO()
    }

//    fun deletePermanently(instance: FileInstance) {
//        val file = getFileObject(instance)
//        if (file.exists()) {
//            file.deleteRecursively()
//        }
//        fileInstanceRepository.delete(instance)
//    }

    fun length(instance: StoredFile): Long {
        return getFileObject(instance).length()
    }

    fun computeHash(file: File): String {
        val digest = DigestUtils.getSha256Digest()
        val digestStream: DigestInputStream = DigestInputStream(file.inputStream(), digest)
        var buffer = ByteArray(2048)
        while (digestStream.read(buffer) != -1) {
            // blank
        }
        val digestResult = digest.digest()
        val sb = StringBuilder()
        for (i in 0 until digestResult.size) {
            sb.append(String.format("%x", digestResult[i]))
        }
        return sb.toString().toLowerCase()
    }

    fun verify(instance: StoredFile) {
        val file = getFileObject(instance)
        if (!file.exists()) {
            throw NotFoundException("File Not Found")
        }
        if (!file.isFile) {
            throw InvalidFileException("Not A File")
        }
    }

    fun validate(instance: StoredFile): Boolean {
        val originalIntegrity = instance.hash.toLowerCase()
        verify(instance)
        val computedIntegrity = computeHash(getFileObject(instance))
        return computedIntegrity == originalIntegrity
    }
}