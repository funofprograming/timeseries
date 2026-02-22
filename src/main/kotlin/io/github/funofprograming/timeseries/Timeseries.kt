package io.github.funofprograming.timeseries

import java.time.Instant
import java.util.*

interface Timeseries<E>: Iterable<Collection<TimeseriesEntry<E>>> {

    fun plus(entry: TimeseriesEntry<E>, overwrite: Boolean=true): Timeseries<E>

    fun plus(entries: Collection<TimeseriesEntry<E>>, overwrite: Boolean=true): Timeseries<E>

    fun get(eventInstant: Instant, eventId:UUID): TimeseriesEntry<E>?

    fun get(eventInstant:Instant): Collection<TimeseriesEntry<E>>

    fun getAll(): NavigableMap<Instant, Collection<TimeseriesEntry<E>>>

    fun getAllInstants(): NavigableSet<Instant>

    fun minus(entry: TimeseriesEntry<E>): Timeseries<E>

    fun minus(entries: Collection<TimeseriesEntry<E>>): Timeseries<E>

    fun minus(eventInstant: Instant): Timeseries<E>

    fun contains(eventInstant:Instant): Boolean

    fun contains(eventId:UUID): Boolean

    fun contains(event:E): Boolean

    fun countInstants(): Int

    fun countEvents(): Int

    fun isEmpty(): Boolean = countInstants() == 0

    fun isNotEmpty(): Boolean = !isEmpty()

    fun getEntriesSubMap(fromEventInstant: Instant, fromInclusive: Boolean, toEventInstant: Instant, toInclusive: Boolean): NavigableMap<Instant, Collection<TimeseriesEntry<E>>>

    fun getEntriesHeadMap(toEventInstant: Instant, toInclusive: Boolean): NavigableMap<Instant, Collection<TimeseriesEntry<E>>>

    fun getEntriesTailMap(fromEventInstant: Instant, fromInclusive: Boolean): NavigableMap<Instant, Collection<TimeseriesEntry<E>>>

    fun getEntriesSubMap(fromEventInstant: Instant, toEventInstant: Instant): NavigableMap<Instant, Collection<TimeseriesEntry<E>>> = getEntriesSubMap(fromEventInstant, false, toEventInstant, false)

    fun getEntriesHeadMap(toEventInstant: Instant): NavigableMap<Instant, Collection<TimeseriesEntry<E>>> = getEntriesHeadMap(toEventInstant, false)

    fun getEntriesTailMap(fromEventInstant: Instant): NavigableMap<Instant, Collection<TimeseriesEntry<E>>> = getEntriesTailMap(fromEventInstant, false)

    fun start(): Collection<TimeseriesEntry<E>>?

    fun end(): Collection<TimeseriesEntry<E>>?

    fun descendingIterator(): Iterator<Collection<TimeseriesEntry<E>>>
}