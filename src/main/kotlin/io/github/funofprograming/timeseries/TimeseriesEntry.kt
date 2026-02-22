package io.github.funofprograming.timeseries

import java.time.Instant
import java.util.UUID

interface TimeseriesEntry<E>: Comparable<TimeseriesEntry<E>> {
    val event: E
    val eventInstant:Instant
    var eventId:UUID?
}