package com.hs.unit

import com.hs.entity.Product
import com.hs.entity.ProductConfirmStatus
import com.hs.exception.DomainMySqlException
import com.hs.message.CommandAppExceptionMessage
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import java.time.LocalDateTime

@DisplayName("Product 단위 테스트")
class ProductTest {

    @Test
    fun `상품의 Id는 영속화된 상품 객체의 Id로 매핑된다`() {
        // given
        val product = Product(
            name = "상품 이름",
            price = 10_000,
            stockQuantity = 20,
            imageUrls = listOf("imageUrl1", "imageUrl2")
        )
        val afterPersistenceId: Long = 1

        // when
        product.reflectIdAfterPersistence(id = afterPersistenceId)

        // then
        assertThat(product.id).isEqualTo(afterPersistenceId)
    }

    @Test
    fun `상품을 수정하면, 상태는 WAIT로 변경되고, 수정날짜가 갱신된다`() {
        // given
        val product = Product(
            name = "상품 이름",
            price = 10_000,
            stockQuantity = 20,
            imageUrls = listOf("imageUrl1", "imageUrl2")
        )
        val fixtureUpdateDate: LocalDateTime = product.updatedDate
        val fixtureName = "상품 이름 변경"
        val fixturePrice = 5_000
        val fixtureStockQuantity = 10

        // when
        product.update(name = fixtureName, price = fixturePrice, stockQuantity = fixtureStockQuantity)

        // then
        assertThat(product.name).isEqualTo(fixtureName)
        assertThat(product.price).isEqualTo(fixturePrice)
        assertThat(product.stockQuantity).isEqualTo(fixtureStockQuantity)
        assertThat(product.confirmStatus).isEqualTo(ProductConfirmStatus.WAIT)
        assertThat(product.updatedDate).isNotEqualTo(fixtureUpdateDate)
    }

    @Test
    fun `차감 요청한 재고 수량보다 상품의 재고 수량이 크면, 상품의 재고 수량이 차감되고, 수정날짜가 갱신된다`() {
        // given
        val product = Product(
            name = "상품 이름",
            price = 10_000,
            stockQuantity = 10,
            imageUrls = listOf("imageUrl1", "imageUrl2")
        )
        val fixtureUpdateDate: LocalDateTime = product.updatedDate
        val fixtureStockQuantity = product.stockQuantity
        val requestStockQuantity = 5

        // when
        product.decreaseStockCount(stockQuantity = requestStockQuantity)

        // then
        assertThat(product.stockQuantity).isEqualTo(fixtureStockQuantity - requestStockQuantity)
        assertThat(product.updatedDate).isNotEqualTo(fixtureUpdateDate)
    }

    @Test
    fun `차감 요청한 재고 수량보다 상품의 재고 수량이 작으면, 예외를 발생시킨다`() {
        // given
        val product = Product(
            name = "상품 이름",
            price = 10_000,
            stockQuantity = 10,
            imageUrls = listOf("imageUrl1", "imageUrl2")
        )
        val requestStockQuantity = 15

        // when
        val exception = Assertions.assertThrows(DomainMySqlException::class.java) {
            product.decreaseStockCount(stockQuantity = requestStockQuantity)
        }

        // then
        assertThat(exception.message)
            .isEqualTo(CommandAppExceptionMessage.HAVE_EXCEEDED_THE_QUANTITY_AVAILABLE_FOR_PURCHASE.localizedMessage)

        assertThat(exception.localizedMessage)
            .isEqualTo(CommandAppExceptionMessage.HAVE_EXCEEDED_THE_QUANTITY_AVAILABLE_FOR_PURCHASE.localizedMessage)
    }

    @ParameterizedTest
    @CsvSource(value = ["APPROVE", "REJECT", "WAIT"])
    fun `상품의 상태를 변경한다`(confirmStatus: ProductConfirmStatus) {
        // given
        val product = Product(
            name = "상품 이름",
            price = 10_000,
            stockQuantity = 10,
            imageUrls = listOf("imageUrl1", "imageUrl2")
        )

        // when
        product.updateConfirmStatus(confirmStatus = confirmStatus)

        // then
        assertThat(product.confirmStatus).isEqualTo(confirmStatus)
    }
}
