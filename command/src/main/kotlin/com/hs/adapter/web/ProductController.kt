package com.hs.adapter.web

import com.hs.dto.CreateProductDto
import com.hs.dto.UpdateProductDto
import com.hs.response.SuccessResponse
import com.hs.dto.FindProductDto
import com.hs.adapter.web.request.CreateProductRequest
import com.hs.adapter.web.request.UpdateProductConfirmRequest
import com.hs.adapter.web.request.UpdateProductImageRequest
import com.hs.adapter.web.request.UpdateProductRequest
import com.hs.adapter.web.request.UpdateProductStockRequest
import com.hs.application.usecase.ProductCommand
import com.hs.application.usecase.ProductQuery
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
class ProductController(
    private val productQuery: ProductQuery,
    private val productCommand: ProductCommand
) {

    @GetMapping(value = ["{id}"])
    fun find(@PathVariable(value = "id") productId: Long): ResponseEntity<SuccessResponse<FindProductDto>> {
        val product: FindProductDto = productQuery.findProduct(id = productId)
        return ResponseEntity.ok(SuccessResponse(data = product))
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    fun create(@Valid @RequestBody request: CreateProductRequest): ResponseEntity<SuccessResponse<Any>> {
        val productId: Long? = productCommand.create(
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
        productCommand.update(
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
    fun decreaseStockQuantity(
        @PathVariable(value = "id") productId: Long,
        @Valid @RequestBody request: UpdateProductStockRequest
    ): ResponseEntity<SuccessResponse<Any>> {
        productCommand.decreaseStockQuantity(id = productId, completeStockQuantity = request.completeStockQuantity)

        return ResponseEntity.ok(SuccessResponse(data = object {}))
    }

    @PatchMapping(value = ["{id}/confirm-status"])
    fun changeConfirmStatus(
        @PathVariable(value = "id") productId: Long,
        @Valid @RequestBody request: UpdateProductConfirmRequest
    ): ResponseEntity<SuccessResponse<Any>> {
        productCommand.changeConfirmStatus(id = productId, strProductConfirmStatus = request.confirmStatus)

        return ResponseEntity.ok(SuccessResponse(data = object {}))
    }

    @PatchMapping(value = ["{id}/images"])
    fun updateImages(
        @PathVariable(value = "id") productId: Long,
        @Valid @RequestBody request: UpdateProductImageRequest
    ): ResponseEntity<SuccessResponse<Any>> {
        productCommand.updateImage(id = productId, imageUrls = request.imageUrls)

        return ResponseEntity.ok(SuccessResponse(data = object {}))
    }
}
