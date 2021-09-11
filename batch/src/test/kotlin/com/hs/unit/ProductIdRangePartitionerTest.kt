package com.hs.unit

import com.hs.job.partitioner.ProductIdRangePartitioner
import com.hs.repository.BatchAppProductQueryRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.then
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.batch.item.ExecutionContext
import java.time.LocalDateTime

@ExtendWith(value = [MockitoExtension::class])
class ProductIdRangePartitionerTest {

    @Mock
    private lateinit var productQueryRepository: BatchAppProductQueryRepository

    @Test
    fun `gridSize에 맞게 Id가 분할된다`() {
        // given
        val startDatetime = LocalDateTime.now()
        val endDatetime = LocalDateTime.now()
        val minId = 1L
        val maxId = 10L

        given(productQueryRepository.findMinId(startDatetime = startDatetime, endDatetime = endDatetime))
            .willReturn(minId)

        given(productQueryRepository.findMaxId(startDatetime = startDatetime, endDatetime = endDatetime))
            .willReturn(maxId)

        // when
        val partitioner = ProductIdRangePartitioner(
            productQueryRepository = productQueryRepository,
            startDatetime = startDatetime,
            endDatetime = endDatetime
        )

        val executionContextMap: Map<String, ExecutionContext> = partitioner.partition(5)
        val partition0: ExecutionContext = executionContextMap["partition0"]!!
        val partition1: ExecutionContext = executionContextMap["partition1"]!!
        val partition2: ExecutionContext = executionContextMap["partition2"]!!
        val partition3: ExecutionContext = executionContextMap["partition3"]!!
        val partition4: ExecutionContext = executionContextMap["partition4"]!!

        // then
        then(productQueryRepository)
            .should()
            .findMinId(startDatetime = startDatetime, endDatetime = endDatetime)

        then(productQueryRepository)
            .should()
            .findMaxId(startDatetime = startDatetime, endDatetime = endDatetime)

        assertThat(partition0.getLong("minId")).isEqualTo(1L)
        assertThat(partition0.getLong("maxId")).isEqualTo(2L)

        assertThat(partition1.getLong("minId")).isEqualTo(3L)
        assertThat(partition1.getLong("maxId")).isEqualTo(4L)

        assertThat(partition2.getLong("minId")).isEqualTo(5L)
        assertThat(partition2.getLong("maxId")).isEqualTo(6L)

        assertThat(partition3.getLong("minId")).isEqualTo(7L)
        assertThat(partition3.getLong("maxId")).isEqualTo(8L)

        assertThat(partition4.getLong("minId")).isEqualTo(9L)
        assertThat(partition4.getLong("maxId")).isEqualTo(10L)
    }
}
