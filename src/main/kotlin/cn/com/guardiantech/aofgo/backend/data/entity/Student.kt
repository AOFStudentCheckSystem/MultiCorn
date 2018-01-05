package cn.com.guardiantech.aofgo.backend.data.entity

import java.util.*
import javax.persistence.*

@Entity
class Student(
        @Id
        @GeneratedValue
        @Column(name = "student_id")
        val id: Long = -1,

        @Column(name = "id_number")
        val idNumber: String,

        @Column(name = "card_secret")
        val cardSecret: String,

        @Column(name = "grade")
        val grade: Int,

        @Column(name = "date_of_birth")
        @Temporal(TemporalType.TIMESTAMP)
        val dateOfBirth: Date,

        @Enumerated(EnumType.STRING)
        @Column(name = "gender")
        val gender: Gender,

        @Column(name = "dorm")
        val dorm: String,

        @Column(name = "dorm_info")
        val dormInfo: String,

        @OneToMany
        @JoinColumn(name = "account_id")
        val advisors: MutableSet<Account> = hashSetOf(),

        @OneToMany
        @JoinColumn(name = "account_id")
        val parents: MutableSet<Account> = hashSetOf(),

        @OneToOne
        @JoinColumn(name = "account_id")
        val account: Account?

//        @OneToMany
//        @Column(name = "courses")
//        val courses: MutableSet<Courses> = hashSetOf()
)