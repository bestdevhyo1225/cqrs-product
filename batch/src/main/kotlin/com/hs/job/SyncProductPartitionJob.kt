package com.hs.job

import com.hs.dto.FindProductDto
import com.hs.entity.Product
import com.hs.entity.ProductAggregate
import com.hs.entity.ProductAggregateType.FIND_PRODUCT
import com.hs.job.partitioner.ProductIdRangePartitioner
import com.hs.job.reader.JpaPagingFetchItemReader
import com.hs.repository.BatchAppProductAggregateRepository
import com.hs.repository.BatchAppProductQueryRepository
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
        * [ keepAliveSeconds : 30초 ]
        * - 30초가 지나서도 Core Thread Pool의 Thread가 사용되지 않을 경우, 해당 Thread는 종료된다.
        * */
        executor.keepAliveSeconds = 30
        executor.setThreadNamePrefix("partition-exec-")
        /*
         * [ allowCoreThreadTimeOut : true ]
         * - Core Thread가 일정 시간 Task를 받지 않을 경우, Pool에서 정리되고, 모든 자식 Thread가 정리되면, JVM도 종료된다.
         * */
        executor.setAllowCoreThreadTimeOut(true)
        // Queue 대기열 및 Task 가 완료된 이후에 Shutdown 여부
        executor.setWaitForTasksToCompleteOnShutdown(true)
        executor.initialize()

        return executor
    }

    @Bean(name = [JOB_NAME + "_PartitionHandler"])
    fun partitionHandler(): TaskExecutorPartitionHandler {
        val partitionHandler = TaskExecutorPartitionHandler()

        partitionHandler.setTaskExecutor(executor())
        partitionHandler.gridSize = poolSize
        partitionHandler.step = step() // Worker로 실행할 Step을 지정한다. Partitioner가 만들어준 StepExecutions 환경에서 개별적으로 실행된다.

        return partitionHandler
    }

    @Bean(name = [JOB_NAME])
    fun job(): Job {
        return jobBuilderFactory.get("syncProductPartitionJob")
            .start(stepManager())
            .preventRestart() // 재실행을 막아준다.
            .build()
    }

    @Bean(name = [JOB_NAME + "_StepManager"])
    fun stepManager(): Step {
        return stepBuilderFactory.get("syncProductPartitionStep.Manager")
            .partitioner("Step", partitioner(null, null)) // step에서 사용될 Partitioner 구현체를 등록한다.
            .step(step()) // 파티셔닝될 step을 등록한다. Partitioner 로직에 따라 서로 다른 StepExecutions를 가진 여러개로 생성된다.
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
            .chunk<Product, ProductAggregate>(chunkSize)
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
    ): JpaPagingFetchItemReader<Product> {
        logger.info("minId = {}, maxId = {}", minId, maxId)

        val params: Map<String, Long> = mapOf("minId" to (minId ?: 0), "maxId" to (maxId ?: 0))

        val reader: JpaPagingFetchItemReader<Product> = JpaPagingFetchItemReader()

        reader.setEntityManagerFactory(entityManagerFactory = entityManagerFactory)
        reader.setQueryString(queryString = "SELECT p FROM Product p WHERE p.id BETWEEN :minId AND :maxId ORDER BY p.id")
        reader.setParameterValues(parameterValues = params)
        reader.pageSize = chunkSize

        return reader
    }

    private fun processor(): ItemProcessor<Product, ProductAggregate> {
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
