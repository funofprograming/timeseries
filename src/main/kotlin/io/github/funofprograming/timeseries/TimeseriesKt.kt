package io.github.funofprograming.timeseries

import io.github.funofprograming.timeseries.impl.MutableTimeseriesImpl
import io.github.funofprograming.timeseries.impl.TimeseriesEntryImpl
import io.github.funofprograming.timeseries.impl.TimeseriesImpl
import java.time.Instant
import java.util.NavigableMap
import java.util.UUID

fun <E> timeseriesEntryOf(eventInstant: Instant, event:E, eventId: UUID?=null) = TimeseriesEntryImpl(eventInstant, event, eventId)

fun <E> timeseriesOf(): Timeseries<E> = TimeseriesImpl()

fun <E> timeseriesOf(timeSeries: Timeseries<E>): Timeseries<E> = TimeseriesImpl(timeSeries)

fun <E> timeseriesOf(entry: TimeseriesEntry<E>): Timeseries<E> = timeseriesOf(setOf(entry))

fun <E> timeseriesOf(entries: Collection<TimeseriesEntry<E>>): Timeseries<E> = TimeseriesImpl(entries)

fun <E> timeseriesOf(entries: NavigableMap<Instant, Collection<TimeseriesEntry<E>>>): Timeseries<E> = TimeseriesImpl(entries)

fun <E> mutableTimeseriesOf(): MutableTimeseries<E> = MutableTimeseriesImpl()

fun <E> mutableTimeseriesOf(timeSeries: Timeseries<E>): MutableTimeseries<E> = MutableTimeseriesImpl(timeSeries)

fun <E> mutableTimeseriesOf(entry: TimeseriesEntry<E>): MutableTimeseries<E> = mutableTimeseriesOf(setOf(entry))

fun <E> mutableTimeseriesOf(entries: Collection<TimeseriesEntry<E>>): MutableTimeseries<E> = MutableTimeseriesImpl(entries)

fun <E> mutableTimeseriesOf(entries: NavigableMap<Instant, Collection<TimeseriesEntry<E>>>): MutableTimeseries<E> = MutableTimeseriesImpl(entries)