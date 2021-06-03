package com.hs.entity

import au.com.console.kassava.kotlinEquals
import au.com.console.kassava.kotlinHashCode
import au.com.console.kassava.kotlinToString
import com.hs.event.ChangeProductEvent
import com.hs.event.ChangeProductStockEvent
import com.hs.event.CreateProductAggregateEvent
import org.hibernate.annotations.DynamicUpdate
import org.springframework.context.ApplicationEventPublisher
import java.lang.IllegalStateException
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

    @OneToMany(mappedBy = "product", cascade = [CascadeType.ALL])
    val productImages: MutableList<ProductImage> = mutableListOf()

    override fun toString() = kotlinToString(properties = toStringProperties)
    override fun equals(other: Any?) = kotlinEquals(other = other, properties = equalsAndHashCodeProperties)
    override fun hashCode() = kotlinHashCode(properties = equalsAndHashCodeProperties)

    companion object {
        private val equalsAndHashCodeProperties = arrayOf(Product::id)
        private val toStringProperties = arrayOf(Product::id, Product::name, Product::price, Product::stockCount)

        fun create(
            name: String,
            price: Int,
            stockCount: Int,
            productImages: List<ProductImage>,
            publisher: ApplicationEventPublisher
        ): Product {
            val product = Product(name = name, price = price, stockCount = stockCount)

            val productImageUrls: MutableList<String> = mutableListOf()

            productImages.forEach { productImage ->
                product.addProductImage(productImage = productImage)
                productImageUrls.add(productImage.url)
            }

            publisher.publishEvent(
                CreateProductAggregateEvent(
                    name = name,
                    price = price,
                    stockCount = stockCount,
                    productImageUrls = productImageUrls
                )
            )

            return product
        }
    }

    fun addProductImage(productImage: ProductImage) {
        productImages.add(productImage)
        productImage.changeProduct(this)
    }

    fun changeProduct(name: String, price: Int, stockCount: Int, publisher: ApplicationEventPublisher) {
        this.name = name
        this.price = price
        this.stockCount = stockCount

        publisher.publishEvent(ChangeProductEvent(name = name, price = price, stockCount = stockCount))
    }

    fun changeStockCount(stockCount: Int, publisher: ApplicationEventPublisher) {
        if (this.stockCount - stockCount <= 0) throw IllegalStateException("재고 수량이 0개 이하입니다.")

        this.stockCount -= stockCount

        publisher.publishEvent(ChangeProductStockEvent(stockCount = this.stockCount))
    }
}
