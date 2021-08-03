package com.hs.entity

import au.com.console.kassava.kotlinEquals
import au.com.console.kassava.kotlinHashCode
import au.com.console.kassava.kotlinToString
import com.hs.exception.DomainMySqlException
import com.hs.message.CommandAppExceptionMessage
import org.hibernate.annotations.DynamicUpdate
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Column
import javax.persistence.OneToMany
import javax.persistence.CascadeType
import javax.persistence.EnumType
import javax.persistence.Enumerated

@Entity
@DynamicUpdate
class Product(name: String, price: Int, stockQuantity: Int) {

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
    var stockQuantity: Int = stockQuantity
        protected set

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var confirmStatus: ProductConfirmStatus = ProductConfirmStatus.WAIT
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
            Product::stockQuantity,
            Product::confirmStatus,
            Product::createdDate,
            Product::updatedDate,
            Product::deletedDate
        )

        fun create(
            name: String,
            price: Int,
            stockQuantity: Int,
            imageUrls: List<String>
        ): Product {
            val product = Product(name = name, price = price, stockQuantity = stockQuantity)

            imageUrls.map { imageUrl -> ProductImage(url = imageUrl, product = product) }
                .forEach { productImage -> product.addProductImage(productImage = productImage) }

            return product
        }
    }

    fun addProductImage(productImage: ProductImage) {
        productImages.add(productImage)
    }

    fun updateImage() {
        this.confirmStatus = ProductConfirmStatus.WAIT
        this.updatedDate = LocalDateTime.now()
    }

    fun update(name: String, price: Int, stockQuantity: Int) {
        this.name = name
        this.price = price
        this.stockQuantity = stockQuantity
        this.confirmStatus = ProductConfirmStatus.WAIT
        this.updatedDate = LocalDateTime.now()
    }

    fun decreaseStockCount(stockQuantity: Int) {
        if (this.stockQuantity - stockQuantity <= 0) {
            throw DomainMySqlException(
                exceptionMessage = CommandAppExceptionMessage.HAVE_EXCEEDED_THE_QUANTITY_AVAILABLE_FOR_PURCHASE
            )
        }

        this.stockQuantity -= stockQuantity
        this.updatedDate = LocalDateTime.now()
    }

    fun changeConfirmStatus(strProductConfirmStatus: String) {
        val confirmStatus: ProductConfirmStatus =
            ProductConfirmStatus.convertFromStringToProductConfirmStatus(value = strProductConfirmStatus)

        this.confirmStatus = confirmStatus
        this.updatedDate = LocalDateTime.now()
    }
}
