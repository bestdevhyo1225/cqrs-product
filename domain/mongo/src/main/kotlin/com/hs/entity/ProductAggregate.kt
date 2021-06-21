package com.hs.entity

import au.com.console.kassava.kotlinEquals
import au.com.console.kassava.kotlinHashCode
import au.com.console.kassava.kotlinToString
import com.hs.dto.FindProductAggregateDto
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.CompoundIndex
import org.springframework.data.mongodb.core.index.CompoundIndexes
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Document(collection = "product_aggregates")
@CompoundIndexes(
    value = [
        CompoundIndex(
            name = "PRODUCT_AGGREGATES_IX_SEARCH",
            def = "{'productId': 1, 'type': 1, 'isDisplay': 1}",
            unique = true
        )
    ]
)
class ProductAggregate(productId: Long, type: ProductAggregateType, isDisplay: String, data: FindProductAggregateDto) {

    @Id
    var id: String? = null
        protected set

    var productId: Long = productId
        protected set

    var type: ProductAggregateType = type
        protected set

    var isDisplay: String = isDisplay
        protected set

    var data: FindProductAggregateDto = data
        protected set

    var createdDatetime: String = LocalDateTime.now().format(DATETIME_FORMATTER).toString()
        protected set

    var updatedDatetime: String = LocalDateTime.now().format(DATETIME_FORMATTER).toString()
        protected set

    override fun toString() = kotlinToString(properties = toStringProperties)
    override fun equals(other: Any?) = kotlinEquals(other = other, properties = equalsAndHashCodeProperties)
    override fun hashCode() = kotlinHashCode(properties = equalsAndHashCodeProperties)

    companion object {
        private val DATETIME_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        private val equalsAndHashCodeProperties = arrayOf(ProductAggregate::id)
        private val toStringProperties = arrayOf(
            ProductAggregate::id,
            ProductAggregate::productId,
            ProductAggregate::type,
            ProductAggregate::isDisplay,
            ProductAggregate::data,
            ProductAggregate::createdDatetime,
            ProductAggregate::updatedDatetime,
        )

        fun create(productAggregateDto: FindProductAggregateDto, type: ProductAggregateType): ProductAggregate {
            return ProductAggregate(
                productId = productAggregateDto.productId,
                type = type,
                isDisplay = (productAggregateDto.confirmStatus == "APPROVE").toString(),
                data = productAggregateDto
            )
        }
    }

    fun changeProductAggregateData(data: FindProductAggregateDto) {
        this.data = data
        this.isDisplay = (data.confirmStatus == "APPROVE").toString()
        this.updatedDatetime = LocalDateTime.now().format(DATETIME_FORMATTER).toString()
    }
}
