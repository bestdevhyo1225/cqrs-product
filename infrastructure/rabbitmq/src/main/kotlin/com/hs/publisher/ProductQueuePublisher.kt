package com.hs.publisher

interface ProductQueuePublisher {
    fun publish(productId: Long)
}
