package com.hs.vo

/*
* [ 일급 컬렉션 ]
* - Collection을 Wrapping 하면서, Wrapping한 Collection 외에 다른 멤버 변수가 없다는 것이 특징이다.
*
* 장점)
* - 하나의 인스턴스에서 비즈니스 로직을 관리할 수 있다.
*   ex) 만약에 urls 길이가 50이 넘어가는 것은 제외해야 하는 경우
* - 리스트 내의 객체 상태를 동일하게 관리할 수 있다.
*   - 일급 컬렉션이 불변성을 보장한다는 것은 아니다.
*   - 일급 컬렉션은 불변성을 보장하지 않으며, 보장하도록 구현해야 할 필요도 없다.
*
* 참고)
* - 일급 컬렉션의 비즈니스 로직에 의해서 상태가 변경되어도 괜찮다. (불변 클래스가 아니어도 되기 때문에)
* */

class ProductImageUrls private constructor(
    private val productImageUrls: List<String>
) {

    override fun toString(): String {
        return "ProductImageUrls(productImageUrls=$productImageUrls)"
    }

    companion object {
        @JvmStatic
        fun create(productImageUrls: List<String>): ProductImageUrls {
            return ProductImageUrls(productImageUrls = productImageUrls)
        }
    }

    fun getProductImageUrls(): List<String> = productImageUrls

    fun getImageUrlsWithLengthLessThanFifty(): List<String> {
        return productImageUrls.filter { it.length < 50 }
    }
}
