package cn.com.guardiantech.aofgo.backend.jsonview

interface SubjectView {
    interface BriefView
    interface AuthenticationView : BriefView
    interface AdminView : AuthenticationView
    interface FullView : AdminView
}