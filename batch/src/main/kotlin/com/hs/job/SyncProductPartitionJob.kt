package com.hs.job

import com.hs.entity.ProductAggregate
import com.hs.entity.ProductPersistence
import com.hs.job.partitioner.ProductIdRangePartitioner
import com.hs.job.reader.JpaPagingFetchItemReader
import com.hs.repository.BatchAppProductAggregateRepository
import com.hs.repository.BatchAppProductQueryRepository
import com.hs.dto.UpsertProductAggregateDto
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.core.partition.support.TaskExecutorPartitionHandler
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.ItemWriter
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.task.TaskExecutor
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.persistence.EntityManagerFactory

@Configuration
class SyncProductPartitionJob(
    @Value("\${chunk-size:10}")
    private val chunkSize: Int,
    @Value("\${pool-size:5}")
    private val poolSize: Int,
    private val jobBuilderFactory: JobBuilderFactory,
    private val stepBuilderFactory: StepBuilderFactory,
    private val entityManagerFactory: EntityManagerFactory,
    private val productQueryRepository: BatchAppProductQueryRepository,
    private val productAggregateRepository: BatchAppProductAggregateRepository
) {

    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    companion object {
        private const val JOB_NAME = "SyncProductPartitionJob"
    }

    @Bean(name = [JOB_NAME + "_Executor"])
    fun executor(): TaskExecutor {
        val executor = ThreadPoolTaskExecutor()

        executor.corePoolSize = poolSize
        executor.maxPoolSize = poolSize
        /*
        * [ keepAliveSeconds : 30??? ]
        * - 30?????? ???????????? Core Thread Pool??? Thread??? ???????????? ?????? ??????, ?????? Thread??? ????????????.
        * */
        executor.keepAliveSeconds = 30
        executor.setThreadNamePrefix("partition-exec-")
        /*
         * [ allowCoreThreadTimeOut : true ]
         * - Core Thread??? ?????? ?????? Task??? ?????? ?????? ??????, Pool?????? ????????????, ?????? ?????? Thread??? ????????????, JVM??? ????????????.
         * */
        executor.setAllowCoreThreadTimeOut(true)
        // Queue ????????? ??? Task ??? ????????? ????????? Shutdown ??????
        executor.setWaitForTasksToCompleteOnShutdown(true)
        executor.initialize()

        return executor
    }

    @Bean(name = [JOB_NAME + "_PartitionHandler"])
    fun partitionHandler(): TaskExecutorPartitionHandler {
        val partitionHandler = TaskExecutorPartitionHandler()

        partitionHandler.setTaskExecutor(executor())
        partitionHandler.gridSize = poolSize
        partitionHandler.step = step() // Worker??? ????????? Step??? ????????????. Partitioner??? ???????????? StepExecutions ???????????? ??????????????? ????????????.

        return partitionHandler
    }

    @Bean(name = [JOB_NAME])
    fun job(): Job {
        return jobBuilderFactory.get("syncProductPartitionJob")
            .start(stepManager())
            .preventRestart() // ???????????? ????????????.
            .build()
    }

    @Bean(name = [JOB_NAME + "_StepManager"])
    fun stepManager(): Step {
        return stepBuilderFactory.get("syncProductPartitionStep.Manager")
            .partitioner("Step", partitioner(null, null)) // step?????? ????????? Partitioner ???????????? ????????????.
            .step(step()) // ??????????????? step??? ????????????. Partitioner ????????? ?????? ?????? ?????? StepExecutions??? ?????? ???????????? ????????????.
            .partitionHandler(partitionHandler())
            .build()
    }

    @Bean(name = [JOB_NAME + "_Partitioner"])
    @StepScope
    fun partitioner(
        @Value("#{jobParameters['startDate']}") startDate: String?,
        @Value("#{jobParameters['endDate']}") endDate: String?
    ): ProductIdRangePartitioner {
        val datetimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val startLocalDatetime = LocalDateTime.parse("$startDate 00:00:00", datetimeFormatter)
        val endLocalDatetime = LocalDateTime.parse("$endDate 23:59:59", datetimeFormatter)

        return ProductIdRangePartitioner(
            productQueryRepository = productQueryRepository,
            startDatetime = startLocalDatetime,
            endDatetime = endLocalDatetime
        )
    }

    @Bean(name = [JOB_NAME + "_Step"])
    fun step(): Step {
        return stepBuilderFactory.get("syncProductPartitionStep")
            .chunk<ProductPersistence, UpsertProductAggregateDto>(chunkSize)
            .reader(reader(null, null))
            .processor(processor())
            .writer(writer())
            .build()
    }

    @Bean(name = [JOB_NAME + "_Reader"])
    @StepScope
    fun reader(
        @Value("#{stepExecutionContext['minId']}") minId: Long?,
        @Value("#{stepExecutionContext['maxId']}") maxId: Long?,
    ): JpaPagingFetchItemReader<ProductPersistence> {
        logger.info("minId = {}, maxId = {}", minId, maxId)

        val params: Map<String, Long> = mapOf("minId" to (minId ?: 0), "maxId" to (maxId ?: 0))

        val reader: JpaPagingFetchItemReader<ProductPersistence> = JpaPagingFetchItemReader()

        reader.setEntityManagerFactory(entityManagerFactory = entityManagerFactory)
        reader.setQueryString(queryString = "SELECT p FROM ProductPersistence p WHERE p.id BETWEEN :minId AND :maxId ORDER BY p.id")
        reader.setParameterValues(parameterValues = params)
        reader.pageSize = chunkSize

        return reader
    }

    private fun processor(): ItemProcessor<ProductPersistence, UpsertProductAggregateDto> {
        return ItemProcessor<ProductPersistence, UpsertProductAggregateDto> { product ->
            var productAggregate: ProductAggregate? =
                productAggregateRepository.findByProductId(productId = product.id!!)

            val isNew: Boolean = isNewProductAggregate(productAggregate)

            when (productAggregate) {
                null -> productAggregate = ProductAggregate.create(
                    productId = product.id!!,
                    name = product.name,
                    price = product.price,
                    stockQuantity = product.stockQuantity,
                    imageUrls = product.createImageUrls(),
                    confirmStatus = product.confirmStatus.toString(),
                )
                else -> productAggregate.changeProductAggregateData(
                    name = product.name,
                    price = product.price,
                    stockQuantity = product.stockQuantity,
                    imageUrls = product.createImageUrls(),
                    confirmStatus = product.confirmStatus.toString()
                )
            }

            UpsertProductAggregateDto(productAggregate = productAggregate, isNew = isNew)
        }
    }

    @Bean(name = [JOB_NAME + "_Writer"])
    fun writer(): ItemWriter<UpsertProductAggregateDto> {
        return ItemWriter { upsertProductAggregateVos ->
            val insertProductAggregates: MutableList<ProductAggregate> = mutableListOf()
            val saveProductAggregates: MutableList<ProductAggregate> = mutableListOf()

            upsertProductAggregateVos.forEach {
                if (it.isNew) insertProductAggregates.add(it.productAggregate)
                else saveProductAggregates.add(it.productAggregate)
            }

            if (insertProductAggregates.isNotEmpty()) {
                productAggregateRepository.insertAll(productAggregates = insertProductAggregates)
            }

            if (saveProductAggregates.isNotEmpty()) {
                productAggregateRepository.saveAll(productAggregates = saveProductAggregates)
            }
        }
    }

    private fun isNewProductAggregate(productAggregate: ProductAggregate?): Boolean {
        return productAggregate == null
    }
}
