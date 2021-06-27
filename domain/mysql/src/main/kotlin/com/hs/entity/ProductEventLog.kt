package com.hs.entity

import au.com.console.kassava.kotlinEquals
import au.com.console.kassava.kotlinHashCode
import au.com.console.kassava.kotlinToString
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Column
import javax.persistence.Enumerated
import javax.persistence.EnumType
import javax.persistence.Index
import javax.persistence.Table

@Entity
@Table(indexes = [Index(name = "PRODUCT_EVENT_LOG_IX_PRODUCT", columnList = "productId")])
class ProductEventLog(productId: Long, productCommandCode: ProductCommandCode, message: String = "") {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    @Column(nullable = false)
    var productId: Long = productId
        protected set

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    var productCommandCode: ProductCommandCode = productCommandCode
        protected set

    @Column(nullable = false, length = 2500)
    var message: String = message
        protected set

    @Column(nullable = false)
    val createdDate: LocalDateTime = LocalDateTime.now()

    override fun toString() = kotlinToString(properties = toStringProperties)
    override fun equals(other: Any?) = kotlinEquals(other = other, properties = equalsAndHashCodeProperties)
    override fun hashCode() = kotlinHashCode(properties = equalsAndHashCodeProperties)

    companion object {
        private val equalsAndHashCodeProperties = arrayOf(ProductEventLog::id)
        private val toStringProperties = arrayOf(
            ProductEventLog::id,
            ProductEventLog::productId,
            ProductEventLog::productCommandCode,
            ProductEventLog::message,
            ProductEventLog::createdDate
        )
    }
}
