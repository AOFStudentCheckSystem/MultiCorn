package cn.com.guardiantech.aofgo.backend.data.entity

import java.util.*
import javax.annotation.Nullable
import javax.persistence.*

@Entity
@Table(uniqueConstraints = [UniqueConstraint(columnNames = ["id_number"]), UniqueConstraint(columnNames = ["card_secret"])])
class Student(
        @Id
        @GeneratedValue
        @Column(name = "student_id")
        var id: Long = -1,

        @Column(name = "id_number", unique = true)
        var idNumber: String,

        @Column(name = "card_secret", unique = true)
        var cardSecret: String? = null,

        @Column(name = "grade")
        var grade: Int? = null,

        @Column(name = "date_of_birth")
        @Temporal(TemporalType.TIMESTAMP)
        var dateOfBirth: Date? = null,

        @Enumerated(EnumType.STRING)
        @Column(name = "gender")
        var gender: Gender? = null,

        @Column(name = "dorm")
        var dorm: String? = null,

        @Column(name = "dorm_info")
        var dormInfo: String? = null,

        @OneToMany
        @JoinTable
        var advisors: MutableSet<Account> = hashSetOf(),

        @OneToMany
        @JoinTable
        var parents: MutableSet<Account> = hashSetOf(),

        @OneToOne
        @JoinColumn
        var account: Account? = null

//        @OneToMany
//        @Column(name = "courses")
//        var courses: MutableSet<Courses> = hashSetOf()
)