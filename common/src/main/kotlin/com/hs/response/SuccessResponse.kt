package com.hs.response

data class SuccessResponse<T>(
    val status: String = "success",
    val data: T
)
