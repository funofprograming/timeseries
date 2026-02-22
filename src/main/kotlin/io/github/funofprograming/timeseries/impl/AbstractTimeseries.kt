package io.github.funofprograming.timeseries.impl

import io.github.funofprograming.timeseries.Timeseries
import io.github.funofprograming.timeseries.TimeseriesEntry
import io.github.funofprograming.timeseries.timeseriesEntryOf
import java.time.Instant
import java.util.Collections.emptyIterator
import java.util.Comparator
import java.util.NavigableMap
import java.util.NavigableSet
import java.util.TreeMap
import java.util.TreeSet
import java.util.UUID
import java.util.stream.Collectors
import kotlin.collections.plus

abstract class AbstractTimeseries<E, S: Set<UUID>, M: Map<UUID, E>>: Timeseries<E> {

    protected abstract fun getTimeseriesStore(): NavigableMap<Instant, S>
    protected abstract fun getTimeseriesEventsStore(): M

    override fun get(eventInstant:Instant, eventId:UUID): TimeseriesEntry<E>? = read {
        if(getTimeseriesStore()[eventInstant]?.contains(eventId)?:false) {
            return@read getTimeseriesEventsStore()[eventId]?.let { timeseriesEntryOf(eventInstant, it, eventId) }
        }
        return@read null
    }

    override fun get(eventInstant:Instant): Collection<TimeseriesEntry<E>> = read  {
        return@read getEntriesSubMap(eventInstant, true, eventInstant, true)?.get(eventInstant) ?: emptyList()
    }

    override fun getAll(): NavigableMap<Instant, Collection<TimeseriesEntry<E>>> = read { storeToEntries(getTimeseriesStore()) }

    override fun getAllInstants(): NavigableSet<Instant> = read { getTimeseriesStore().keys.stream().collect(Collectors.toCollection { TreeSet() }) }

    override fun contains(eventInstant:Instant): Boolean = read { getTimeseriesStore().contains(eventInstant) }

    override fun contains(eventId:UUID): Boolean = read { getTimeseriesEventsStore().contains(eventId) }

    override fun contains(event:E): Boolean = read { getTimeseriesEventsStore().values.contains(event) }

    override fun countInstants(): Int = read { getTimeseriesStore().size }

    override fun countEvents(): Int = read { getTimeseriesEventsStore().size }

    override fun getEntriesSubMap(fromEventInstant: Instant, fromInclusive: Boolean, toEventInstant: Instant, toInclusive: Boolean): NavigableMap<Instant, Collection<TimeseriesEntry<E>>> =
        read { storeToEntries(getTimeseriesStore().subMap(fromEventInstant, fromInclusive, toEventInstant, toInclusive)) }

    override fun getEntriesHeadMap(toEventInstant: Instant, toInclusive: Boolean): NavigableMap<Instant, Collection<TimeseriesEntry<E>>> =
        read { storeToEntries(getTimeseriesStore().headMap(toEventInstant, toInclusive)) }

    override fun getEntriesTailMap(fromEventInstant: Instant, fromInclusive: Boolean): NavigableMap<Instant, Collection<TimeseriesEntry<E>>> =
        read { storeToEntries(getTimeseriesStore().tailMap(fromEventInstant, fromInclusive)) }

    protected fun storeToEntries(store: NavigableMap<Instant, S>?, descending: Boolean = false): NavigableMap<Instant, Collection<TimeseriesEntry<E>>> {
        val entries: NavigableMap<Instant, Collection<TimeseriesEntry<E>>> = if(!descending) TreeMap(java.util.Comparator.naturalOrder()) else TreeMap(
            Comparator.reverseOrder())
        store?.forEach { (instant, set) ->
            set?.forEach { eventId ->
                getTimeseriesEventsStore()?.get(eventId)?.let {
                    entries.compute(instant) { _, v ->
                        v?.plus(timeseriesEntryOf(instant, it, eventId))
                            ?: listOf(timeseriesEntryOf(instant, it, eventId))
                    }
                }
            }
        }
        return entries
    }

    override fun start(): Collection<TimeseriesEntry<E>>? = read {
        return@read getTimeseriesStore().firstEntry()
            ?.let { (instant, set) ->
                set?.stream()
                    ?.map { eventId ->  getTimeseriesEventsStore()[eventId]?.let { timeseriesEntryOf(instant, it, eventId) } }
                    ?.filter { it != null }
                    ?.collect(Collectors.toCollection { mutableSetOf() })
            }
    }

    override fun end(): Collection<TimeseriesEntry<E>>? = read {
        return@read getTimeseriesStore().lastEntry()
            ?.let { (instant, set) ->
                set?.stream()
                    ?.map { eventId ->  getTimeseriesEventsStore()[eventId]?.let { timeseriesEntryOf(instant, it, eventId) } }
                    ?.filter { it != null }
                    ?.collect(Collectors.toCollection { mutableSetOf() })
            }
    }

    override fun iterator(): Iterator<Collection<TimeseriesEntry<E>>> = read { storeToEntries(getTimeseriesStore())?.values?.iterator() ?: emptyIterator() }

    override fun descendingIterator(): Iterator<Collection<TimeseriesEntry<E>>> = read { storeToEntries(getTimeseriesStore(), true)?.values?.iterator() ?: emptyIterator() }

    override fun equals(other: Any?): Boolean = read {
        if (this === other) return@read true
        if (javaClass != other?.javaClass) return@read false

        other as AbstractTimeseries<*, *, *>

        if(getTimeseriesStore() != other.getTimeseriesStore()) return@read false
        if(getTimeseriesEventsStore() != other.getTimeseriesEventsStore()) return@read false

        return@read true
    }

    override fun hashCode(): Int = read {
        var result = getTimeseriesStore().hashCode()
        result = 31 * result + getTimeseriesEventsStore().hashCode()
        return@read result
    }

    protected open fun <T> read(action: ()->T): T = action() //passthru

    protected open fun <T> write(action: ()->T): T = action() //passthru
}