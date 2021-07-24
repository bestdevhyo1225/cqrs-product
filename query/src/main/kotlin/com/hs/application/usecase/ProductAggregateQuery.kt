package com.hs.application.usecase

import com.hs.dto.FindProductAggregateDto
import com.hs.entity.ProductAggregate
import com.hs.entity.ProductAggregateType.FIND_PRODUCT
import com.hs.message.QueryAppExceptionMessage
import com.hs.repository.QueryAppProductAggregateRepository
import org.springframework.stereotype.Service

@Service
class ProductAggregateQuery(
    private val productAggregateRepository: QueryAppProductAggregateRepository
) {

    fun findProductAggregate(productId: Long): FindProductAggregateDto {
        val productAggregate: ProductAggregate = productAggregateRepository.findByProductIdAndTypeAndIsDisplay(
            productId = productId,
            type = FIND_PRODUCT,
            isDisplay = true
        ) ?: throw NoSuchElementException(QueryAppExceptionMessage.NOT_FOUND_PRODUCT.localizedMessage)

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
}
