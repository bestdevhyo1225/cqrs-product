package com.hs.application.usecase

import com.hs.dto.FindProductDto
import com.hs.message.CommandAppExceptionMessage
import com.hs.repository.ProductQueryRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class QueryService(
    private val productQueryRepository: ProductQueryRepository
) {

    fun findProductAggregate(id: Long): FindProductDto {
        return productQueryRepository.findProductAggregate(id = id)
            ?: throw NoSuchElementException(CommandAppExceptionMessage.NOT_FOUND_PRODUCT.localizedMessage)
    }
}
