package com.hs.service

interface ProductQueuePublisher {
    fun publish(productId: Long)
}
