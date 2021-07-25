package com.hs.job.reader

import org.springframework.batch.item.database.AbstractPagingItemReader
import org.springframework.batch.item.database.orm.JpaQueryProvider
import org.springframework.dao.DataAccessResourceFailureException
import org.springframework.util.Assert
import org.springframework.util.ClassUtils
import org.springframework.util.CollectionUtils
import java.util.concurrent.CopyOnWriteArrayList
import javax.persistence.EntityManager
import javax.persistence.EntityManagerFactory
import javax.persistence.Query

open class JpaPagingFetchItemReader<T> : AbstractPagingItemReader<T>() {

    private val jpaPropertyMap: Map<String, Any> = HashMap()

    private lateinit var entityManagerFactory: EntityManagerFactory
    private lateinit var entityManager: EntityManager
    private var queryProvider: JpaQueryProvider? = null
    private var parameterValues: Map<String, Any>? = null
    private lateinit var queryString: String

    init {
        this.setName(ClassUtils.getShortName(JpaPagingFetchItemReader::class.java))
    }

    private fun createQuery(): Query {
        return queryProvider?.createQuery() ?: entityManager.createQuery(queryString)
    }

    fun setEntityManagerFactory(entityManagerFactory: EntityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory
    }

    fun setParameterValues(parameterValues: Map<String, Any>) {
        this.parameterValues = parameterValues
    }

    fun setQueryString(queryString: String) {
        this.queryString = queryString
    }

    fun setQueryProvider(queryProvider: JpaQueryProvider) {
        this.queryProvider = queryProvider
    }

    override fun afterPropertiesSet() {
        super.afterPropertiesSet()

        if (queryProvider == null) {
            Assert.notNull(entityManagerFactory, "EntityManager is required when queryProvider is null");
            Assert.hasLength(queryString, "Query string is required when queryProvider is null");
        }
    }

    override fun doOpen() {
        super.doOpen()

        entityManager = entityManagerFactory.createEntityManager(jpaPropertyMap)
            ?: throw DataAccessResourceFailureException("Unable to obtain an EntityManager")

        queryProvider?.setEntityManager(entityManager)
    }

    @Suppress(names = ["UNCHECKED_CAST"])
    override fun doReadPage() {
        entityManager.clear()

        val query: Query = createQuery().setFirstResult(page * pageSize).setMaxResults(pageSize)

        parameterValues?.entries?.forEach { entry ->
            query.setParameter(entry.key, entry.value)
        }

        if (CollectionUtils.isEmpty(results)) { // CollectionUtils.isEmpty -> 널 체크까지 해준다.
            results = CopyOnWriteArrayList()
        } else {
            results.clear()
        }

        results.addAll(query.resultList as List<T>)
    }

    override fun doJumpToPage(itemIndex: Int) {
        TODO("Not yet implemented")
    }

    override fun doClose() {
        entityManager.close()
        super.doClose()
    }
}
