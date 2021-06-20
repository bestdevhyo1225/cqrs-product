package com.hs.web

import com.hs.entity.ProductAggregate
import com.hs.response.SuccessResponse
import com.hs.application.usecase.ProductAggregateQuery
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
class QueryController(
    private val productAggregateQuery: ProductAggregateQuery
) {

    @GetMapping(value = ["{id}"])
    fun findProductAggregate(
        @PathVariable(value = "id") @Min(value = 1, message = "1 이상을 입력해야 합니다.") productId: Long
    ): ResponseEntity<SuccessResponse<Any>> {
        val productAggregate: ProductAggregate = productAggregateQuery.findProductAggregate(productId = productId)
        return ResponseEntity.ok(SuccessResponse(data = productAggregate.data))
    }
}
