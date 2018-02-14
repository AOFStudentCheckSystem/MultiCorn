package cn.com.guardiantech.aofgo.backend.repository.checkin

import cn.com.guardiantech.aofgo.backend.data.entity.checkin.EventGroup
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.PagingAndSortingRepository
import java.util.*
import javax.transaction.Transactional

/**
 * Created by Codetector on 2017/4/7.
 * Project backend
 */
interface EventGroupRepository : PagingAndSortingRepository<EventGroup, Long> {
    fun findById(id: Long): Optional<EventGroup>

    fun findAllByOrderByIdDesc(pageable: Pageable): Page<EventGroup>

    @Modifying
    @Transactional
    fun removeById(id: Long): Long

    @Query("select e from EventGroup e WHERE e.inEntries IS EMPTY")
    fun findGroupWithoutBinding(): List<EventGroup>
}