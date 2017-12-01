package cn.com.guardiantech.aofgo.backend.data.entity.authentication

import cn.com.guardiantech.aofgo.backend.annotation.NoArg
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

/**
 * Created by Codetector on 29/11/2017.
 * Project aofgo-backend
 */
@NoArg
@Entity
class Subject (
    @Id
    @GeneratedValue
    val id: Long = -1
)