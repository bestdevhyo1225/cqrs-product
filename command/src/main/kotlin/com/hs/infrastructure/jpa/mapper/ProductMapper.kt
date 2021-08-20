package com.hs.infrastructure.jpa.mapper

import com.hs.entity.ProductV2
import com.hs.infrastructure.jpa.persistence.ProductPersistence

class ProductMapper {
    companion object {
        fun toDomainEntity(productPersistence: ProductPersistence?): ProductV2? {
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

        fun toPersistenceEntity(product: ProductV2): ProductPersistence {
            return ProductPersistence.create(
                name = product.name,
                price = product.price,
                stockQuantity = product.stockQuantity,
                imageUrls = product.imageUrls,
                confirmStatus = product.confirmStatus,
                createdDate = product.createdDate,
                updatedDate = product.updatedDate,
                deletedDate = product.deletedDate
            )
        }
    }
}
