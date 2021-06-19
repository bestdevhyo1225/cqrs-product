package com.hs.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "products")
class Product(productId: Long, data: String) {

    @Id
    val id: String? = null

    @Indexed(unique = true)
    var productId: Long = productId
        protected set

    var data: String = data
        protected set
}
