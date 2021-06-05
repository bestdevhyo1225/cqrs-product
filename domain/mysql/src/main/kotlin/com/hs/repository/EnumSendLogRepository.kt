package com.hs.repository

import com.hs.entity.EventSendLog
import org.springframework.data.jpa.repository.JpaRepository

interface EnumSendLogRepository : JpaRepository<EventSendLog, Long>
