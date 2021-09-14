package com.hs.unit

import com.hs.entity.ProductDetail
import com.hs.exception.DomainMySqlException
import com.hs.exception.DomainMysqlExceptionMessage
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

@DisplayName("ProductDetail 단위 테스트")
class ProductDetailTest {

    @ParameterizedTest
    @CsvSource(
        value = ["APPROVE, APPROVE", "REJECT, REJECT", "WAIT, WAIT"]
    )
    fun `String 타입의 상품 상태 값을 Enum 클래스로 변환한다`(
        requestConfirmStatus: String, expectConfirmStatus: ProductDetail.ConfirmStatus
    ) {
        // when
        val productConfirmStatus: ProductDetail.ConfirmStatus =
            ProductDetail.convertFromStringToEnumValue(value = requestConfirmStatus)

        // then
        assertThat(productConfirmStatus).isEqualTo(expectConfirmStatus)
    }

    @Test
    fun `유효하지 않은 String 타입의 상품 상태 값이면, 예외를 반환한다`() {
        // given
        val confirmStatus = "EXCEPTION_TEST"

        // when
        val exception = org.junit.jupiter.api.Assertions.assertThrows(DomainMySqlException::class.java) {
            ProductDetail.convertFromStringToEnumValue(value = confirmStatus)
        }

        // then
        assertThat(exception.message)
            .isEqualTo(DomainMysqlExceptionMessage.NOT_EXIST_PRODUCT_CONFIRM_STATUS.localizedMessage)

        assertThat(exception.localizedMessage)
            .isEqualTo(DomainMysqlExceptionMessage.NOT_EXIST_PRODUCT_CONFIRM_STATUS.localizedMessage)
    }
}
