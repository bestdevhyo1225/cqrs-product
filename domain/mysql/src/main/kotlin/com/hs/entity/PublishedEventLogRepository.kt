package com.hs.entity

import org.springframework.data.jpa.repository.JpaRepository

interface PublishedEventLogRepository : JpaRepository<PublishedEventLog, Long>
