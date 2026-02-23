package io.github.funofprograming.timeseries

import java.time.Instant
import java.util.*

/**
 * Interface for defining the Timeseries data structure. This interface defines an immutable data structure.
 */
interface Timeseries<E>: Iterable<Collection<TimeseriesEntry<E>>> {

    /**
     * Create new Timeseries with current entries plus the one provided as param
     *
     * @param entry
     * @param overwrite If new entry already exists in timeseries then should it be overwritten or not. Default: true
     *
     * @return New [Timeseries]
     */
    fun plus(entry: TimeseriesEntry<E>, overwrite: Boolean=true): Timeseries<E>

    /**
     * Create new Timeseries with current entries plus the ones provided as Collection param
     *
     * @param entries
     * @param overwrite If any of the new entries already exists in timeseries then should it be overwritten or not. Default: true
     *
     * @return New [Timeseries]
     */
    fun plus(entries: Collection<TimeseriesEntry<E>>, overwrite: Boolean=true): Timeseries<E>

    /**
     * Get all entries for given instant.
     *
     * @param eventInstant
     *
     * @return [Collection]<[TimeseriesEntry]>
     */
    fun get(eventInstant:Instant): Collection<TimeseriesEntry<E>>

    /**
     * Get all entries.
     *
     * @return [NavigableMap]<[Instant], [Collection]<[TimeseriesEntry]<E>>>
     */
    fun getAll(): NavigableMap<Instant, Collection<TimeseriesEntry<E>>>

    /**
     * Get all instants.
     *
     * @return [NavigableSet]<[Instant]>
     */
    fun getAllInstants(): NavigableSet<Instant>

    /**
     * Create new Timeseries with current entries minus the one provided as param
     *
     * @param entry
     *
     * @return New [Timeseries]
     */
    fun minus(entry: TimeseriesEntry<E>): Timeseries<E>

    /**
     * Create new Timeseries with current entries minus the ones provided as Collection param
     *
     * @param entries
     *
     * @return New [Timeseries]
     */
    fun minus(entries: Collection<TimeseriesEntry<E>>): Timeseries<E>

    /**
     * Create new Timeseries with current entries minus the ones for provided instant
     *
     * @param eventInstant
     *
     * @return New [Timeseries]
     */
    fun minus(eventInstant: Instant): Timeseries<E>

    /**
     * Whether any entries in timeseries for given instant
     *
     * @param eventInstant
     *
     * @return `true` if instant exists
     */
    fun contains(eventInstant:Instant): Boolean

    /**
     * Whether any entries in timeseries for given event. This one is a costly operation in terms of time
     *
     * @param event
     *
     * @return Boolean
     */
    fun contains(event:E): Boolean

    /**
     * Number of instants in timeseries
     *
     * @return count of instants
     */
    fun countInstants(): Int

    /**
     * Number of events in timeseries
     *
     * @return count of events across same or different instants
     */
    fun countEvents(): Int

    /**
     * Whether empty
     *
     * @return `true` if empty
     */
    fun isEmpty(): Boolean = countInstants() == 0

    /**
     * Whether not empty
     *
     * @return `true` if not empty
     */
    fun isNotEmpty(): Boolean = !isEmpty()

    /**
     * Get entries sub map based on from and to instant time period. Note that changes to this sub map will NOT alter the [Timeseries]
     *
     * @param fromEventInstant From instant
     * @param fromInclusive Whether to include `fromEventInstant` in the search. If true then returned sub map entries will all have instant >= fromEventInstant else instant > fromEventInstant.
     * @param toEventInstant To instant
     * @param toInclusive Whether to include `toEventInstant` in the search. If true then returned sub map entries will all have instant <= toEventInstant else instant < toEventInstant.
     *
     * @return [NavigableMap]<[Instant], [Collection]<[TimeseriesEntry]<E>>> of entries between fromEventInstant and toEventInstant
     */
    fun getEntriesSubMap(fromEventInstant: Instant, fromInclusive: Boolean, toEventInstant: Instant, toInclusive: Boolean): NavigableMap<Instant, Collection<TimeseriesEntry<E>>>

