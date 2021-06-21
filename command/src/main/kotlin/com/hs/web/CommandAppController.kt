package com.hs.web

import com.hs.dto.CreateProductDto
import com.hs.dto.UpdateProductDto
import com.hs.response.SuccessResponse
import com.hs.application.usecase.ProductCommandProcessor
import com.hs.application.usecase.ProductQueryProcessor
import com.hs.dto.FindProductDto
import com.hs.web.request.CreateProductRequest
import com.hs.web.request.UpdateProductConfirmRequest
import com.hs.web.request.UpdateProductRequest
import com.hs.web.request.UpdateProductStockRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
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
class CommandAppController(
    private val productQueryProcessor: ProductQueryProcessor,
    private val productCommandProcessor: ProductCommandProcessor,
) {

    @GetMapping(value = ["{id}"])
    fun find(@PathVariable(value = "id") productId: Long): ResponseEntity<SuccessResponse<FindProductDto>> {
        val product: FindProductDto = productQueryProcessor.findProductAggregate(id = productId)
        return ResponseEntity.ok(SuccessResponse(data = product))
    }

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
    fun update(
        @PathVariable(value = "id") productId: Long,
        @Valid @RequestBody request: UpdateProductRequest
    ): ResponseEntity<SuccessResponse<Any>> {
        productCommandProcessor.updateProduct(
            updateProductDto = UpdateProductDto(
                id = productId,
                name = request.name,
                price = request.price,
                stockQuantity = request.stockQuantity
            )
        )

        return ResponseEntity.ok(SuccessResponse(data = object {}))
    }

    @PatchMapping(value = ["{id}/stock"])
    fun updateStock(
        @PathVariable(value = "id") productId: Long,
        @Valid @RequestBody request: UpdateProductStockRequest
    ): ResponseEntity<SuccessResponse<Any>> {
        productCommandProcessor.updateProductStock(
            id = productId,
            completeStockQuantity = request.completeStockQuantity
        )

        return ResponseEntity.ok(SuccessResponse(data = object {}))
    }

    @PatchMapping(value = ["{id}/confirm-status"])
    fun updateConfirmStatus(
        @PathVariable(value = "id") productId: Long,
        @Valid @RequestBody request: UpdateProductConfirmRequest
    ): ResponseEntity<SuccessResponse<Any>> {
        productCommandProcessor.updateProductConfirmStatus(
            id = productId,
            strProductConfirmStatus = request.comfirmStatus
        )

        return ResponseEntity.ok(SuccessResponse(data = object {}))
    }
}
