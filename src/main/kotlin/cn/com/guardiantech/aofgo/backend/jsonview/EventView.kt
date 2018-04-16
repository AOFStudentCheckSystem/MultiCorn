package cn.com.guardiantech.aofgo.backend.jsonview

/**
 * Created by Codetector on 2018/02/02.
 * Project AOFGoBackend
 */
interface EventView {
    interface NormalView
    interface EventGroupView : NormalView
    interface EventRecordView : NormalView
    interface FullEvent : EventGroupView, EventRecordView
}