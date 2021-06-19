package com.hs.response

data class SuccessResponse<T>(
    val status: String = "Success",
    val data: T
)
