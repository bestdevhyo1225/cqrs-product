package com.hs.web

import com.hs.dto.CreateProductDto
import com.hs.dto.UpdateProductDto
import com.hs.usecase.CommandService
import com.hs.web.request.CreateProductRequest
import com.hs.web.request.UpdateProductRequest
import com.hs.web.request.UpdateProductStockRequest
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping(value = ["/products"])
class CommandController(
    private val commandService: CommandService
) {

    @PostMapping
    fun create(@Valid @RequestBody request: CreateProductRequest) {
        commandService.createProduct(
            createProductDto = CreateProductDto(
                name = request.name,
                price = request.price,
                stockCount = request.stockCount,
                imageUrls = request.imageUrls
            )
        )
    }

    @PatchMapping(value = ["{id}"])
    fun update(@PathVariable(value = "id") productId: Long, @Valid @RequestBody request: UpdateProductRequest) {
        commandService.updateProduct(
            updateProductDto = UpdateProductDto(
                id = productId,
                name = request.name,
                price = request.price,
                stockCount = request.stockCount
            )
        )
    }

    @PatchMapping(value = ["{id}/stock"])
    fun updateStock(
        @PathVariable(value = "id") productId: Long,
        @Valid @RequestBody request: UpdateProductStockRequest
    ) {
        commandService.updateProductStock(id = productId, completeStockCount = request.completeStockCount)
    }
}
