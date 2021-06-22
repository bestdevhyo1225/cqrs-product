package com.hs.application.usecase.command

import com.hs.application.usecase.query.ProductQuery
import com.hs.dto.UpdateProductDto
import com.hs.entity.Product
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ProductUpdateCommandManager(
    private val productQuery: ProductQuery,
    private val productUpdateCommand: ProductUpdateCommand,
    private val productStockQuantityUpdateCommand: ProductStockQuantityUpdateCommand,
    private val productConfirmStatusUpdateCommand: ProductConfirmStatusUpdateCommand
) {

    fun update(updateProductDto: UpdateProductDto) {
        val product: Product = productQuery.findProduct(id = updateProductDto.id)

        productUpdateCommand.execute(updateProductDto = updateProductDto, product = product)
    }

    fun updateStockQuantity(id: Long, completeStockQuantity: Int) {
        val product: Product = productQuery.findProduct(id = id)

        productStockQuantityUpdateCommand.execute(id = id, completeStockQuantity = completeStockQuantity, product = product)
    }

    fun updateConfirmStatus(id: Long, strProductConfirmStatus: String) {
        val product: Product = productQuery.findProduct(id = id)

        productConfirmStatusUpdateCommand.execute(
            id = id,
            strProductConfirmStatus = strProductConfirmStatus,
            product = product
        )
    }
}
