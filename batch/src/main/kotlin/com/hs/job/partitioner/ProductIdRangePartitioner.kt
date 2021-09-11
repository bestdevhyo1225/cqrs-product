package com.hs.job.partitioner

import com.hs.repository.BatchAppProductQueryRepository
import org.springframework.batch.core.partition.support.Partitioner
import org.springframework.batch.item.ExecutionContext
import java.time.LocalDateTime

open class ProductIdRangePartitioner(
    private val productQueryRepository: BatchAppProductQueryRepository,
    private val startDatetime: LocalDateTime,
    private val endDatetime: LocalDateTime,
) : Partitioner {

    override fun partition(gridSize: Int): MutableMap<String, ExecutionContext> {
        val minId: Long = productQueryRepository.findMinId(startDatetime = startDatetime, endDatetime = endDatetime) ?: 0
        val maxId: Long = productQueryRepository.findMaxId(startDatetime = startDatetime, endDatetime = endDatetime) ?: 0
        val targetSize: Long = (maxId - minId) / gridSize + 1

        val result: MutableMap<String, ExecutionContext> = mutableMapOf()

        var number = 0
        var start: Long = minId
        var end: Long = start + targetSize - 1

        while (start <= maxId) {
            val executionContext = ExecutionContext()

            result["partition$number"] = executionContext

            if (end >= maxId) end = maxId

            executionContext.putLong("minId", start)
            executionContext.putLong("maxId", end)

            start += targetSize
            end += targetSize

            number++
        }

        return result
    }
}
