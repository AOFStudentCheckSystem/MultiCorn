//package cn.com.guardiantech.aofgo.backend.repository.checkin
//
//import cn.com.guardiantech.checkin.server.entity.authentication.User
//import org.springframework.data.repository.CrudRepository
//import java.util.*
//
///**
// * Created by Codetector on 2017/4/6.
// * Project backend
// */
//interface UserRepository: CrudRepository<User, Long> {
//    fun findByEmailIgnoreCase(email: String): Optional<User>
//
//    //Search Functions
//    fun findByEmailStartingWithIgnoreCase(emailBegin: String): List<User>
//}