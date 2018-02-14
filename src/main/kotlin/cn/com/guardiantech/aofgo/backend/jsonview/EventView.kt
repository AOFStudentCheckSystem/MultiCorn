package cn.com.guardiantech.aofgo.backend.jsonview

/**
 * Created by Codetector on 2018/02/02.
 * Project AOFGoBackend
 */
interface EventView {
    interface EventGroupView
    interface EventRecordView
    interface FullEvent : EventGroupView, EventRecordView
}