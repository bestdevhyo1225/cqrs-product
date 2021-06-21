package com.hs.publisher

interface ProductQueuePublisher {
    suspend fun publish(body: ByteArray)
}
