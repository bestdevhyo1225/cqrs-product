package com.hs.unit

import com.hs.vo.ProductDatetime
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.time.LocalDateTime

@DisplayName("ProductDatetime 단위 테스트")
class ProductDatetimeTest {

    companion object {
        @JvmStatic
        fun localDatetimeParams(): List<Arguments> {
            return listOf(
                Arguments.arguments(listOf(LocalDateTime.now(), LocalDateTime.now())),
                Arguments.arguments(
                    listOf(
                        LocalDateTime.now(),
                        LocalDateTime.of(2021, 9, 17, 12, 33, 55)
                    )
                ),
            )
        }

        @JvmStatic
        fun stringParams(): List<Arguments> {
            return listOf(
                Arguments.arguments(listOf("2021-01-01 12:30:55", "2021-01-02 09:25:37")),
            )
        }
    }

    @ParameterizedTest
    @MethodSource(value = ["localDatetimeParams"])
    fun `LocalDatetime 타입을 인자로 넣으면, ProductDatetime 인스턴스가 생성된다`(localDateTimes: List<LocalDateTime>) {
        // given
        val createdDatetime: LocalDateTime = localDateTimes[0].withNano(0)
        val updatedDatetime: LocalDateTime = localDateTimes[1].withNano(0)

        // when
        val productDatetime = ProductDatetime.createWithZeroNanoOfSecond(
            createdDatetime = createdDatetime,
            updatedDatetime = updatedDatetime
        )

        // then
        assertThat(productDatetime).isInstanceOf(ProductDatetime::class.java)
        assertThat(productDatetime.getCreatedDatetime()).isInstanceOf(LocalDateTime::class.java)
        assertThat(productDatetime.getUpdatedDatetime()).isInstanceOf(LocalDateTime::class.java)
        assertThat(productDatetime.getCreatedDatetime()).isEqualTo(createdDatetime)
        assertThat(productDatetime.getUpdatedDatetime()).isEqualTo(updatedDatetime)
    }

    @Test
    fun `인자 없이 ProductDatetime 인스턴스를 생성하면, createdDatetime 값과 updatedDatetime 값은 항상 같다`() {
        // when
        val productDatetime = ProductDatetime.createWithZeroNanoOfSecond()

        // then
        assertThat(productDatetime.getCreatedDatetime()).isEqualTo(productDatetime.getUpdatedDatetime())
    }

    @ParameterizedTest
    @MethodSource(value = ["stringParams"])
    fun `String 타입을 인자로 넣으면, ProductDatetime 인스턴스가 생성된다`(stringDatetimes: List<String>) {
        // given
        val createdDatetime: String = stringDatetimes[0]
        val updatedDatetime: String = stringDatetimes[1]

        // when
        val productDatetime = ProductDatetime.createByStringParams(
            createdDatetime = createdDatetime,
            updatedDatetime = updatedDatetime
        )

        // then
        assertThat(productDatetime).isInstanceOf(ProductDatetime::class.java)
        assertThat(productDatetime.getCreatedDatetime()).isInstanceOf(LocalDateTime::class.java)
        assertThat(productDatetime.getUpdatedDatetime()).isInstanceOf(LocalDateTime::class.java)
    }

    @ParameterizedTest
    @MethodSource(value = ["stringParams"])
    fun `LocalDatetime 타입의 값을 String 타입으로 변환하여, 반환한다`(stringDatetimes: List<String>) {
        // given
        val createdDatetime: String = stringDatetimes[0]
        val updatedDatetime: String = stringDatetimes[1]

        // when
        val productDatetime = ProductDatetime.createByStringParams(
            createdDatetime = createdDatetime,
            updatedDatetime = updatedDatetime
        )

        // then
        assertThat(productDatetime.getStringCreatedDatetime()).isInstanceOf(String::class.java)
        assertThat(productDatetime.getStringUpdatedDatetime()).isInstanceOf(String::class.java)
        assertThat(productDatetime.getStringCreatedDatetime()).isEqualTo(createdDatetime)
        assertThat(productDatetime.getStringUpdatedDatetime()).isEqualTo(updatedDatetime)
    }
}
