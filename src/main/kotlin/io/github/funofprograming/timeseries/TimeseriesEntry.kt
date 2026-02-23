package io.github.funofprograming.timeseries

import java.time.Instant
import java.util.UUID

/**
 * Interface representing the entry in and out from a [Timeseries] data structure
 *
 * @property event This represents some event of type `E`
 * @property eventInstant This represents the [Instant] for which the event is to be stored
 * @property eventId This represents the [unique id][UUID] assigned to each event inside the [Timeseries] data structure. This has no meaning outside Timeseries.
 */
interface TimeseriesEntry<E>: Comparable<TimeseriesEntry<E>> {
    val event: E
    val eventInstant:Instant
    var eventId:UUID?
}