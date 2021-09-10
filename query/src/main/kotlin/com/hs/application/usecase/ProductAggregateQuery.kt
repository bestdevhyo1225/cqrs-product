package com.hs.application.usecase

import com.hs.dto.FindProductAggregateDto
import com.hs.dto.FindPaginationDto
import com.hs.dto.FindProductAggregatePaginationDto
import com.hs.entity.ProductAggregate
import com.hs.message.QueryAppExceptionMessage
import com.hs.repository.QueryAppProductAggregateRepository
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
class ProductAggregateQuery(
    private val productAggregateRepository: QueryAppProductAggregateRepository
) {

    //    @Cacheable(
//        value = ["productAggregatePage"],
//        key = "#page.toString().concat('-').concat(#pageSize)",
//        cacheManager = "redisCacheManager"
//    )
    fun findProductAggregatesWithPagination(
        page: Int,
        pageSize: Int
    ): FindPaginationDto {
        val productAggregatesPagination: Pair<List<ProductAggregate>, Long> =
            productAggregateRepository.findAllByIsDisplay(isDisplay = true, page = page, pageSize = pageSize)

        val items: List<FindProductAggregatePaginationDto> = productAggregatesPagination.first.map { productAggregate ->
            FindProductAggregatePaginationDto(
                productId = productAggregate.productInfo.id,
                name = productAggregate.productInfo.name,
                price = productAggregate.productInfo.price
            )
        }

        return FindPaginationDto(
            items = items,
            page = page,
            pageSize = pageSize,
            totalCount = productAggregatesPagination.second
        )
    }

    @Cacheable(
        // key = "'copy-'.concat(#copyNumber).concat('::').concat(#productId)"
        key = "#copyPrefixKey.concat('::').concat(#productId)",
        cacheResolver = "productAggregateCacheableResolver"
    )
    fun findProductAggregate(copyPrefixKey: String, productId: Long): FindProductAggregateDto {
        val productAggregate: ProductAggregate = productAggregateRepository.findByProductIdAndIsDisplay(
            productId = productId,
            isDisplay = true
        ) ?: throw NoSuchElementException(QueryAppExceptionMessage.NOT_FOUND_PRODUCT.localizedMessage)

        return FindProductAggregateDto(
            productId = productAggregate.productInfo.id,
            name = productAggregate.productInfo.name,
            price = productAggregate.productInfo.price,
            stockQuantity = productAggregate.productInfo.stockQuantity,
            imageUrls = productAggregate.productInfo.imageUrls,
            createdDatetime = productAggregate.convertToStringCreatedDatetime(),
            updatedDatetime = productAggregate.convertToStringUpdatedDatetime()
        )
    }
}
