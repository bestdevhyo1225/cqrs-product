package com.hs.unit

import com.hs.entity.ProductInfo
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("ProductInfo 단위 테스트")
class ProductInfoTest {

    @Test
    fun `ProductInfo는 생성 메서드를 지원한다`() {
        // given
        val productId = 1L
        val name = "상품 이름"
        val price = 100_000
        val confirmStatus = "WAIT"
        val stockQuantity = 50
        val imageUrls: List<String> = listOf("https://testImage.com")

        // when
        val productInfo = ProductInfo.create(
            id = productId,
            name = name,
            price = price,
            confirmStatus = confirmStatus,
            stockQuantity = stockQuantity,
            imageUrls = imageUrls
        )

        // then
        assertThat(productInfo.getId()).isEqualTo(productId)
        assertThat(productInfo.getName()).isEqualTo(name)
        assertThat(productInfo.getPrice()).isEqualTo(price)
        assertThat(productInfo.getIsDisplay()).isFalse
        assertThat(productInfo.getStockQuantity()).isEqualTo(stockQuantity)
        assertThat(productInfo.getProductImageUrls().first()).isEqualTo(imageUrls.first())
    }
}
