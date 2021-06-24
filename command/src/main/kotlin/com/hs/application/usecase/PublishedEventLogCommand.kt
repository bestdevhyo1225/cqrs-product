package com.hs.application.usecase

import com.hs.entity.PublishedEventLog
import com.hs.event.ProductEvent
import com.hs.repository.PublishedEventLogRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class PublishedEventLogCommand(
    private val publishedEventLogRepository: PublishedEventLogRepository
) {

    fun create(event: ProductEvent) {
        publishedEventLogRepository.save(PublishedEventLog(commandCode = event.commandCode, message = event.toString()))
    }
}
