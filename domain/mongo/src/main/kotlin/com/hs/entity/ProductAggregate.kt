package com.hs.entity

import com.hs.vo.ProductAggregateId
import com.hs.vo.ProductDatetime

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

class ProductAggregate private constructor(
    productAggregateId: ProductAggregateId,
    productInfo: ProductInfo,
    productDatetime: ProductDatetime,
) {

    var productAggregateId: ProductAggregateId = productAggregateId
        private set

    var productInfo: ProductInfo = productInfo
        private set

    var productDatetime: ProductDatetime = productDatetime
        private set

    override fun toString(): String {
        return "ProductAggregate(productAggregateId=$productAggregateId, " +
                "productInfo=$productInfo, productDatetime=$productDatetime)"
    }

    companion object {
        @JvmStatic
        fun create(
            productId: Long,
            name: String,
            price: Int,
            stockQuantity: Int,
            imageUrls: List<String>,
            confirmStatus: String
        ): ProductAggregate {
            return ProductAggregate(
                productAggregateId = ProductAggregateId.create(),
                productInfo = ProductInfo.create(
                    id = productId,
                    name = name,
                    price = price,
                    confirmStatus = confirmStatus,
                    stockQuantity = stockQuantity,
                    imageUrls = imageUrls
                ),
                productDatetime = ProductDatetime.createWithZeroNanoOfSecond()
            )
        }

        @JvmStatic
        fun mapOf(
            id: String,
            productId: Long,
            name: String,
            price: Int,
            stockQuantity: Int,
            imageUrls: List<String>,
            isDisplay: Boolean,
            createdDatetime: String,
            updatedDatetime: String,
        ): ProductAggregate {
            return ProductAggregate(
                productAggregateId = ProductAggregateId.create(id = id),
                productInfo = ProductInfo.mapOf(
                    id = productId,
                    name = name,
                    price = price,
                    isDisplay = isDisplay,
                    stockQuantity = stockQuantity,
                    imageUrls = imageUrls
                ),
                productDatetime = ProductDatetime.createByStringParams(
                    createdDatetime = createdDatetime,
                    updatedDatetime = updatedDatetime
                )
            )
        }
    }

    fun changeProductAggregateData(
        name: String,
        price: Int,
        stockQuantity: Int,
        imageUrls: List<String>,
        confirmStatus: String
    ) {
        productInfo.changeProductInfo(
            name = name,
            price = price,
            stockQuantity = stockQuantity,
            imageUrls = imageUrls,
            confirmStatus= confirmStatus,
        )
        productDatetime = ProductDatetime.createWithZeroNanoOfSecond(
            createdDatetime = productDatetime.getCreatedDatetime()
        )
    }

    fun reflectIdAfterPersistence(id: String?) {
        productAggregateId = ProductAggregateId.createAfterPersistence(id = id)
    }

    fun getProductName(): String = productInfo.getName()
    fun getProductPrice(): Int = productInfo.getPrice()
    fun getProductStockQuantity(): Int = productInfo.getStockQuantity()
    fun getProductImageUrls(): List<String> = productInfo.getProductImageUrls()

    fun getStringCreatedDatetime(): String = productDatetime.getStringCreatedDatetime()
    fun getStringUpdatedDatetime(): String = productDatetime.getStringUpdatedDatetime()
}
