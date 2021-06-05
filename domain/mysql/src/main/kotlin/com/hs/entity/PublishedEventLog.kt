package com.hs.entity

import au.com.console.kassava.kotlinEquals
import au.com.console.kassava.kotlinHashCode
import au.com.console.kassava.kotlinToString
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Column
import javax.persistence.Enumerated
import javax.persistence.EnumType

@Entity
class PublishedEventLog(commandCode: CommandCode, message: String) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    var commandCode: CommandCode = commandCode
        protected set

    @Column(nullable = false)
    var message: String = message
        protected set

    @Column(nullable = false)
    val createdDate: LocalDateTime = LocalDateTime.now()

    override fun toString() = kotlinToString(properties = toStringProperties)
    override fun equals(other: Any?) = kotlinEquals(other = other, properties = equalsAndHashCodeProperties)
    override fun hashCode() = kotlinHashCode(properties = equalsAndHashCodeProperties)

    companion object {
        private val equalsAndHashCodeProperties = arrayOf(PublishedEventLog::id)
        private val toStringProperties = arrayOf(
            PublishedEventLog::id,
            PublishedEventLog::commandCode,
            PublishedEventLog::message,
            PublishedEventLog::createdDate
        )
    }
}
