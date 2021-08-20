package com.hs.infrastructure.jpa.mapper

import com.hs.entity.Product
import com.hs.infrastructure.jpa.persistence.ProductPersistence

class ProductMapper {
    companion object {
        fun toDomainEntity(productPersistence: ProductPersistence?, usedFetchJoin: Boolean = false): Product? {
            productPersistence ?: return null

            return Product(
                id = productPersistence.id,
                name = productPersistence.name,
                price = productPersistence.price,
                stockQuantity = productPersistence.stockQuantity,
                imageUrls = if (usedFetchJoin) productPersistence.productImages.map { it.url } else listOf(),
                confirmStatus = productPersistence.confirmStatus,
                createdDate = productPersistence.createdDate,
                updatedDate = productPersistence.updatedDate,
                deletedDate = productPersistence.deletedDate
            )
        }

        fun toPersistenceEntity(product: Product): ProductPersistence {
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
