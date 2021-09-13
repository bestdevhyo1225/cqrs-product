package com.hs.unit

import com.hs.entity.ProductAggregate
import com.hs.vo.ProductImageUrls
import com.hs.vo.ProductInfo
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

@DisplayName("ProductAggregate 단위 테스트")
class ProductAggregateTest {

    @ParameterizedTest
    @CsvSource(value = ["APPROVE, true", "WAIT, false", "REJECT, false"])
    fun `ProductAggregate는 생성 메서드를 지원한다`(confirmStatus: String, isDisplay: Boolean) {
        // given
        val productId = 1L
        val name = "상품 이름"
        val price = 10_000
        val stockQuantity = 20
        val imageUrls: List<String> = listOf("test1")

        // when
        val productAggregate = ProductAggregate.create(
            productId = productId,
            name = name,
            price = price,
            stockQuantity = stockQuantity,
            imageUrls = imageUrls,
            confirmStatus = confirmStatus
        )

        // then
        assertThat(productAggregate.productAggregateId.getId()).isNull()
        assertThat(productAggregate.productAggregateId.getProductId()).isEqualTo(productId)
        assertThat(productAggregate.productInfo.getName()).isEqualTo(name)
        assertThat(productAggregate.productInfo.getPrice()).isEqualTo(price)
        assertThat(productAggregate.productInfo.getStockQuantity()).isEqualTo(stockQuantity)
        assertThat(productAggregate.productInfo.getProductImageUrls()).hasSize(imageUrls.size)
        assertThat(productAggregate.productInfo.getProductImageUrls().first()).isEqualTo(imageUrls.first())
        assertThat(productAggregate.isDisplay).isEqualTo(isDisplay)
    }

    @Test
    fun `ProductAggregate는 외부에서 들어오는 특정 데이터를 통해 ProductAggregate 인스턴스로 변환하는 메서드를 제공한다`() {
        // given
        val id = "id1234567"
        val productId = 1L
        val productImageUrls = ProductImageUrls.create(productImageUrls = listOf("http://test123124.com"))
        val productInfo =
            ProductInfo.create(name = "상품 이름", price = 21_000, stockQuantity = 30, productImageUrls = productImageUrls)
        val isDisplay = true
        val createdDatetime = "2021-10-10 00:30:30"
        val updatedDatetime = "2021-11-11 12:56:28"

        // when
        val productAggregate = ProductAggregate.mapOf(
            id = id,
            productId = productId,
            productInfo = productInfo,
            isDisplay = isDisplay,
            createdDatetime = createdDatetime,
            updatedDatetime = updatedDatetime
        )

        // then
        assertThat(productAggregate.productAggregateId.getId()).isEqualTo(id)
        assertThat(productAggregate.productAggregateId.getProductId()).isEqualTo(productId)
        assertThat(productAggregate.productInfo.getName()).isEqualTo(productInfo.getName())
        assertThat(productAggregate.productInfo.getPrice()).isEqualTo(productInfo.getPrice())
        assertThat(productAggregate.productInfo.getStockQuantity()).isEqualTo(productInfo.getStockQuantity())
        assertThat(productAggregate.productInfo.getProductImageUrls()).hasSize(productInfo.getProductImageUrls().size)
        assertThat(
            productAggregate.productInfo.getProductImageUrls().first()
        ).isEqualTo(productInfo.getProductImageUrls().first())
        assertThat(productAggregate.isDisplay).isEqualTo(isDisplay)
        assertThat(productAggregate.productDatetime.getStringCreatedDatetime()).isEqualTo(createdDatetime)
        assertThat(productAggregate.productDatetime.getStringUpdatedDatetime()).isEqualTo(updatedDatetime)
    }

    @ParameterizedTest
    @CsvSource(value = ["APPROVE, true", "WAIT, false", "REJECT, false"])
    fun `ProductAggregate는 변경하는 메서드를 제공한다`(confirmStatus: String, isDisplay: Boolean) {
        // given
        val productId = 1L
        val name = "상품 이름"
        val price = 10_000
        val stockQuantity = 20
        val imageUrls: List<String> = listOf("test1")
        val productAggregate = ProductAggregate.create(
            productId = productId,
            name = name,
            price = price,
            stockQuantity = stockQuantity,
            imageUrls = imageUrls,
            confirmStatus = confirmStatus
        )
        val changeName = "상품 이름 변경"
        val changePrice = 15_000
        val changeStockQuantity = 50
        val changeImageUrls: List<String> = listOf("changeImageUrls")

        // when
        productAggregate.changeProductAggregateData(
            name = changeName,
            price = changePrice,
            stockQuantity = changeStockQuantity,
            imageUrls = changeImageUrls,
            confirmStatus = confirmStatus
        )

        // then
        assertThat(productAggregate.productAggregateId.getProductId()).isEqualTo(productId)
        assertThat(productAggregate.productInfo.getName()).isEqualTo(changeName)
        assertThat(productAggregate.productInfo.getPrice()).isEqualTo(changePrice)
        assertThat(productAggregate.productInfo.getStockQuantity()).isEqualTo(changeStockQuantity)
        assertThat(productAggregate.productInfo.getProductImageUrls().first()).isEqualTo(changeImageUrls.first())
        assertThat(productAggregate.isDisplay).isEqualTo(isDisplay)
    }

    @Test
    fun `ProductAggregate는 영속화 객체가 저장된 후, 영속화 객체의 Id를 매핑할 수 있는 메서드를 제공한다`() {
        // given
        val id = "1234asjdf!@@#fdsjf"
        val productId = 1L
        val name = "상품 이름"
        val price = 10_000
        val stockQuantity = 20
        val imageUrls: List<String> = listOf("test1")
        val confirmStatus = "APPROVE"
        val productAggregate = ProductAggregate.create(
            productId = productId,
            name = name,
            price = price,
            stockQuantity = stockQuantity,
            imageUrls = imageUrls,
            confirmStatus = confirmStatus
        )

        // when
        productAggregate.reflectIdAfterPersistence(id = id)

        // then
        assertThat(productAggregate.productAggregateId.getId()).isEqualTo(id)
    }
}
