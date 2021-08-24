package com.hs.adapter.rest

import com.hs.application.usecase.QueryBasedHandler
import com.hs.dto.FindProductDto
import com.hs.response.SuccessResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(value = ["/query-based"])
class QueryBasedController(private val queryBasedHandler: QueryBasedHandler) {

    @GetMapping(value = ["/products/{id}"])
    fun findProduct(@PathVariable(value = "id") productId: Long): ResponseEntity<SuccessResponse<FindProductDto>> {
        val product: FindProductDto = queryBasedHandler.findProduct(id = productId)
        return ResponseEntity.ok(SuccessResponse(data = product))
    }
}
