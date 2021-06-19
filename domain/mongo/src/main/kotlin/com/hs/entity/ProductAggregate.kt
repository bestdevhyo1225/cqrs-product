package com.hs.entity

import au.com.console.kassava.kotlinEquals
import au.com.console.kassava.kotlinHashCode
import au.com.console.kassava.kotlinToString
import com.hs.dto.FindProductAggregateDto
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.CompoundIndex
import org.springframework.data.mongodb.core.index.CompoundIndexes
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "product_aggregates")
@CompoundIndexes(
    value = [
        CompoundIndex(name = "PRODUCT_AGGREGATES_IX_SEARCH", def = "{'productId': 1, 'type': 1}", unique = true)
    ]
)
class ProductAggregate(productId: Long, type: ProductAggregateType, data: FindProductAggregateDto) {

    @Id
    var id: String? = null
        protected set

    var productId: Long = productId
        protected set

    var type: ProductAggregateType = type
        protected set

    var data: FindProductAggregateDto = data
        protected set

    override fun toString() = kotlinToString(properties = toStringProperties)
    override fun equals(other: Any?) = kotlinEquals(other = other, properties = equalsAndHashCodeProperties)
    override fun hashCode() = kotlinHashCode(properties = equalsAndHashCodeProperties)

    companion object {
        private val equalsAndHashCodeProperties = arrayOf(ProductAggregate::id)
        private val toStringProperties = arrayOf(
            ProductAggregate::id,
            ProductAggregate::productId,
            ProductAggregate::type,
            ProductAggregate::data,
        )

        fun create(productAggregateDto: FindProductAggregateDto, type: ProductAggregateType): ProductAggregate {
            return ProductAggregate(
                productId = productAggregateDto.productId,
                type = type,
                data = productAggregateDto
            )
        }
    }

    fun changeProductAggregateData(data: FindProductAggregateDto) {
        this.data = data
    }
}
