package com.hs.integration

import com.hs.TestBatchConfig
import com.hs.TestQuerydslConfig
import com.hs.entity.Product
import com.hs.job.SyncProductPartitionJob
import com.hs.repository.ProductAggregateRepositoryImpl
import com.hs.repository.ProductQueryRepositoryImpl
import com.hs.repository.ProductRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.batch.core.BatchStatus
import org.springframework.batch.core.JobExecution
import org.springframework.batch.core.JobParameters
import org.springframework.batch.test.JobLauncherTestUtils
import org.springframework.batch.test.context.SpringBatchTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@SpringBootTest(
    classes = [
        TestBatchConfig::class, TestQuerydslConfig::class, SyncProductPartitionJob::class,
        ProductQueryRepositoryImpl::class, ProductAggregateRepositoryImpl::class
    ]
)
@SpringBatchTest
class SyncProductPartitionJobTest {

    @Autowired(required = false)
    private lateinit var jobLauncherTestUtils: JobLauncherTestUtils

    @Autowired
    private lateinit var productRepository: ProductRepository

    @Test
    fun `Product 정보를 가공해서 ProductAggregate로 이관한다`() {
        // given
        val name = "반팔티"
        val price = 25_000
        val stockQuantity = 15
        val imageUrls: List<String> = listOf("image1.com", "image2.com")

        val products: MutableList<Product> = mutableListOf()
        for (i in 0 until 50) {
            products.add(
                Product.create(
                    name = name + i,
                    price = price,
                    stockQuantity = stockQuantity,
                    imageUrls = imageUrls
                )
            )
        }

        productRepository.saveAll(products)

        val datetimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val currentDatetime = LocalDate.now()
        val jobParameters: JobParameters = jobLauncherTestUtils.uniqueJobParametersBuilder
            .addString("startDate", currentDatetime.format(datetimeFormatter))
            .addString("endDate", currentDatetime.plusDays(1).format(datetimeFormatter))
            .toJobParameters()

        // when
        val jobExecution: JobExecution = jobLauncherTestUtils.launchJob(jobParameters)

        // then
        assertThat(jobExecution.status).isEqualTo(BatchStatus.COMPLETED)
    }
}
