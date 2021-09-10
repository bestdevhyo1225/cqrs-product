package com.hs.infrastructure.jpa.persistence

import com.hs.entity.Product
import org.hibernate.annotations.DynamicUpdate
import java.time.LocalDateTime
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@DynamicUpdate
@Table(name = "product")
class ProductPersistence private constructor(
    name: String,
    price: Int,
    stockQuantity: Int,
    confirmStatus: Product.ConfirmStatus,
    createdDate: LocalDateTime,
    updatedDate: LocalDateTime,
    deletedDate: LocalDateTime?
) {

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
    var confirmStatus: Product.ConfirmStatus = confirmStatus
        protected set

    @Column(nullable = false, columnDefinition = "datetime")
    var createdDate: LocalDateTime = createdDate
        protected set

    @Column(nullable = false, columnDefinition = "datetime")
    var updatedDate: LocalDateTime = updatedDate
        protected set

    @Column(nullable = true, columnDefinition = "datetime")
    var deletedDate: LocalDateTime? = deletedDate
        protected set

    @OneToMany(mappedBy = "product", cascade = [CascadeType.ALL])
    val productImages: MutableList<ProductImagePersistence> = mutableListOf()

    override fun toString(): String {
        return "ProductPersistence(" +
                "id=$id, name=$name, price=$price, stockQuantity=$stockQuantity, confirmStatus=$confirmStatus, " +
                "createdDate=$createdDate, updatedDate=$updatedDate, deletedDate=$deletedDate" +
                ")"
    }

    companion object {
        fun create(
            name: String,
            price: Int,
            stockQuantity: Int,
            imageUrls: List<String>,
            confirmStatus: Product.ConfirmStatus,
            createdDate: LocalDateTime,
            updatedDate: LocalDateTime,
            deletedDate: LocalDateTime?
        ): ProductPersistence {
            val product = ProductPersistence(
                name = name,
                price = price,
                stockQuantity = stockQuantity,
                confirmStatus = confirmStatus,
                createdDate = createdDate,
                updatedDate = updatedDate,
                deletedDate = deletedDate
            )

            imageUrls.map { imageUrl -> ProductImagePersistence.create(url = imageUrl, product = product) }
                .forEach { productImage -> product.addProductImage(productImage = productImage) }

            return product
        }
    }

    fun addProductImage(productImage: ProductImagePersistence) {
        productImages.add(productImage)
        productImage.changeProduct(this)
    }

    fun update(name: String, price: Int, stockQuantity: Int) {
        this.name = name
        this.price = price
        this.stockQuantity = stockQuantity
        this.confirmStatus = Product.ConfirmStatus.WAIT
        this.updatedDate = LocalDateTime.now()
    }

    fun decreaseStockCount(stockQuantity: Int) {
        this.stockQuantity = stockQuantity
        this.updatedDate = LocalDateTime.now()
    }

    fun updateConfirmStatus(confirmStatus: Product.ConfirmStatus) {
        this.confirmStatus = confirmStatus
        this.updatedDate = LocalDateTime.now()
    }

    fun createImageUrls(): List<String> {
        return productImages.map { it.url }
    }
}
