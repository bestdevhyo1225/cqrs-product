package com.hs.job

import com.hs.dto.FindProductDto
import com.hs.entity.Product
import com.hs.entity.ProductAggregate
import com.hs.entity.ProductAggregateType.FIND_PRODUCT
import com.hs.reader.JpaPagingFetchItemReader
import com.hs.repository.BatchAppProductAggregateRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.database.JpaPagingItemReader
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.persistence.EntityManagerFactory

@Configuration
class SyncProductJob(
    @Value("\${chunk-size}")
    private val chunkSize: Int,
    private val jobBuilderFactory: JobBuilderFactory,
    private val stepBuilderFactory: StepBuilderFactory,
    private val entityManagerFactory: EntityManagerFactory,
    private val productAggregateRepository: BatchAppProductAggregateRepository
) {

    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    companion object {
        private const val JOB_NAME = "SyncProductJob"
    }

    @Bean(name = [JOB_NAME])
    fun job(): Job {
        return jobBuilderFactory.get("syncProductJob")
            .preventRestart() // 재실행을 막아준다.
            .start(step())
            .build()
    }

    @Bean(name = [JOB_NAME + "_Step"])
    fun step(): Step {
        return stepBuilderFactory.get("syncProductStep")
            .chunk<Product, ProductAggregate>(chunkSize)
            .reader(reader())
            .processor(processor())
            .writer(writer())
            .build()
    }

    @Bean(name = [JOB_NAME + "_Reader"])
    fun reader(): JpaPagingFetchItemReader<Product> {
        val reader: JpaPagingFetchItemReader<Product> = JpaPagingFetchItemReader()

        reader.setEntityManagerFactory(entityManagerFactory = entityManagerFactory)
        reader.setQueryString(queryString = "SELECT p FROM Product p")
        reader.pageSize = chunkSize

        return reader
    }

    @Bean(name = [JOB_NAME + "_Processor"])
    fun processor(): ItemProcessor<Product, ProductAggregate> {
        return ItemProcessor<Product, ProductAggregate> { product ->
            val productDto = FindProductDto(
                productId = product.id!!,
                name = product.name,
                price = product.price,
                stockQuantity = product.stockQuantity,
                confirmStatus = product.confirmStatus.toString(),
                imageUrls = product.productImages.map { productImage -> productImage.url }
            )

            var productAggregate: ProductAggregate? = productAggregateRepository.findByProductIdAndType(
                productId = product.id!!,
                type = FIND_PRODUCT
            )

            when (productAggregate) {
                null -> productAggregate = ProductAggregate.create(productDto = productDto, type = FIND_PRODUCT)
                else -> productAggregate.changeProductAggregateData(data = productDto)
            }

            productAggregate
        }
    }

    @Bean(name = [JOB_NAME + "_Writer"])
    fun writer(): ItemWriter<ProductAggregate> {
        return ItemWriter { productAggregates ->
            productAggregateRepository.saveAll(productAggregates = productAggregates)
        }
    }
}
