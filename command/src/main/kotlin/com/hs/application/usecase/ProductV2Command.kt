package com.hs.application.usecase

import com.hs.dto.CreateProductDto
import com.hs.dto.UpdateProductDto
import com.hs.entity.ProductV2
import com.hs.message.CommandAppExceptionMessage
import com.hs.repository.ProductV2Repository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ProductV2Command(
    private val productRepository: ProductV2Repository
) {

    fun create(createProductDto: CreateProductDto): Long? {
        val product: ProductV2 = productRepository.save(
            product = ProductV2(
                name = createProductDto.name,
                price = createProductDto.price,
                stockQuantity = createProductDto.stockQuantity,
                imageUrls = createProductDto.imageUrls
            )
        )

        return product.id
    }

    fun update(updateProductDto: UpdateProductDto) {
        val product: ProductV2 = findProduct(id = updateProductDto.id)

        product.update(
            name = updateProductDto.name,
            price = updateProductDto.price,
            stockQuantity = updateProductDto.stockQuantity,
        )

        productRepository.update(product = product)
    }

    fun decreaseStockQuantity(id: Long, completeStockQuantity: Int) {
        val product: ProductV2 = findProduct(id = id)

        product.decreaseStockCount(stockQuantity = completeStockQuantity)

        productRepository.updateStockQuantity(product = product)
    }

    fun changeConfirmStatus(id: Long, strProductConfirmStatus: String) {

    }

    fun updateImage(id: Long, imageUrls: List<String>) {

    }

    fun findProduct(id: Long): ProductV2 {
        return productRepository.findProduct(id = id)
            ?: throw NoSuchElementException(CommandAppExceptionMessage.NOT_FOUND_PRODUCT.localizedMessage)
    }
}
