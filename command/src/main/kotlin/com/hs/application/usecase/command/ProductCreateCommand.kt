package com.hs.application.usecase.command

import com.hs.dto.CreateProductDto
import com.hs.entity.Product
import com.hs.entity.ProductImage
import com.hs.repository.ProductRepository
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ProductCreateCommand(
    private val publisher: ApplicationEventPublisher,
    private val productRepository: ProductRepository
) {

    fun execute(createProductDto: CreateProductDto): Long? {
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
}
