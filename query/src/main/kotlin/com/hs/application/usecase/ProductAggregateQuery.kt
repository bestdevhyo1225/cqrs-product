package com.hs.application.usecase

import com.hs.entity.ProductAggregate
import com.hs.repository.ProductAggregateRepository
import com.hs.entity.ProductAggregateType
import com.hs.message.QueryAppExceptionMessage
import org.springframework.stereotype.Service

@Service
class ProductAggregateQuery(
    private val productAggregateRepository: ProductAggregateRepository
) {

    fun findProductAggregate(productId: Long): ProductAggregate {
        return productAggregateRepository.findByProductIdAndTypeAndIsDisplay(
            productId = productId,
            type = ProductAggregateType.FIND_PRODUCT
        ) ?: throw NoSuchElementException(QueryAppExceptionMessage.NOT_FOUND_PRODUCT.localizedMessage)
    }
}
