package com.hs.application.usecase

import com.hs.RestGetRequestor
import com.hs.dto.FindProductDto
import com.hs.entity.ProductAggregate
import com.hs.vo.ProductInfo
import com.hs.repository.QueryAppProductAggregateRepository
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
            CacheEvict(key = "#productId", cacheResolver = "productAggregateCacheEvictResolver")
        ]
    )
    fun createOrUpdate(productId: Long) = runBlocking {
        val asyncProductDto: Deferred<FindProductDto> =
            async(Dispatchers.IO) { restGetRequestor.getProduct(productId = productId) }

        val asyncProductAggregate: Deferred<ProductAggregate?> =
            async(Dispatchers.IO) { findProductAggregate(productId = productId) }

        val productDto: FindProductDto = asyncProductDto.await()
        when (val productAggregate: ProductAggregate? = asyncProductAggregate.await()) {
            null -> insertProductAggregate(productDto)
            else -> updateProductAggregate(productAggregate, productDto)
        }
    }

    suspend fun findProductAggregate(productId: Long): ProductAggregate? {
        return productAggregateRepository.findByProductId(productId = productId)
    }

    private fun insertProductAggregate(productDto: FindProductDto) {
        productAggregateRepository.insert(
            productAggregate = ProductAggregate.create(
                productId = productDto.productId,
                name = productDto.name,
                price = productDto.price,
                stockQuantity = productDto.stockQuantity,
                imageUrls = productDto.imageUrls,
                confirmStatus = productDto.confirmStatus,
            )
        )
    }

    private fun updateProductAggregate(
        productAggregate: ProductAggregate,
        productDto: FindProductDto
    ): ProductAggregate? {
        productAggregate.changeProductAggregateData(
            name = productDto.name,
            price = productDto.price,
            stockQuantity = productDto.stockQuantity,
            imageUrls = productDto.imageUrls,
            confirmStatus = productDto.confirmStatus
        )

        return productAggregateRepository.save(productAggregate = productAggregate)
    }
}
