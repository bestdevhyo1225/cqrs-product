package com.hs.adapter.rest

import com.hs.application.usecase.ProductAggregateQuery
import com.hs.response.SuccessResponse
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import javax.validation.constraints.Min

@RestController
@RequestMapping(value = ["/query/products"])
@Validated
class QueryAppController(
    private val productAggregateQuery: ProductAggregateQuery
) {

    @GetMapping
    fun findProductAggregatesWithPagination(
        @RequestParam(value = "page", defaultValue = "0") page: Int,
        @RequestParam(value = "pageSize", defaultValue = "10") pageSize: Int
    ): ResponseEntity<SuccessResponse<Any>> {
        return ResponseEntity.ok(
            SuccessResponse(
                data = productAggregateQuery.findProductAggregatesWithPagination(
                    page = page,
                    pageSize = pageSize
                )
            )
        )
    }

    @GetMapping(value = ["{id}"])
    fun findProductAggregate(
        @PathVariable(value = "id") @Min(value = 1, message = "1 이상을 입력해야 합니다.") productId: Long
    ): ResponseEntity<SuccessResponse<Any>> {
        return ResponseEntity.ok(SuccessResponse(data = productAggregateQuery.findProductAggregate(productId = productId)))
    }
}
