package com.hs.web

import com.hs.dto.FindProductDto
import com.hs.response.SuccessResponse
import com.hs.application.usecase.QueryService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(value = ["/products"])
class QueryController(
    private val queryService: QueryService
) {

    @GetMapping(value = ["{id}"])
    fun find(@PathVariable(value = "id") productId: Long): ResponseEntity<SuccessResponse<FindProductDto>> {
        val product: FindProductDto = queryService.findProductAggregate(id = productId)
        return ResponseEntity.ok(SuccessResponse(data = product))
    }
}