    /**
     * Get entries head map based start from the earliest instant in Timeseries and up to toEventInstant time period. Note that changes to this sub map will NOT alter the [Timeseries]
     *
     * @param toEventInstant To instant
     * @param toInclusive Whether to include `toEventInstant` in the search. If true then returned sub map entries will all have instant <= toEventInstant else instant < toEventInstant.
     *
     * @return [NavigableMap]<[Instant], [Collection]<[TimeseriesEntry]<E>>> of entries up to toEventInstant
     */
    fun getEntriesHeadMap(toEventInstant: Instant, toInclusive: Boolean): NavigableMap<Instant, Collection<TimeseriesEntry<E>>>

    /**
     * Get entries tail map based start from the fromEventInstant instant in Timeseries and up to latest instant in Timeseries. Note that changes to this sub map will NOT alter the [Timeseries]
     *
     * @param fromEventInstant From instant
     * @param fromInclusive Whether to include `fromEventInstant` in the search. If true then returned sub map entries will all have instant >= fromEventInstant else instant > fromEventInstant.
     *
     * @return [NavigableMap]<[Instant], [Collection]<[TimeseriesEntry]<E>>> of entries from fromEventInstant
     */
    fun getEntriesTailMap(fromEventInstant: Instant, fromInclusive: Boolean): NavigableMap<Instant, Collection<TimeseriesEntry<E>>>

    /**
     * Get entries sub map based on from and to instant time period. Note that changes to this sub map will NOT alter the [Timeseries]
     *
     * @param fromEventInstant From instant. Exclusive. Returned sub map entries will all have instant > fromEventInstant.
     * @param toEventInstant To instant. Exclusive. Returned sub map entries will all have instant < toEventInstant.
     *
     * @return [NavigableMap]<[Instant], [Collection]<[TimeseriesEntry]<E>>> of entries between fromEventInstant and toEventInstant
     */
    fun getEntriesSubMap(fromEventInstant: Instant, toEventInstant: Instant): NavigableMap<Instant, Collection<TimeseriesEntry<E>>> = getEntriesSubMap(fromEventInstant, false, toEventInstant, false)

    /**
     * Get entries head map based start from the earliest instant in Timeseries and up to toEventInstant time period. Note that changes to this sub map will NOT alter the [Timeseries]
     *
     * @param toEventInstant To instant. Exclusive. Returned sub map entries will all have instant < toEventInstant.
     *
     * @return [NavigableMap]<[Instant], [Collection]<[TimeseriesEntry]<E>>> of entries up to toEventInstant
     */
    fun getEntriesHeadMap(toEventInstant: Instant): NavigableMap<Instant, Collection<TimeseriesEntry<E>>> = getEntriesHeadMap(toEventInstant, false)

    /**
     * Get entries tail map based start from the fromEventInstant instant in Timeseries and up to latest instant in Timeseries. Note that changes to this sub map will NOT alter the [Timeseries]
     *
     * @param fromEventInstant From instant. Exclusive. Returned sub map entries will all have instant > fromEventInstant.
     *
     * @return [NavigableMap]<[Instant], [Collection]<[TimeseriesEntry]<E>>> of entries from fromEventInstant
     */
    fun getEntriesTailMap(fromEventInstant: Instant): NavigableMap<Instant, Collection<TimeseriesEntry<E>>> = getEntriesTailMap(fromEventInstant, false)

    /**
     * Get all entries for earliest (smallest) instant from Timeseries
     *
     * @return [Collection]<[TimeseriesEntry]<E>> for earliest instant
     */
    fun start(): Collection<TimeseriesEntry<E>>?

    /**
     * Get all entries for latest (largest) instant from Timeseries
     *
     * @return [Collection]<[TimeseriesEntry]<E>> for latest instant
     */
    fun end(): Collection<TimeseriesEntry<E>>?

    /**
     * Iterator over timeseries in descending order of instants from latest (largest) to earliest (smallest)
     *
     * @return [Iterator]<[Collection]<[TimeseriesEntry]<E>>> returning events corresponding to events from latest (largest) to earliest (smallest)
     */
    fun descendingIterator(): Iterator<Collection<TimeseriesEntry<E>>>
}