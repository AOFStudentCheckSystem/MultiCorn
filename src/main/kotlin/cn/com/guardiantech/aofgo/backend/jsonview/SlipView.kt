package cn.com.guardiantech.aofgo.backend.jsonview

/**
 * Created by dedztbh on 18-4-4.
 * Project AOFGoBackend
 */
interface SlipView {
    interface StudentView
    interface FacultyView : StudentView
    interface FullView : FacultyView
}