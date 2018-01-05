package cn.com.guardiantech.aofgo.backend.data.entity

import javax.persistence.Column
import javax.persistence.GeneratedValue
import javax.persistence.Id

class Dorm(
        @Id
        @GeneratedValue
        val id: Long = -1,

        @Column(name = "name", unique = true)
        val name: String,

        @Column(name = "location")
        val location: String,

        @Column(name = "description")
        val description: String,

        @Column(name = "capacity")
        val capacity: Int
)