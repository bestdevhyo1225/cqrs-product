package com.hs.usecase

import com.hs.dto.FindProductAggregateDto
import com.hs.repository.ProductQueryRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class QueryService(
    private val productQueryRepository: ProductQueryRepository
) {

    fun findProductAggregate(id: Long): FindProductAggregateDto {
        return productQueryRepository.findProductAggregate(id = id)
            ?: throw NoSuchElementException("해당 상품이 존재하지 않습니다. 상품 번호를 다시 확인해주세요!")
    }
}
