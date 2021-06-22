package com.hs.application.usecase

import com.hs.dto.CreateProductDto
import com.hs.dto.UpdateProductDto
import com.hs.entity.Product
import com.hs.entity.ProductImage
import com.hs.repository.ProductRepository
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ProductCommand(
    private val publisher: ApplicationEventPublisher,
    private val productRepository: ProductRepository
) {

    fun create(createProductDto: CreateProductDto): Long? {
        val productImages: List<ProductImage> =
            createProductDto.imageUrls.map { imageUrl -> ProductImage(url = imageUrl) }

        val product = Product.create(
            name = createProductDto.name,
            price = createProductDto.price,
            stockQuantity = createProductDto.stockQuantity,
            productImages = productImages,
        )

        productRepository.save(product)

        product.publishEventOfCreatedProduct(publisher = publisher)

        return product.id
    }

    fun update(updateProductDto: UpdateProductDto, product: Product) {
        product.changeProduct(
            name = updateProductDto.name,
            price = updateProductDto.price,
            stockQuantity = updateProductDto.stockQuantity,
            publisher = publisher
        )
    }

    fun updateStockQuantity(id: Long, completeStockQuantity: Int, product: Product) {
        product.changeStockCount(stockQuantity = completeStockQuantity, publisher = publisher)
    }

    fun updateConfirmStatus(id: Long, strProductConfirmStatus: String, product: Product) {
        product.changeConfirmStatus(strProductConfirmStatus = strProductConfirmStatus, publisher = publisher)
    }
}
