package com.hs.entity

import au.com.console.kassava.kotlinEquals
import au.com.console.kassava.kotlinHashCode
import au.com.console.kassava.kotlinToString
import com.hs.event.ProductUpdateImageEvent
import com.hs.exception.DomainMySqlException
import com.hs.message.CommandAppExceptionMessage
import org.hibernate.annotations.DynamicUpdate
import org.springframework.context.ApplicationEventPublisher
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Column
import javax.persistence.ManyToOne
import javax.persistence.FetchType
import javax.persistence.JoinColumn

@Entity
@DynamicUpdate
class ProductImage(url: String) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    @Column(nullable = false)
    var url: String = url
        protected set

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    var product: Product? = null
        protected set

    override fun toString() = kotlinToString(properties = toStringProperties)
    override fun equals(other: Any?) = kotlinEquals(other = other, properties = equalsAndHashCodeProperties)
    override fun hashCode() = kotlinHashCode(properties = equalsAndHashCodeProperties)

    companion object {
        private val equalsAndHashCodeProperties = arrayOf(ProductImage::id)
        private val toStringProperties = arrayOf(ProductImage::id, ProductImage::url)

        fun create(imageUrls: List<String>, product: Product): List<ProductImage> {
            return imageUrls.map { imageUrl ->
                val productImage = ProductImage(url = imageUrl)

                productImage.changeProduct(product = product)

                productImage
            }
        }
    }

    fun changeProduct(product: Product) {
        this.product = product
    }
}
