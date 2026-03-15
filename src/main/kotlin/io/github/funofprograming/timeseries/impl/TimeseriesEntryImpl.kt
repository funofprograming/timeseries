package io.github.funofprograming.timeseries.impl

import io.github.funofprograming.timeseries.TimeseriesEntry
import java.time.Instant
import java.util.UUID

open class TimeseriesEntryImpl<E> (
    override val eventInstant: Instant
    , override val event:E
    , override var eventId: UUID?): TimeseriesEntry<E> {

    override fun compareTo(other: TimeseriesEntry<E>): Int = this.eventInstant.compareTo(other.eventInstant)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TimeseriesEntryImpl<*>

        if (eventInstant != other.eventInstant) return false
        if (event != other.event) return false

        return true
    }

    override fun hashCode(): Int {
        var result = eventInstant.hashCode()
        result = 31 * result + (event?.hashCode() ?: 0)
        return result
    }


}