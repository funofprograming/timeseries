package io.github.funofprograming.timeseries

import io.github.funofprograming.timeseries.impl.TimeseriesImpl
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import java.time.Instant
import java.util.UUID

class TestTimeseriesEntry {

    @Test
    fun testInitializeTimeseriesEntry_nullEventId() {
        val instant = Instant.now()
        val event = 1
        val timeseriesEntry: TimeseriesEntry<Int> = timeseriesEntryOf<Int>(instant, event)
        assertNotNull(timeseriesEntry)
        assertEquals(instant, timeseriesEntry.eventInstant)
        assertEquals(event, timeseriesEntry.event)
        assertNull(timeseriesEntry.eventId)
    }

    @Test
    fun testInitializeTimeseriesEntry_nonNullEventId() {
        val instant = Instant.now()
        val event = 1
        val eventId = UUID.randomUUID()
        val timeseriesEntry: TimeseriesEntry<Int> = timeseriesEntryOf<Int>(instant, event, eventId)
        assertNotNull(timeseriesEntry)
        assertEquals(instant, timeseriesEntry.eventInstant)
        assertEquals(event, timeseriesEntry.event)
        assertEquals(eventId, timeseriesEntry.eventId)
    }
}