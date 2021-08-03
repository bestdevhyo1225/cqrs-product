package com.hs.infrastructure.rabbitmq.event

data class PublishProductEvent(
    val productId: Long
)
