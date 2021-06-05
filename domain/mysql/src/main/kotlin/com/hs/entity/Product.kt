package com.hs.entity

import au.com.console.kassava.kotlinEquals
import au.com.console.kassava.kotlinHashCode
import au.com.console.kassava.kotlinToString
import com.hs.event.ProductEvent
import org.hibernate.annotations.DynamicUpdate
import org.springframework.context.ApplicationEventPublisher
import java.lang.IllegalStateException
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Column
import javax.persistence.OneToMany
import javax.persistence.CascadeType

@Entity
@DynamicUpdate
class Product(name: String, price: Int, stockCount: Int) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    @Column(nullable = false)
    var name: String = name
        protected set

    @Column(nullable = false)
    var price: Int = price
        protected set

    @Column(nullable = false)
    var stockCount: Int = stockCount
        protected set

    @Column(nullable = false, columnDefinition = "datetime")
    var createdDate: LocalDateTime = LocalDateTime.now()
        protected set

    @Column(nullable = false, columnDefinition = "datetime")
    var updatedDate: LocalDateTime = LocalDateTime.now()
        protected set

    @Column(nullable = true, columnDefinition = "datetime")
    var deletedDate: LocalDateTime? = null
        protected set

    @OneToMany(mappedBy = "product", cascade = [CascadeType.ALL])
    val productImages: MutableList<ProductImage> = mutableListOf()

    override fun toString() = kotlinToString(properties = toStringProperties)
    override fun equals(other: Any?) = kotlinEquals(other = other, properties = equalsAndHashCodeProperties)
    override fun hashCode() = kotlinHashCode(properties = equalsAndHashCodeProperties)

    companion object {
        private val equalsAndHashCodeProperties = arrayOf(Product::id)
        private val toStringProperties = arrayOf(
            Product::id,
            Product::name,
            Product::price,
            Product::stockCount,
            Product::createdDate,
            Product::updatedDate,
            Product::deletedDate
        )

        fun create(
            name: String,
            price: Int,
            stockCount: Int,
            productImages: List<ProductImage>
        ): Product {
            val product = Product(name = name, price = price, stockCount = stockCount)

            productImages.forEach { productImage -> product.addProductImage(productImage = productImage) }

            return product
        }
    }

    fun publishEventOfCreatedProduct(publisher: ApplicationEventPublisher) {
        publisher.publishEvent(ProductEvent(productId = this.id!!, commandCode = CommandCode.INSERT))
    }

    fun addProductImage(productImage: ProductImage) {
        productImages.add(productImage)
        productImage.changeProduct(this)
    }

    fun changeProduct(name: String, price: Int, stockCount: Int, publisher: ApplicationEventPublisher) {
        this.name = name
        this.price = price
        this.stockCount = stockCount
        this.updatedDate = LocalDateTime.now()

        publisher.publishEvent(ProductEvent(productId = this.id!!, commandCode = CommandCode.UPDATE))
    }

    fun changeStockCount(stockCount: Int, publisher: ApplicationEventPublisher) {
        if (this.stockCount - stockCount <= 0) throw IllegalStateException("재고 수량이 0개 이하입니다.")

        this.stockCount -= stockCount
        this.updatedDate = LocalDateTime.now()

        publisher.publishEvent(ProductEvent(productId = this.id!!, commandCode = CommandCode.UPDATE_STOCK))
    }
}
