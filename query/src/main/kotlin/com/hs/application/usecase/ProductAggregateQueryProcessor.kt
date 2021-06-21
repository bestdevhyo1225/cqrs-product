package com.hs.application.usecase

import com.hs.dto.FindProductAggregateDto
import com.hs.entity.ProductAggregate
import com.hs.repository.ProductAggregateRepository
import com.hs.entity.ProductAggregateType.FIND_PRODUCT
import com.hs.message.QueryAppExceptionMessage
import org.springframework.stereotype.Service

@Service
class ProductAggregateQueryProcessor(
    private val productAggregateRepository: ProductAggregateRepository
) {

    fun findProductAggregate(productId: Long): FindProductAggregateDto {
        val productAggregate: ProductAggregate = productAggregateRepository.findByProductIdAndType(
            productId = productId,
            type = FIND_PRODUCT
        ) ?: throw NoSuchElementException(QueryAppExceptionMessage.NOT_FOUND_PRODUCT.localizedMessage)

        if (!productAggregate.isDisplay.toBoolean()) {
            throw NoSuchElementException(QueryAppExceptionMessage.NOT_FOUND_PRODUCT_BY_CREATE_OR_UPDATE.localizedMessage)
        }

        return FindProductAggregateDto(
            productId = productAggregate.data.productId,
            name = productAggregate.data.name,
            price = productAggregate.data.price,
            stockQuantity = productAggregate.data.stockQuantity,
            imageUrls = productAggregate.data.imageUrls,
            createdDatetime = productAggregate.createdDatetime,
            updatedDatetime = productAggregate.updatedDatetime
        )
    }

    suspend fun findProductAggregateForCommand(productId: Long): ProductAggregate? {
        return productAggregateRepository.findByProductIdAndType(productId = productId, type = FIND_PRODUCT)
    }
}
