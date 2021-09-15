package com.hs.vo

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatter.ofPattern
import java.util.*

/*
* [ 정적 팩토리 메서드 패턴 ]
* - 디자인 패턴의 팩토리 메서드 패턴과는 다른 패턴이다. 이름만 비슷하다.
* - 클래스가 불변임을 보장하려면, 자신을 상속하지 못하게 해야한다. 이것을 간단하게 하려면, final 클래스로 선언할 수 있지만,
*   이보다 더 유연한 방법이 정적 팩토리 메서드 패턴이다.
*
* 장점)
* - 이름을 가질 수 있다.
* - 호출될 때 마다 인스턴스를 새로 생성하지 않아도 된다. (인스턴스의 변수에 따라 캐싱 전략을 사용할 수 있다.)
* - 반환 타입의 하위 타입 객체를 반환할 수 있는 능력이 있다.
* - 입력 매개변수에 따라 매번 다른 클래스의 객체를 반환할 수 있다.
* - 정적 팩터리 메서드를 작성하는 시점에는 반환할 객체의 클래스가 존재하지 않아도 된다.
*
* 단점)
* - 상속을 하려면, public, protected 생성자가 필요하니, 정적 팩터리 메서드만 제공하면, 하위 클래스를 만들 수 없다.
* - 정적 팩터리 메서드는 프로그래머가 찾기 어렵다.
* */

class ProductDatetime private constructor(
    private var createdDatetime: LocalDateTime,
    private var updatedDatetime: LocalDateTime,
) {

    override fun toString(): String =
        "ProductDatetime(createdDatetime=$createdDatetime, updatedDatetime=$updatedDatetime)"

    override fun hashCode(): Int = Objects.hash(createdDatetime, updatedDatetime)
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other is ProductDatetime) {
            return createdDatetime == other.createdDatetime && updatedDatetime == other.updatedDatetime
        }
        return false
    }

    companion object {
        @JvmStatic
        private val DATETIME_FORMATTER: DateTimeFormatter = ofPattern("yyyy-MM-dd HH:mm:ss")

        @JvmStatic
        fun createWithZeroNanoOfSecond(
            createdDatetime: LocalDateTime = LocalDateTime.now(),
            updatedDatetime: LocalDateTime = LocalDateTime.now()
        ): ProductDatetime {
            return ProductDatetime(
                createdDatetime = createdDatetime.withNano(0),
                updatedDatetime = updatedDatetime.withNano(0)
            )
        }

        @JvmStatic
        fun createByStringParams(
            createdDatetime: String,
            updatedDatetime: String
        ): ProductDatetime {
            return ProductDatetime(
                createdDatetime = toLocalDatetime(datetime = createdDatetime),
                updatedDatetime = toLocalDatetime(datetime = updatedDatetime)
            )
        }

        @JvmStatic
        private fun toLocalDatetime(datetime: String) =
            LocalDateTime.parse(datetime, DATETIME_FORMATTER)
    }

    fun getStringCreatedDatetime(): String = createdDatetime.format(DATETIME_FORMATTER)
    fun getStringUpdatedDatetime(): String = updatedDatetime.format(DATETIME_FORMATTER)

    fun getCreatedDatetime(): LocalDateTime = createdDatetime
    fun getUpdatedDatetime(): LocalDateTime = updatedDatetime
}
