package com.hs.application.usecase

import com.hs.application.exception.ApplicationLayerException
import com.hs.dto.CreateProductDto
import com.hs.dto.UpdateProductDto
import com.hs.entity.ProductCommandCode
import com.hs.entity.Product
import com.hs.event.ProductChangeConfirmStatusEvent
import com.hs.event.ProductCreateAndUpdateEvent
import com.hs.event.ProductDecreaseStockQuantityEvent
import com.hs.event.ProductUpdateImageEvent
import com.hs.message.CommandAppExceptionMessage
import com.hs.repository.ProductRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.reactive.TransactionSynchronization.STATUS_ROLLED_BACK
import org.springframework.transaction.support.TransactionSynchronization
import org.springframework.transaction.support.TransactionSynchronizationManager

@Service
@Transactional
class ProductCommand(
    private val publisher: ApplicationEventPublisher,
    private val productRepository: ProductRepository
) {

    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    fun create(createProductDto: CreateProductDto): Long? {
        TransactionSynchronizationManager.registerSynchronization(
            object : TransactionSynchronization {
                override fun afterCompletion(status: Int) {
                    if (status == STATUS_ROLLED_BACK) logger.info("Save RollBack!!!")
                }
            }
        )

        val product: Product = productRepository.save(
            product = Product.create(
                name = createProductDto.name,
                price = createProductDto.price,
                stockQuantity = createProductDto.stockQuantity,
                imageUrls = createProductDto.imageUrls
            )
        )

        publishEvent(
            event = ProductCreateAndUpdateEvent(
                productId = product.id!!,
                productCommandCode = ProductCommandCode.INSERT,
                product = product
            )
        )

        return product.id
    }

    fun update(updateProductDto: UpdateProductDto) {
        val product: Product = findProduct(id = updateProductDto.id)

        product.update(
            name = updateProductDto.name,
            price = updateProductDto.price,
            stockQuantity = updateProductDto.stockQuantity,
        )

        productRepository.update(product = product)

        publishEvent(
            event = ProductCreateAndUpdateEvent(
                productId = product.id!!,
                productCommandCode = ProductCommandCode.UPDATE,
                product = product
            )
        )
    }

    fun decreaseStockQuantity(id: Long, completeStockQuantity: Int) {
        val product: Product = findProduct(id = id)

        product.decreaseStockCount(stockQuantity = completeStockQuantity)

        productRepository.updateStockQuantity(product = product)

        publishEvent(
            event = ProductDecreaseStockQuantityEvent(
                productId = product.id!!,
                productCommandCode = ProductCommandCode.DECREASE_STOCK_QUANTITY,
                currentStockQuantity = product.stockQuantity
            )
        )
    }

    fun changeConfirmStatus(id: Long, strProductConfirmStatus: String) {
        val confirmStatus: Product.ConfirmStatus =
            Product.convertFromStringTypeToEnumType(value = strProductConfirmStatus)

        val product: Product = findProduct(id = id)

        product.updateConfirmStatus(confirmStatus = confirmStatus)

        productRepository.updateConfirmStatus(product = product)

        publishEvent(
            event = ProductChangeConfirmStatusEvent(
                productId = product.id!!,
                productCommandCode = ProductCommandCode.CHANGE_CONFIRM_STATUS,
                confirmStatus = product.confirmStatus
            )
        )
    }

    fun updateImage(id: Long, imageUrls: List<String>) {
        val product: Product = findProductWithFetchJoin(id = id)

        productRepository.deleteImageByProductId(productId = product.id!!)
        productRepository.saveAllImage(product = product, imageUrls = imageUrls)

        product.updateConfirmStatus(confirmStatus = Product.ConfirmStatus.WAIT)
        productRepository.updateConfirmStatus(product = product)

        publishEvent(
            event = ProductUpdateImageEvent(
                productId = product.id!!,
                productCommandCode = ProductCommandCode.UPDATE_IMAGE,
                imageUrls = imageUrls
            )
        )
    }

    fun findProduct(id: Long): Product {
        return productRepository.findProduct(id = id)
            ?: throw NoSuchElementException(CommandAppExceptionMessage.NOT_FOUND_PRODUCT.localizedMessage)
    }

    fun findProductWithFetchJoin(id: Long): Product {
        return productRepository.findProductWithFetchJoin(id = id)
            ?: throw NoSuchElementException(CommandAppExceptionMessage.NOT_FOUND_PRODUCT.localizedMessage)
    }

    private fun publishEvent(event: Any) {
        try {
            publisher.publishEvent(event)
        } catch (exception: NullPointerException) {
            throw ApplicationLayerException(exceptionMessage = CommandAppExceptionMessage.PRODUCT_ID_IS_NULL)
        }
    }
}
