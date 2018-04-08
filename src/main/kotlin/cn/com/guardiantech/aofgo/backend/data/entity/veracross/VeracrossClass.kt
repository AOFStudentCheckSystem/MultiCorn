package cn.com.guardiantech.aofgo.backend.data.entity.veracross

import javax.persistence.*

@Entity
class VeracrossClass (
        @Id
        @GeneratedValue
        @Column(name = "class_id")
        var id: Long = -1,

        @Column(name = "class_identifier")
        var identifier: String? = null,

        @Column(name = "class_key")
        var key: String? = null,

        @OneToMany
        @JoinColumn(name = "assignment_id")
        var assignment: MutableSet<VeracrossAssignment> = hashSetOf(),

        @OneToMany
        @
)