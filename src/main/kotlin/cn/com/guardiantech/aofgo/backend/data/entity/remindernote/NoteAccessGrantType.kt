package cn.com.guardiantech.aofgo.backend.data.entity.remindernote

/**
 * Created by Codetector on 2018/04/06.
 * Project AOFGoBackend
 */
enum class NoteAccessGrantType(val value: String){
    OWNER("Owner"),
    READ("Read"),
    WRITE("Write")
}