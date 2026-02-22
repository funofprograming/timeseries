package io.github.funofprograming.timeseries

import java.time.Instant
import java.util.UUID

interface MutableTimeseries<E>: Timeseries<E> {

    fun add(entry: TimeseriesEntry<E>, overwrite: Boolean = true): UUID

    fun add(entries: Collection<TimeseriesEntry<E>>, overwrite: Boolean=true): Collection<UUID>

    fun remove(entry: TimeseriesEntry<E>): Boolean

    fun remove(entries: Collection<TimeseriesEntry<E>>): Boolean

    fun remove(eventInstant: Instant): Boolean

    fun clear()
}