package com.hs.infrastructure.jpa.persistence

import org.hibernate.annotations.DynamicUpdate
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@DynamicUpdate
@Table(name = "product_image_2")
class ProductImagePersistence(url: String, product: ProductPersistence) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    @Column(nullable = false)
    var url: String = url
        protected set

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    var product: ProductPersistence = product
        protected set

    override fun toString(): String {
        return "ProductImagePersistence(id=$id, url=$url)"
    }

    fun changeProduct(product: ProductPersistence) {
        this.product = product
    }
}
