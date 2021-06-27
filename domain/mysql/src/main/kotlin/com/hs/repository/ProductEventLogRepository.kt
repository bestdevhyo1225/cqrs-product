package com.hs.repository

import com.hs.entity.PublishedEventLog
import org.springframework.data.jpa.repository.JpaRepository

interface PublishedEventLogRepository : JpaRepository<PublishedEventLog, Long>
