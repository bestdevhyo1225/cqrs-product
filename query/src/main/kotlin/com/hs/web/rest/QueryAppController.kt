package com.hs.web.rest

import com.hs.application.usecase.ProductAggregateQueryProcessor
import com.hs.response.SuccessResponse
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.constraints.Min

@RestController
@RequestMapping(value = ["/query/products"])
@Validated
class QueryAppController(
    private val productAggregateQueryProcessor: ProductAggregateQueryProcessor
) {

    @GetMapping(value = ["{id}"])
    fun findProductAggregate(
        @PathVariable(value = "id") @Min(value = 1, message = "1 이상을 입력해야 합니다.") productId: Long
    ): ResponseEntity<SuccessResponse<Any>> {
        return ResponseEntity.ok(SuccessResponse(data = productAggregateQueryProcessor.findProductAggregate(productId = productId)))
    }
}
