package cn.com.guardiantech.aofgo.backend.data.entity

import java.util.*
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
        var guardians: MutableSet<Guardian> = hashSetOf(),

        @OneToOne
        @JoinColumn
        var account: Account? = null

//        @OneToMany
//        @Column(name = "courses")
//        var courses: MutableSet<Courses> = hashSetOf()
) {
    fun relationWith(account: Account): GuardianType? {
        return guardians.firstOrNull {
            it.guardianAccount.id == account.id
        }?.relation
    }

    fun addGuardian(guardian: Guardian) {
        guardians.add(guardian)
    }
}