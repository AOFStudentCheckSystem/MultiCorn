package cn.com.guardiantech.aofgo.backend.data.entity.checkin

import java.util.*
import javax.persistence.*

/**
 * Created by Codetector on 2017/4/4.
 * Edited by DE_DZ_TBH on 2018/1/5.
 * Project backend
 */
@Entity
@Table(name = "events")
class ActivityEvent(
        @Id
        @GeneratedValue
        var id: Int = 0,

        @Column(unique = true)
        var eventId: String = System.currentTimeMillis().toString(36).toLowerCase(),

        var eventName: String,

        @Lob
        var eventDescription: String = "",

        var eventTime: Date = Date(),

        @Enumerated(value = EnumType.STRING)
        var eventStatus: EventStatus = EventStatus.FUTURE,

        @OneToMany(mappedBy = "event", fetch = FetchType.LAZY, cascade = arrayOf(CascadeType.ALL))
        @Column(nullable = false)
        var records: MutableSet<ActivityEventRecord> = hashSetOf(),

        @ManyToMany(mappedBy = "events", fetch = FetchType.LAZY)
        var eventGroups: Set<EventGroup>? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as ActivityEvent

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}