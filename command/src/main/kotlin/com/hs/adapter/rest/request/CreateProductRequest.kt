package com.hs.adapter.rest.request

import javax.validation.constraints.NotBlank
import javax.validation.constraints.Positive
import javax.validation.constraints.PositiveOrZero

data class CreateProductRequest(
    @field:NotBlank(message = "name은 비어 있을 수 없습니다.")
    var name: String,

    @field:Positive(message = "price는 0 또는 음수가 될 수 없습니다.")
    var price: Int,

    @field:PositiveOrZero(message = "stockQuantity는 음수가 될 수 없습니다.")
    var stockQuantity: Int,

    var imageUrls: List<String>
)
