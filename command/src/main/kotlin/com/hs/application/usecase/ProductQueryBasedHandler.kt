package com.hs.application.usecase

import com.hs.dto.FindProductDto
import com.hs.entity.Product
import com.hs.message.CommandAppExceptionMessage
import com.hs.repository.ProductQueryRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class ProductQueryBasedHandler(
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
