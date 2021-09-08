package com.hs.unit

import com.hs.entity.ProductConfirmStatus
import com.hs.exception.DomainMySqlException
import com.hs.message.CommandAppExceptionMessage
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

@DisplayName("ProductConfirmStatus 단위 테스트")
class ProductConfirmStatusTest {

    @ParameterizedTest
    @CsvSource(
        value = ["APPROVE, APPROVE", "REJECT, REJECT", "WAIT, WAIT"]
    )
    fun `String 타입의 상품 상태 값을 Enum 클래스로 변환한다`(
        requestConfirmStatus: String, expectConfirmStatus: ProductConfirmStatus
    ) {
        // when
        val productConfirmStatus =
            ProductConfirmStatus.convertFromStringToProductConfirmStatus(value = requestConfirmStatus)

        // then
        assertThat(productConfirmStatus).isEqualTo(expectConfirmStatus)
    }

    @Test
    fun `유효하지 않은 String 타입의 상품 상태 값이면, 예외를 반환한다`() {
        // given
        val confirmStatus = "EXCEPTION_TEST"

        // when
        val exception = Assertions.assertThrows(DomainMySqlException::class.java) {
            ProductConfirmStatus.convertFromStringToProductConfirmStatus(value = confirmStatus)
        }

        // then
        assertThat(exception.message)
            .isEqualTo(CommandAppExceptionMessage.NOT_EXIST_PRODUCT_CONFIRM_STATUS.localizedMessage)

        assertThat(exception.localizedMessage)
            .isEqualTo(CommandAppExceptionMessage.NOT_EXIST_PRODUCT_CONFIRM_STATUS.localizedMessage)
    }
}
