package com.hs.application.usecase

import com.hs.application.exception.CommandAppExceptionMessage
import com.hs.dto.FindProductDto
import com.hs.entity.Product
import com.hs.repository.ProductQueryRepository
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
            name = product.detail.getName(),
            price = product.detail.getPrice(),
            stockQuantity = product.detail.getStockQuantity(),
            confirmStatus = product.detail.getConfirmStatus().toString(),
            imageUrls = product.imageUrls.getProductImageUrls()
        )
    }
}
