package com.hs.usecase

import com.hs.dto.CreateProductDto
import com.hs.dto.UpdateProductDto
import com.hs.entity.Product
import com.hs.entity.ProductConfirmStatus
import com.hs.entity.ProductImage
import com.hs.repository.ProductRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class CommandService(
    private val publisher: ApplicationEventPublisher,
    private val productRepository: ProductRepository
) {

    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    fun createProduct(createProductDto: CreateProductDto): Long? {
        logger.info("[ Service - createProduct() ] Start")

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

        logger.info("[ Service - createProduct() ] End")

        return product.id
    }

    fun updateProduct(updateProductDto: UpdateProductDto) {
        logger.info("[ Service - updateProduct() ] Start")

        val product: Product = findProduct(id = updateProductDto.id)

        product.changeProduct(
            name = updateProductDto.name,
            price = updateProductDto.price,
            stockQuantity = updateProductDto.stockQuantity,
            publisher = publisher
        )

        logger.info("[ Service - updateProduct() ] End")
    }

    fun updateProductStock(id: Long, completeStockQuantity: Int) {
        logger.info("[ Service - updateProductStock() ] Start")

        val product: Product = findProduct(id = id)

        product.changeStockCount(stockQuantity = completeStockQuantity, publisher = publisher)

        logger.info("[ Service - updateProductStock() ] End")
    }

    fun updateProductConfirmStatus(id: Long, strProductConfirmStatus: String) {
        logger.info("[ Service - updateProductConfirmStatus() ] Start")

        val product: Product = findProduct(id = id)

        product.changeConfirmStatus(strProductConfirmStatus = strProductConfirmStatus, publisher = publisher)

        logger.info("[ Service - updateProductConfirmStatus() ] End")
    }

    private fun findProduct(id: Long): Product {
        return productRepository.findByIdOrNull(id = id) ?: throw NoSuchElementException("해당 상품이 존재하지 않습니다.")
    }
}
