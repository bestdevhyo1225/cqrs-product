package com.hs.web

import com.hs.dto.CreateProductDto
import com.hs.dto.UpdateProductDto
import com.hs.response.SuccessResponse
import com.hs.application.usecase.ProductCommandProcessor
import com.hs.web.request.CreateProductRequest
import com.hs.web.request.UpdateProductConfirmRequest
import com.hs.web.request.UpdateProductRequest
import com.hs.web.request.UpdateProductStockRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import java.net.URI
import javax.validation.Valid

@RestController
@RequestMapping(value = ["/products"])
class CommandController(
    private val productCommandProcessor: ProductCommandProcessor
) {

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    fun create(@Valid @RequestBody request: CreateProductRequest): ResponseEntity<SuccessResponse<Any>> {
        val productId: Long? = productCommandProcessor.createProduct(
            createProductDto = CreateProductDto(
                name = request.name,
                price = request.price,
                stockQuantity = request.stockQuantity,
                imageUrls = request.imageUrls
            )
        )

        return ResponseEntity.created(URI.create("/products/$productId"))
            .body(SuccessResponse(status = "Created", data = object {
                val productId = productId
            }))
    }

    @PatchMapping(value = ["{id}"])
    fun update(@PathVariable(value = "id") productId: Long, @Valid @RequestBody request: UpdateProductRequest) {
        productCommandProcessor.updateProduct(
            updateProductDto = UpdateProductDto(
                id = productId,
                name = request.name,
                price = request.price,
                stockQuantity = request.stockQuantity
            )
        )
    }

    @PatchMapping(value = ["{id}/stock"])
    fun updateStock(
        @PathVariable(value = "id") productId: Long,
        @Valid @RequestBody request: UpdateProductStockRequest
    ) {
        productCommandProcessor.updateProductStock(
            id = productId,
            completeStockQuantity = request.completeStockQuantity
        )
    }

    @PatchMapping(value = ["{id}/confirm-status"])
    fun updateConfirmStatus(
        @PathVariable(value = "id") productId: Long,
        @Valid @RequestBody request: UpdateProductConfirmRequest
    ) {
        productCommandProcessor.updateProductConfirmStatus(
            id = productId,
            strProductConfirmStatus = request.comfirmStatus
        )
    }
}
