package com.hs.unit

import com.hs.vo.ProductAggregateId
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.assertj.core.api.Assertions.assertThat

@DisplayName("ProductAggregateId 단위 테스트")
class ProductAggregateIdTest {

    @Test
    fun `ProductAggregateId는 생성 메서드를 지원한다`() {
        // given
        val id = "12398dfdffd"
        val productId = 1L

        // when
        val productAggregateId = ProductAggregateId.create(id = id, productId = productId)

        // then
        assertThat(productAggregateId.getId()).isEqualTo(id)
        assertThat(productAggregateId.getProductId()).isEqualTo(productId)
    }

    @Test
    fun `ProductAggregateId는 영속화된 객체의 Id를 생성하는 메서드를 지원한다`() {
        // given
        val id = "123150df89gf"
        val productId = 1L

        // when
        val productAggregateId = ProductAggregateId.createAfterPersistence(id = id, productId = productId)

        // then
        assertThat(productAggregateId.getId()).isEqualTo(id)
        assertThat(productAggregateId.getProductId()).isEqualTo(productId)
    }
}
