package cn.com.guardiantech.aofgo.backend.data.entity.checkin

import javax.persistence.*

/**
 * Created by Codetector on 2017/4/7.
 * Edited by DE_DZ_TBH on 2018/1/5.
 * Project backend
 */
@Entity
class EventGroup(
        @Id
        @GeneratedValue
        var id: Long = 0,

        var name: String = "",

        @ManyToMany(fetch = FetchType.LAZY)
        var events: MutableSet<ActivityEvent> = hashSetOf(),

        @OneToMany(fetch = FetchType.LAZY, mappedBy = "eventGroup")
        var inEntries: MutableSet<SignupSheetEntry> = hashSetOf()
) {


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as EventGroup

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}