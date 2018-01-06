package cn.com.guardiantech.aofgo.backend.data.entity.checkin

/**
 * Created by dedztbh on 1/6/18.
 * Project AOFGoBackend
 */
enum class EventStatus(val status: Int) {
    FUTURE(0),
    BOARDING(1),
    COMPLETED(2)
}