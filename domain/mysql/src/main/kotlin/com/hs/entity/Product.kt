package com.hs.entity

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

    override fun toString(): String {
        return "Product(id=$id, name=$name, price=$price, stockQuantity=$stockQuantity, confirmStatus=$confirmStatus, " +
                "createdDate=$createdDate, updatedDate=$updatedDate, deletedDate=$deletedDate)"
    }

    companion object {
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

    fun decreaseStockCount(stockQuantity: Int) {
        if (this.stockQuantity - stockQuantity <= 0) {
            throw DomainMySqlException(
                exceptionMessage = CommandAppExceptionMessage.HAVE_EXCEEDED_THE_QUANTITY_AVAILABLE_FOR_PURCHASE
            )
        }

        this.stockQuantity -= stockQuantity
        this.updatedDate = LocalDateTime.now()
    }

    fun update(name: String, price: Int, stockQuantity: Int) {
        this.name = name
        this.price = price
        this.stockQuantity = stockQuantity
        this.confirmStatus = ProductConfirmStatus.WAIT
        this.updatedDate = LocalDateTime.now()
    }

    fun updateConfirmStatus(confirmStatus: ProductConfirmStatus) {
        this.confirmStatus = confirmStatus
        this.updatedDate = LocalDateTime.now()
    }
}
