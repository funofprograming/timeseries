package io.github.funofprograming.timeseries

import io.github.funofprograming.timeseries.impl.MutableTimeseriesImpl
import io.github.funofprograming.timeseries.impl.TimeseriesEntryImpl
import io.github.funofprograming.timeseries.impl.TimeseriesImpl
import java.time.Instant
import java.util.NavigableMap
import java.util.UUID

/**
 * Create a [TimeseriesEntry]
 *
 * @param eventInstant Instant for which entry is to be created
 * @param event Event for which entry is to be created
 * @param eventId EventId (optional) if set from Timeseries
 */
fun <E> timeseriesEntryOf(eventInstant: Instant, event:E, eventId: UUID?=null) = TimeseriesEntryImpl(eventInstant, event, eventId)

/**
 * Create an empty [Timeseries]
 */
fun <E> timeseriesOf(): Timeseries<E> = TimeseriesImpl()

/**
 * Create a [Timeseries] by copying entries from another [Timeseries]
 *
 * @param timeSeries Another [Timeseries] that is to be copied
 */
fun <E> timeseriesOf(timeSeries: Timeseries<E>): Timeseries<E> = TimeseriesImpl(timeSeries)

/**
 * Create a [Timeseries] from a [TimeseriesEntry]
 *
 * @param entry [TimeseriesEntry] to be used for creating [Timeseries]
 */
fun <E> timeseriesOf(entry: TimeseriesEntry<E>): Timeseries<E> = timeseriesOf(setOf(entry))

/**
 * Create a [Timeseries] from a `Collection` of [TimeseriesEntry]
 *
 * @param entries `Collection` of [TimeseriesEntry] to be used for creating [Timeseries]
 */
fun <E> timeseriesOf(entries: Collection<TimeseriesEntry<E>>): Timeseries<E> = TimeseriesImpl(entries)

/**
 * Create a [Timeseries] from a `NavigableMap` of `Collection<TimeseriesEntry<E>>` grouped by `Instant`
 *
 * @param entries `NavigableMap` of `Collection<TimeseriesEntry<E>>` grouped by `Instant` to be used for creating [Timeseries]
 */
fun <E> timeseriesOf(entries: NavigableMap<Instant, Collection<TimeseriesEntry<E>>>): Timeseries<E> = TimeseriesImpl(entries)

/**
 * Create an empty [MutableTimeseries]
 */
fun <E> mutableTimeseriesOf(): MutableTimeseries<E> = MutableTimeseriesImpl()

/**
 * Create a [MutableTimeseries] by copying entries from another [Timeseries]
 *
 * @param timeSeries Another [Timeseries] that is to be copied
 */
fun <E> mutableTimeseriesOf(timeSeries: Timeseries<E>): MutableTimeseries<E> = MutableTimeseriesImpl(timeSeries)

/**
 * Create a [MutableTimeseries] from a [TimeseriesEntry]
 *
 * @param entry [TimeseriesEntry] to be used for creating [MutableTimeseries]
 */
fun <E> mutableTimeseriesOf(entry: TimeseriesEntry<E>): MutableTimeseries<E> = mutableTimeseriesOf(setOf(entry))

/**
 * Create a [MutableTimeseries] from a `Collection` of [TimeseriesEntry]
 *
 * @param entries `Collection` of [TimeseriesEntry] to be used for creating [MutableTimeseries]
 */
fun <E> mutableTimeseriesOf(entries: Collection<TimeseriesEntry<E>>): MutableTimeseries<E> = MutableTimeseriesImpl(entries)

/**
 * Create a [MutableTimeseries] from a `NavigableMap` of `Collection<TimeseriesEntry<E>>` grouped by `Instant`
 *
 * @param entries `NavigableMap` of `Collection<TimeseriesEntry<E>>` grouped by `Instant` to be used for creating [MutableTimeseries]
 */
fun <E> mutableTimeseriesOf(entries: NavigableMap<Instant, Collection<TimeseriesEntry<E>>>): MutableTimeseries<E> = MutableTimeseriesImpl(entries)