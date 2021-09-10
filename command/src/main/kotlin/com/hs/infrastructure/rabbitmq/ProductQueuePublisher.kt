package com.hs.infrastructure.rabbitmq

interface ProductQueuePublisher {
    fun publish(productId: Long)
}
