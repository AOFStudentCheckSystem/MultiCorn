package cn.com.guardiantech.aofgo.backend.data.entity.checkin

import javax.persistence.*

/**
 * Created by Codetector on 2017/4/7.
 * Edited by DE_DZ_TBH on 2018/1/5.
 * Project backend
 */
@Entity
class SignUpSheet(
        @Id
        @GeneratedValue
//        @ApiObjectField(description = "0 -> Scheduled, 1 -> Open, -1 -> Closed")
        var id: Long = 0,

        var status: Int = 0,

        var name: String,

        @OneToMany(cascade = [(CascadeType.ALL)], orphanRemoval = true)
        var entries: MutableList<SignupSheetEntry> = arrayListOf()
)