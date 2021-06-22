package com.hs.application.usecase.command

import com.hs.entity.Product
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ProductConfirmStatusUpdateCommand(
    private val publisher: ApplicationEventPublisher
) {

    fun execute(id: Long, strProductConfirmStatus: String, product: Product) {
        product.changeConfirmStatus(strProductConfirmStatus = strProductConfirmStatus, publisher = publisher)
    }
}
