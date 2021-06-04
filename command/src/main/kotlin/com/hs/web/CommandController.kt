package com.hs.web

import com.hs.dto.CreateProductDto
import com.hs.dto.UpdateProductDto
import com.hs.usecase.CommandService
import com.hs.web.request.CreateProductRequest
import com.hs.web.request.UpdateProductRequest
import com.hs.web.request.UpdateProductStockRequest
import com.hs.web.response.SuccessResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI
import javax.validation.Valid

@RestController
@RequestMapping(value = ["/products"])
class CommandController(
    private val commandService: CommandService
) {

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    fun create(@Valid @RequestBody request: CreateProductRequest): ResponseEntity<SuccessResponse> {
        val productId: Long? = commandService.createProduct(
            createProductDto = CreateProductDto(
                name = request.name,
                price = request.price,
                stockCount = request.stockCount,
                imageUrls = request.imageUrls
            )
        )

        return ResponseEntity.created(URI.create("/products/$productId"))
            .body(SuccessResponse(data = object {
                val productId = productId
            }))
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
