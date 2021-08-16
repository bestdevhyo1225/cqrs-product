package com.hs.data.jpa.repository

import com.hs.entity.ProductImage
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface ProductImageSpringDataJpaRepository : JpaRepository<ProductImage, Long> {

    /*
    * @Modifying은 INSERT, UPDATE, DELETE 쿼리에서 사용하고, 주로 벌크 연산시 적용하는 어노테이션이다.
    * - clearAutomatically 옵션은 영속성 컨텍스트를 Clear 할 것인지를 지정하는 속성이다.
    * */
    @Modifying
    @Query(value = "DELETE FROM ProductImage pi WHERE pi.product.id = :productId")
    fun deleteByProductId(productId: Long)
}
