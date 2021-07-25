package com.hs.application.usecase

import com.hs.dto.FindProductAggregateDto
import com.hs.dto.FindPaginationDto
import com.hs.entity.ProductAggregate
import com.hs.entity.ProductAggregateType.FIND_PRODUCT
import com.hs.message.QueryAppExceptionMessage
import com.hs.repository.QueryAppProductAggregateRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

@Service
class ProductAggregateQuery(
    private val productAggregateRepository: QueryAppProductAggregateRepository
) {

    fun findProductAggregatesWithPagination(
        page: Int,
        pageSize: Int
    ): FindPaginationDto<FindProductAggregateDto> {
        val productAggregatesPagination: Page<ProductAggregate> = productAggregateRepository.findAllByTypeAndIsDisplay(
            type = FIND_PRODUCT,
            isDisplay = true,
            pageable = PageRequest.of(page, pageSize)
        )

        val items: List<FindProductAggregateDto> = productAggregatesPagination.content.map { productAggregate ->
            FindProductAggregateDto(
                productId = productAggregate.data.productId,
                name = productAggregate.data.name,
                price = productAggregate.data.price,
                stockQuantity = productAggregate.data.stockQuantity,
                imageUrls = productAggregate.data.imageUrls,
                createdDatetime = productAggregate.createdDatetime,
                updatedDatetime = productAggregate.updatedDatetime
            )
        }

        return FindPaginationDto(
            items = items,
            page = page,
            pageSize = pageSize,
            totalCount = productAggregatesPagination.totalElements
        )
    }

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
