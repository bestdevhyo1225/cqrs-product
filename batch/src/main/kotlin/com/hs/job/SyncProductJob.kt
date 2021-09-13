package com.hs.job

import com.hs.entity.ProductAggregate
import com.hs.vo.ProductInfo
import com.hs.entity.ProductPersistence
import com.hs.job.reader.JpaPagingFetchItemReader
import com.hs.repository.BatchAppProductAggregateRepository
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.ItemWriter
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

    companion object {
        private const val JOB_NAME = "SyncProductJob"
    }

    @Bean(name = [JOB_NAME])
    fun job(): Job {
        return jobBuilderFactory.get("syncProductJob")
            .start(step())
            .preventRestart() // 재실행을 막아준다.
            .build()
    }

    @Bean(name = [JOB_NAME + "_Step"])
    fun step(): Step {
        return stepBuilderFactory.get("syncProductStep")
            .chunk<ProductPersistence, ProductAggregate>(chunkSize)
            .reader(reader())
            .processor(processor())
            .writer(writer())
            .build()
    }

    @Bean(name = [JOB_NAME + "_Reader"])
    fun reader(): JpaPagingFetchItemReader<ProductPersistence> {
        val reader: JpaPagingFetchItemReader<ProductPersistence> = JpaPagingFetchItemReader()

        reader.setEntityManagerFactory(entityManagerFactory = entityManagerFactory)
        reader.setQueryString(queryString = "SELECT p FROM ProductPersistence p ORDER BY p.id")
        reader.pageSize = chunkSize

        return reader
    }

    @Bean(name = [JOB_NAME + "_Processor"])
    fun processor(): ItemProcessor<ProductPersistence, ProductAggregate> {
        return ItemProcessor<ProductPersistence, ProductAggregate> { product ->
            val productInfo = ProductInfo.create(
                name = product.name,
                price = product.price,
                stockQuantity = product.stockQuantity,
                imageUrls = product.createImageUrls()
            )

            var productAggregate: ProductAggregate? =
                productAggregateRepository.findByProductId(productId = product.id!!)

            when (productAggregate) {
                null -> productAggregate = ProductAggregate.create(
                    productId = product.id!!,
                    productInfo = productInfo,
                    confirmStatus = product.confirmStatus.toString(),
                )
                else -> productAggregate.changeProductAggregateData(
                    productInfo = productInfo,
                    confirmStatus = product.confirmStatus.toString()
                )
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
