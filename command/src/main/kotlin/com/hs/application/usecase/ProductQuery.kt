package com.hs.application.usecase

import com.hs.dto.FindProductDto
import com.hs.message.CommandAppExceptionMessage
import com.hs.repository.ProductQueryRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional // Write DB 쪽으로 조회하면, 데이터의 일관성이 보장되기 때문에 ReadOnly 옵션을 사용하지 않았다.
class ProductQuery(
    private val productQueryRepository: ProductQueryRepository
) {

    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    fun findProductAggregate(id: Long): FindProductDto {
        logger.info("findProductAggregate() method is executed")

        return productQueryRepository.findProductAggregate(id = id)
            ?: throw NoSuchElementException(CommandAppExceptionMessage.NOT_FOUND_PRODUCT.localizedMessage)
    }
}
