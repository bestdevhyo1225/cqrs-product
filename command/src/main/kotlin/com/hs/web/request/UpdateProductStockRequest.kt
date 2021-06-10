package com.hs.web.request

import javax.validation.constraints.PositiveOrZero

data class UpdateProductStockRequest(
    @get:PositiveOrZero(message = "completeStockQuantity는 음수가 될 수 없습니다.")
    var completeStockQuantity: Int,
)
