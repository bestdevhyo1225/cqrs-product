package com.hs.entity

import org.hibernate.annotations.DynamicUpdate
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
class ProductImage(url: String, product: Product) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    @Column(nullable = false)
    var url: String = url
        protected set

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    var product: Product = product
        protected set

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ProductImage

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

    override fun toString(): String {
        return "ProductImage(id=$id, url=$url)"
    }

    companion object {
        fun createList(imageUrls: List<String>, product: Product): List<ProductImage> {
            return imageUrls.map { imageUrl -> ProductImage(url = imageUrl, product = product) }
        }
    }
}
