package com.hs.application.usecase

import com.hs.dto.FindProductDto
import com.hs.entity.ProductAggregate
import com.hs.vo.ProductInfo
import com.hs.repository.QueryAppProductAggregateRepository
import com.hs.service.RestGetRequestor
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Caching
import org.springframework.stereotype.Service

@Service
class ProductAggregateCommand(
    private val restGetRequestor: RestGetRequestor,
    private val productAggregateRepository: QueryAppProductAggregateRepository,
) {

    @Caching(
        evict = [
            CacheEvict(key = "#productId", cacheResolver = "productAggregateCacheEvictResolver"),
//            CacheEvict(
//                value = ["productAggregatePage"],
//                key = "#productId",
//                cacheResolver = "productAggregatePageCacheEvictResolver"
//            )
        ]
    )
    fun createOrUpdate(productId: Long) = runBlocking {
        val asyncProductDto: Deferred<FindProductDto> =
            async(Dispatchers.IO) { restGetRequestor.getProduct(productId = productId) }

        val asyncProductAggregate: Deferred<ProductAggregate?> =
            async(Dispatchers.IO) { findProductAggregate(productId = productId) }

        val productDto: FindProductDto = asyncProductDto.await()
        val productInfo = ProductInfo.create(
            name = productDto.name,
            price = productDto.price,
            stockQuantity = productDto.stockQuantity,
            imageUrls = productDto.imageUrls
        )

        when (val productAggregate: ProductAggregate? = asyncProductAggregate.await()) {
            null -> {
                productAggregateRepository.insert(
                    productAggregate = ProductAggregate.create(
                        productId = productDto.productId,
                        confirmStatus = productDto.confirmStatus,
                        productInfo = productInfo
                    )
                )
            }
            else -> {
                productAggregate.changeProductAggregateData(
                    productInfo = productInfo,
                    confirmStatus = productDto.confirmStatus
                )
                productAggregateRepository.save(productAggregate = productAggregate)
            }
        }
    }

    suspend fun findProductAggregate(productId: Long): ProductAggregate? {
        return productAggregateRepository.findByProductId(productId = productId)
    }
}
