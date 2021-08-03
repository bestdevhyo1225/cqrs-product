package com.hs.application.usecase

import com.hs.dto.CreateProductDto
import com.hs.dto.UpdateProductDto
import com.hs.entity.Product
import com.hs.entity.ProductImage
import com.hs.message.CommandAppExceptionMessage
import com.hs.data.jpa.ProductImageRepository
import com.hs.repository.ProductQueryRepository
import com.hs.data.jpa.ProductRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ProductCommand(
    private val publisher: ApplicationEventPublisher,
    private val productRepository: ProductRepository,
    private val productQueryRepository: ProductQueryRepository,
    private val productImageRepository: ProductImageRepository
) {

    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    fun create(createProductDto: CreateProductDto): Long? {
        logger.info("create() method is executed")

        val product = Product.create(
            name = createProductDto.name,
            price = createProductDto.price,
            stockQuantity = createProductDto.stockQuantity,
            imageUrls = createProductDto.imageUrls
        )

        productRepository.save(product)

        product.publishEventOfCreatedProduct(publisher = publisher)

        return product.id
    }

    fun update(updateProductDto: UpdateProductDto) {
        logger.info("update() method is executed")

        val product: Product = findProduct(id = updateProductDto.id)

        product.update(
            name = updateProductDto.name,
            price = updateProductDto.price,
            stockQuantity = updateProductDto.stockQuantity,
            publisher = publisher
        )
    }

    fun decreaseStockQuantity(id: Long, completeStockQuantity: Int) {
        logger.info("decreaseStockQuantity() method is executed")

        val product: Product = findProduct(id = id)

        product.decreaseStockCount(stockQuantity = completeStockQuantity, publisher = publisher)
    }

    fun changeConfirmStatus(id: Long, strProductConfirmStatus: String) {
        logger.info("changeConfirmStatus() method is executed")

        val product: Product = findProduct(id = id)

        product.changeConfirmStatus(strProductConfirmStatus = strProductConfirmStatus, publisher = publisher)
    }

    fun updateImage(id: Long, imageUrls: List<String>) {
        logger.info("updateImage() method is executed")

        val product: Product = findProductWithFetchJoin(id = id)

        productImageRepository.deleteByProductId(productId = product.id!!)

        productImageRepository.saveAll(ProductImage.createList(imageUrls = imageUrls, product = product))

        product.publishEventOfCreateImage(imageUrls = imageUrls, publisher = publisher)
    }

    fun findProduct(id: Long): Product {
        logger.info("findProduct() method is executed")

        return productRepository.findByIdOrNull(id = id)
            ?: throw NoSuchElementException(CommandAppExceptionMessage.NOT_FOUND_PRODUCT.localizedMessage)
    }

    fun findProductWithFetchJoin(id: Long): Product {
        logger.info("findProductWithFetchJoin() method is executed")

        return productQueryRepository.findProduct(id = id)
            ?: throw NoSuchElementException(CommandAppExceptionMessage.NOT_FOUND_PRODUCT.localizedMessage)
    }
}
