package com.hs.service

import com.hs.dto.PublishProductDto

interface ProductQueuePublisher {
    fun publish(publishProductDto: PublishProductDto)
}
