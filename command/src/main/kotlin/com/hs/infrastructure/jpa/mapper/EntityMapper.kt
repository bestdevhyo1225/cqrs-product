package com.hs.infrastructure.jpa.mapper

import com.hs.entity.ProductV2
import com.hs.infrastructure.jpa.persistence.ProductPersistence

class EntityMapper {
    companion object {
        fun toProductEntity(productPersistence: ProductPersistence?): ProductV2? {
            productPersistence ?: return null

            return ProductV2(
                id = productPersistence.id,
                name = productPersistence.name,
                price = productPersistence.price,
                stockQuantity = productPersistence.stockQuantity,
                imageUrls = productPersistence.productImages.map { it.url },
                confirmStatus = productPersistence.confirmStatus,
                createdDate = productPersistence.createdDate,
                updatedDate = productPersistence.updatedDate,
                deletedDate = productPersistence.deletedDate
            )
        }
    }
}
