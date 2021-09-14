package com.hs.unit

import com.hs.vo.ProductImageUrls
import com.hs.vo.ProductInfo
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("ProductInfo 단위 테스트")
class ProductInfoTest {

    @Test
    fun `ProductInfo는 생성 메서드를 지원한다`() {
        // given
        val name = "상품 이름"
        val price = 100_000
        val stockQuantity = 50
        val productImageUrls = ProductImageUrls.create(productImageUrls = listOf("http://testImage.com"))

        // when
        val productInfo = ProductInfo.create(
            name = name,
            price = price,
            stockQuantity = stockQuantity,
            productImageUrls = productImageUrls
        )

        // then
        assertThat(productInfo.getName()).isEqualTo(name)
        assertThat(productInfo.getPrice()).isEqualTo(price)
        assertThat(productInfo.getStockQuantity()).isEqualTo(stockQuantity)
        assertThat(productInfo.getProductImageUrls().first()).isEqualTo(productImageUrls.getProductImageUrls().first())
    }
}
