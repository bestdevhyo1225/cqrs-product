package com.hs.application.usecase

import com.hs.application.exception.CommandAppExceptionMessage
import com.hs.dto.FindProductDto
import com.hs.jpa.entity.Product
import com.hs.jpa.repository.ProductQueryRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class ProductQuery(
    private val productQueryRepository: ProductQueryRepository
) {

    fun findProduct(id: Long): FindProductDto {
        val product: Product = productQueryRepository.findProductWithFetchJoin(id = id)
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
