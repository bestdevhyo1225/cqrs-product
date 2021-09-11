package com.hs.adapter.web.request

import javax.validation.constraints.PositiveOrZero

data class UpdateProductStockRequest(
    @field:PositiveOrZero(message = "completeStockQuantity는 음수가 될 수 없습니다.")
    var completeStockQuantity: Int,
)
