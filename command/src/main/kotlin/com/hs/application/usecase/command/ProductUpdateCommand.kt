package com.hs.application.usecase.command

import com.hs.dto.UpdateProductDto
import com.hs.entity.Product
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ProductUpdateCommand(
    private val publisher: ApplicationEventPublisher
) {

    fun execute(updateProductDto: UpdateProductDto, product: Product) {
        product.changeProduct(
            name = updateProductDto.name,
            price = updateProductDto.price,
            stockQuantity = updateProductDto.stockQuantity,
            publisher = publisher
        )
    }
}
