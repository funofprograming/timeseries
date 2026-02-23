package io.github.funofprograming.timeseries

import java.time.Instant
import java.util.UUID

/**
 * Interface for defining the Timeseries data structure. This interface defines a mutable timeseries data structure.
 */
interface MutableTimeseries<E>: Timeseries<E> {

    /**
     * Add an entry to this [MutableTimeseries]
     *
     * @param entry
     * @param overwrite If new entry already exists in timeseries then should it be overwritten or not. Default: true
     *
     * @return This [MutableTimeseries]
     */
    override fun plus(entry: TimeseriesEntry<E>, overwrite: Boolean): Timeseries<E>

    /**
     * Add `Collection` of entries to this [MutableTimeseries]
     *
     * @param entries
     * @param overwrite If any of the new entries already exists in timeseries then should it be overwritten or not. Default: true
     *
     * @return This [MutableTimeseries]
     */
    override fun plus(entries: Collection<TimeseriesEntry<E>>, overwrite: Boolean): Timeseries<E>

    /**
     * Add an entry to this [MutableTimeseries]
     *
     * @param entry
     * @param overwrite If new entry already exists in timeseries then should it be overwritten or not. Default: true
     *
     * @return Event Id representing event inside Timeseries
     */
    fun add(entry: TimeseriesEntry<E>, overwrite: Boolean = true): UUID

    /**
     * Add `Collection` of entries to this [MutableTimeseries]
     *
     * @param entries
     * @param overwrite If any of the new entries already exists in timeseries then should it be overwritten or not. Default: true
     *
     * @return `Collection` of event ids representing events inside Timeseries
     */
    fun add(entries: Collection<TimeseriesEntry<E>>, overwrite: Boolean=true): Collection<UUID>

    /**
     * Get entry for given instant and id.
     *
     * @param eventInstant
     * @param eventId
     *
     * @return [TimeseriesEntry]
     */
    fun get(eventInstant: Instant, eventId:UUID): TimeseriesEntry<E>?

    /**
     * Remove an entry from this [MutableTimeseries]
     *
     * @param entry
     *
     * @return This [MutableTimeseries]
     */
    override fun minus(entry: TimeseriesEntry<E>): Timeseries<E>

    /**
     * Remove `Collection` of entries from this [MutableTimeseries]
     *
     * @param entries
     *
     * @return This [MutableTimeseries]
     */
    override fun minus(entries: Collection<TimeseriesEntry<E>>): Timeseries<E>

    /**
     * Remove all entries for given instant from this [MutableTimeseries]
     *
     * @param eventInstant
     *
     * @return This [MutableTimeseries]
     */
    override fun minus(eventInstant: Instant): Timeseries<E>

    /**
     * Remove an entry from this [MutableTimeseries]
     *
     * @param entry
     *
     * @return true if entries removed
     */
    fun remove(entry: TimeseriesEntry<E>): Boolean

    /**
     * Remove `Collection` of entries from this [MutableTimeseries]
     *
     * @param entries
     *
     * @return true if all entry removed
     */
    fun remove(entries: Collection<TimeseriesEntry<E>>): Boolean

    /**
     * Remove all entries for given instant from this [MutableTimeseries]
     *
     * @param eventInstant
     *
     * @return true if all entries for given instant are removed
     */
    fun remove(eventInstant: Instant): Boolean

    /**
     * Remove all entries for all instants from this [MutableTimeseries]
     */
    fun clear()

    /**
     * Whether any entries in timeseries for given eventId
     *
     * @param eventId
     *
     * @return Boolean
     */
    fun contains(eventId:UUID): Boolean
}