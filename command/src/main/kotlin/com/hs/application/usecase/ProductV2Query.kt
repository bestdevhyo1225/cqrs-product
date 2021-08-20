package com.hs.application.usecase

import com.hs.dto.FindProductDto
import com.hs.entity.ProductV2
import com.hs.message.CommandAppExceptionMessage
import com.hs.repository.ProductV2Repository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class ProductV2Query(
    private val productRepository: ProductV2Repository
) {

    fun findProduct(id: Long): FindProductDto {
        val product: ProductV2 = productRepository.findProductWithFetchJoin(id = id)
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
