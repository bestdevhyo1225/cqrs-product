package com.hs.application.usecase

import com.hs.entity.PublishedEventLog
import com.hs.event.ProductEvent
import com.hs.repository.PublishedEventLogRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PublishedEventLogCommand(
    private val publishedEventLogRepository: PublishedEventLogRepository
) {

    /*
    * suspend 키워드가 있으면, 코루틴 컨텍스트 환경에서만 실행할 수 있다는 의미이다. 만약, 해당 키워드를 붙이지 않으면,
    * 어디에서나 실행할 수 있는 일반 메소드이다.
    * */
    suspend fun create(event: ProductEvent) {
        publishedEventLogRepository.save(PublishedEventLog(commandCode = event.commandCode, message = event.toString()))
    }
}
