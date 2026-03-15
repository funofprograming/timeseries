package io.github.funofprograming.timeseries.impl

import io.github.funofprograming.timeseries.MutableTimeseries
import io.github.funofprograming.timeseries.Timeseries
import io.github.funofprograming.timeseries.TimeseriesEntry
import io.github.funofprograming.timeseries.timeseriesEntryOf
import java.time.Instant
import java.util.NavigableMap
import java.util.TreeMap
import java.util.UUID
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

open class MutableTimeseriesImpl<E>: AbstractTimeseries<E, MutableSet<UUID>, MutableMap<UUID, E>>, MutableTimeseries<E> {

    private val timeseriesStore: NavigableMap<Instant, MutableSet<UUID>> = TreeMap()
    private val timeseriesEventsStore: MutableMap<UUID, E> = mutableMapOf()
    private val readWriteLock: ReentrantReadWriteLock = ReentrantReadWriteLock()

    override fun getTimeseriesStore(): NavigableMap<Instant, MutableSet<UUID>> = timeseriesStore
    override fun getTimeseriesEventsStore(): MutableMap<UUID, E> = timeseriesEventsStore

    constructor(): this(TreeMap())

    constructor(timeSeries: Timeseries<E>) : this(timeSeries.getAll())

    constructor(entries:Collection<TimeseriesEntry<E>>) {
        add(entries)
    }

    constructor(entries:NavigableMap<Instant, Collection<TimeseriesEntry<E>>>) {
        add(entries.values.stream().flatMap { it.stream() }.toList())
    }

    override fun plus(entry: TimeseriesEntry<E>, overwrite: Boolean): MutableTimeseries<E> {
        add(entry, overwrite)
        return this
    }

    override fun plus(entries: Collection<TimeseriesEntry<E>>, overwrite: Boolean): MutableTimeseries<E> {
        add(entries, overwrite)
        return this
    }

    override fun minus(entry: TimeseriesEntry<E>): MutableTimeseries<E> {
        remove(entry)
        return this
    }

    override fun minus(entries: Collection<TimeseriesEntry<E>>): MutableTimeseries<E> {
        remove(entries)
        return this
    }

    override fun minus(eventInstant: Instant): MutableTimeseries<E> {
        remove(eventInstant)
        return this
    }

    override fun add(entry: TimeseriesEntry<E>, overwrite: Boolean): UUID = write {
        val eventIdForExisting: UUID? = timeseriesEventsStore.filter { it.value?.equals(entry.event) ?: false }?.map { it.key }?.firstOrNull()
        if (overwrite || eventIdForExisting == null) {
            val eventIdToStore = eventIdForExisting ?: entry.eventId ?: UUID.randomUUID()
            timeseriesStore.computeIfAbsent(entry.eventInstant) { mutableSetOf() }.add(eventIdToStore)
            timeseriesEventsStore[eventIdToStore] = entry.event
            return@write eventIdToStore
        }

        return@write eventIdForExisting
    }

    override fun add(entries: Collection<TimeseriesEntry<E>>,overwrite: Boolean): Collection<UUID> = write {
        return@write entries.stream().map { entry -> add(entry, overwrite) }.toList()
    }

    override fun get(eventInstant:Instant, eventId:UUID): TimeseriesEntry<E>? = read {
        if(getTimeseriesStore()[eventInstant]?.contains(eventId)?:false) {
            return@read getTimeseriesEventsStore()[eventId]?.let { timeseriesEntryOf(eventInstant, it, eventId) }
        }
        return@read null
    }

    override fun remove(entry: TimeseriesEntry<E>): Boolean = write {
        val sizeBefore = timeseriesEventsStore.size
        timeseriesStore[entry.eventInstant]
        val uuidToRemove = timeseriesEventsStore.filter { it.value?.equals(entry.event) ?: false }?.map { it.key }?.firstOrNull() //event exists
        val instantToRemove = entry.eventInstant
        val uuidStored = timeseriesStore[instantToRemove]?.contains(uuidToRemove) ?: false //for same instant

        if(uuidStored && uuidToRemove != null) {
            timeseriesEventsStore.remove(uuidToRemove)
            timeseriesStore[instantToRemove]?.remove(uuidToRemove)
        }
        val sizeAfter = timeseriesEventsStore.size
        return@write sizeBefore > sizeAfter
    }

    override fun remove(entries: Collection<TimeseriesEntry<E>>): Boolean = write {
        return@write entries.stream().map { entry -> remove(entry) }.reduce(true) { a, b -> a && b }
    }

    override fun remove(eventInstant: Instant): Boolean = write {
        val sizeBefore = timeseriesEventsStore.size
        val uuidsToRemove = timeseriesStore[eventInstant]
        uuidsToRemove?.let { it.stream().forEach { uuid -> timeseriesEventsStore.remove(uuid) } }
        timeseriesStore.remove(eventInstant)
        val sizeAfter = timeseriesEventsStore.size
        return@write sizeBefore > sizeAfter
    }

    override fun clear() = write {
        timeseriesEventsStore.clear()
        timeseriesStore.clear()
    }

    override fun contains(eventId:UUID): Boolean = read { getTimeseriesEventsStore().contains(eventId) }

    override fun <T> read(action: ()->T):T = readWriteLock.read { action() }
    override fun <T> write(action: ()->T):T = readWriteLock.write { action() }
}