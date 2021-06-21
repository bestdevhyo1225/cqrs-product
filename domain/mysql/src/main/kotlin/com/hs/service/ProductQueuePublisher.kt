package com.hs.service

interface ProductQueuePublisher {
    suspend fun publish(body: ByteArray)
}
