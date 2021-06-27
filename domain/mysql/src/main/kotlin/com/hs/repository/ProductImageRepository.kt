package com.hs.repository

import com.hs.entity.ProductImage
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface ProductImageRepository : JpaRepository<ProductImage, Long> {

    /*
    * @Modifying은 수정, 삭제애서 벌크 연산시 추가해야 하는 어노테이션이다.
    * - clearAutomatically 옵션은 영속성 컨텍스트를 Clear 할 것인지를 지정하는 속성이다.
    * */
    @Modifying
    @Query(value = "DELETE FROM ProductImage pi WHERE pi.product.id = :productId")
    fun deleteByProductId(productId: Long)
}
