package com.hs.application.usecase

import com.hs.dto.FindProductDto
import com.hs.entity.Product
import com.hs.message.CommandAppExceptionMessage
import com.hs.repository.ProductRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional // Write DB 쪽으로 조회하면, 데이터의 일관성이 보장되기 때문에 ReadOnly 옵션을 사용하지 않았다.
class ProductQuery(
    private val productRepository: ProductRepository
) {

    fun findProductAggregate(id: Long): FindProductDto {
        val product: Product = productRepository.findProductWithFetchJoin(id = id)
            ?: throw NoSuchElementException(CommandAppExceptionMessage.NOT_FOUND_PRODUCT.localizedMessage)

        return FindProductDto(
            productId = product.id!!,
            name = product.name,
            price = product.price,
            stockQuantity = product.stockQuantity,
            confirmStatus = product.confirmStatus.toString(),
            imageUrls = product.imageUrls
        )
    }
}
