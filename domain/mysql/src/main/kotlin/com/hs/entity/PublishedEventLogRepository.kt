package com.hs.collection

import org.springframework.data.jpa.repository.JpaRepository

interface PublishedEventLogRepository : JpaRepository<PublishedEventLog, Long>
