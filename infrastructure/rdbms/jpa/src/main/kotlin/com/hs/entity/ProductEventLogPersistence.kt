package com.hs.entity

import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Index
import javax.persistence.Table

@Entity
@Table(
    name = "product_event_log",
    indexes = [Index(name = "PRODUCT_EVENT_LOG_2_IX_PRODUCT", columnList = "productId")]
)
class ProductEventLogPersistence private constructor(
    productId: Long,
    eventStatus: Product.EventStatus,
    message: String
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    @Column(nullable = false)
    var productId: Long = productId
        protected set

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    var eventStatus: Product.EventStatus = eventStatus
        protected set

    @Column(nullable = false, length = 2500)
    var message: String = message
        protected set

    @Column(nullable = false)
    var createdDate: LocalDateTime = LocalDateTime.now()
        protected set

    override fun toString(): String {
        return "ProductEventLogPersistence(id=$id, productId=$productId, eventStatus=$eventStatus, " +
                "message=$message, createdDate=$createdDate)"
    }

    companion object {
        fun create(
            productId: Long,
            eventStatus: Product.EventStatus,
            message: String
        ): ProductEventLogPersistence {
            return ProductEventLogPersistence(
                productId = productId,
                eventStatus = eventStatus,
                message = message,
            )
        }
    }
}
