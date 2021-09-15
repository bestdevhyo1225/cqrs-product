package com.hs.entity

import com.hs.exception.DomainMongoException
import com.hs.exception.DomainMongoExceptionMessage
import com.hs.vo.ProductImageUrls

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

class ProductInfo private constructor(
    private val id: Long,
    private var name: String,
    private var price: Int,
    private var stockQuantity: Int,
    private var isDisplay: Boolean,
    private var productImageUrls: ProductImageUrls,
) {

    enum class ConfirmStatus { WAIT, REJECT, APPROVE }

    override fun toString(): String = "ProductInfo(id=$id, name=$name, price=$price, isDisplay=$isDisplay, " +
            "stockQuantity=$stockQuantity, productImageUrls=$productImageUrls)"

    companion object {
        @JvmStatic
        fun create(
            id: Long,
            name: String,
            price: Int,
            confirmStatus: String,
            stockQuantity: Int,
            imageUrls: List<String>
        ): ProductInfo {
            return ProductInfo(
                id = id,
                name = name,
                price = price,
                isDisplay = isApproveConfirmStatus(value = confirmStatus),
                stockQuantity = stockQuantity,
                productImageUrls = ProductImageUrls.create(imageUrls = imageUrls)
            )
        }

        @JvmStatic
        fun mapOf(
            id: Long,
            name: String,
            price: Int,
            isDisplay: Boolean,
            stockQuantity: Int,
            imageUrls: List<String>
        ): ProductInfo {
            return ProductInfo(
                id = id,
                name = name,
                price = price,
                isDisplay = isDisplay,
                stockQuantity = stockQuantity,
                productImageUrls = ProductImageUrls.create(imageUrls = imageUrls)
            )
        }

        @JvmStatic
        fun isApproveConfirmStatus(value: String): Boolean {
            try {
                return ConfirmStatus.valueOf(value = value) == ConfirmStatus.APPROVE
            } catch (exception: Exception) {
                throw DomainMongoException(DomainMongoExceptionMessage.NOT_EXIST_PRODUCT_CONFIRM_STATUS)
            }
        }
    }

    fun changeProductInfo(
        name: String,
        price: Int,
        confirmStatus: String,
        stockQuantity: Int,
        imageUrls: List<String>
    ) {
        this.name = name
        this.price = price
        this.isDisplay = isApproveConfirmStatus(value = confirmStatus)
        this.stockQuantity = stockQuantity
        this.productImageUrls = ProductImageUrls.create(imageUrls = imageUrls)
    }

    fun getId(): Long = id
    fun getName(): String = name
    fun getPrice(): Int = price
    fun getIsDisplay(): Boolean = isDisplay
    fun getStockQuantity(): Int = stockQuantity
    fun getProductImageUrls(): List<String> = productImageUrls.getProductImageUrls()
}
