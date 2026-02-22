package io.github.funofprograming.timeseries.impl

import io.github.funofprograming.timeseries.Timeseries
import io.github.funofprograming.timeseries.TimeseriesEntry
import io.github.funofprograming.timeseries.timeseriesEntryOf
import java.time.Instant
import java.util.*
import java.util.Collections.emptyIterator
import java.util.stream.Collectors


open class TimeseriesImpl<E>: AbstractTimeseries<E, Set<UUID>, Map<UUID, E>> {

    private val timeseriesStore: NavigableMap<Instant, Set<UUID>>
    private val timeseriesEventsStore: Map<UUID, E>

    override fun getTimeseriesStore(): NavigableMap<Instant, Set<UUID>> = timeseriesStore
    override fun getTimeseriesEventsStore(): Map<UUID, E> = timeseriesEventsStore

    constructor(): this(TreeMap())

    constructor(timeSeries: Timeseries<E>) : this(timeSeries.getAll())

    constructor(entries:Collection<TimeseriesEntry<E>>) {
        val timeseriesStoreLocal:NavigableMap<Instant, Set<UUID>> = TreeMap()
        val timeseriesEventsStoreLocal: MutableMap<UUID, E> = mutableMapOf()

        entries.forEach { event ->
            addEvent(event.eventInstant, event.event, event.eventId, timeseriesStoreLocal, timeseriesEventsStoreLocal)
        }

        timeseriesStore = Collections.unmodifiableNavigableMap(timeseriesStoreLocal)
        timeseriesEventsStore = timeseriesEventsStoreLocal.toMap()
    }

    constructor(entries:NavigableMap<Instant, Collection<TimeseriesEntry<E>>>) {
        val timeseriesStoreLocal:NavigableMap<Instant, Set<UUID>> = TreeMap()
        val timeseriesEventsStoreLocal: MutableMap<UUID, E> = mutableMapOf()

        entries.forEach { (instant, events) ->
            events.forEach { event ->
                event.eventId = addEvent(instant, event.event, event.eventId, timeseriesStoreLocal, timeseriesEventsStoreLocal)
            }
        }

        timeseriesStore = Collections.unmodifiableNavigableMap(timeseriesStoreLocal)
        timeseriesEventsStore = timeseriesEventsStoreLocal.toMap()
    }

    override fun plus(entry: TimeseriesEntry<E>, overwrite: Boolean): Timeseries<E> {
       return plus(setOf(entry), overwrite)
    }

    override fun plus(entries: Collection<TimeseriesEntry<E>>, overwrite: Boolean): Timeseries<E> {
        val entriesCurrent = getAll()
        entries.forEach {
            val eventInstant = it.eventInstant
            val eventEntryExisting = entriesCurrent[eventInstant]?.contains(it) ?: false
            if (overwrite || !eventEntryExisting) {
                val instantSet = entriesCurrent[eventInstant]?.plus(it) ?: setOf(it)
                entriesCurrent[eventInstant] = instantSet
            }
        }
        return TimeseriesImpl(entriesCurrent);
    }

    protected fun addEvent(
        eventInstant: Instant,
        event: E,
        eventId: UUID? = null,
        timeseriesStoreLocal: NavigableMap<Instant, Set<UUID>>,
        timeseriesEventsStoreLocal: MutableMap<UUID, E>
    ): UUID {
        val eventIdForExisting: UUID? = timeseriesEventsStoreLocal.filter { it.value?.equals(event) ?: false }?.map { it.key }?.firstOrNull()
        val eventIdToStore = eventIdForExisting ?: eventId ?: UUID.randomUUID()
        val instantSet = timeseriesStoreLocal[eventInstant]?.plus(eventIdToStore) ?: setOf(eventIdToStore)
        timeseriesStoreLocal[eventInstant] = instantSet
        timeseriesEventsStoreLocal[eventIdToStore] = event
        return eventIdToStore
    }

    override fun minus(entry: TimeseriesEntry<E>): Timeseries<E> {
        return minus(setOf(entry))
    }

    override fun minus(entries: Collection<TimeseriesEntry<E>>): Timeseries<E> {

        val entriesCurrent = getAll()
        entries.forEach {
            val instantSet = entriesCurrent[it.eventInstant]?.minus(it) ?: setOf()
            if(instantSet.isEmpty())
                entriesCurrent.remove(it.eventInstant)
            else
                entriesCurrent[it.eventInstant] = instantSet
        }
        return TimeseriesImpl(entriesCurrent)
    }

    override fun minus(eventInstant: Instant): Timeseries<E> {
        val entriesCurrent = getAll()
        entriesCurrent.remove(eventInstant)
        return TimeseriesImpl(entriesCurrent)
    }
}