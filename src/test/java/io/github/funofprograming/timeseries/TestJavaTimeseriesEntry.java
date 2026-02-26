package io.github.funofprograming.timeseries;

import org.junit.jupiter.api.Test;
import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static io.github.funofprograming.timeseries.TimeseriesBuildersKt.*;

class TestJavaTimeseriesEntry {

    @Test
    void testInitializeTimeseriesEntry_nullEventId() {
        Instant instant = Instant.now();
        Integer event = 1;
        TimeseriesEntry<Integer> timeseriesEntry = timeseriesEntryOf(instant, event, null);
        assertNotNull(timeseriesEntry);
        assertEquals(instant, timeseriesEntry.getEventInstant());
        assertEquals(event, timeseriesEntry.getEvent());
        assertNull(timeseriesEntry.getEventId());
    }

    @Test
    void testInitializeTimeseriesEntry_nonNullEventId() {
        Instant instant = Instant.now();
        Integer event = 1;
        UUID eventId = UUID.randomUUID();
        TimeseriesEntry<Integer> timeseriesEntry = timeseriesEntryOf(instant, event, eventId);
        assertNotNull(timeseriesEntry);
        assertEquals(instant, timeseriesEntry.getEventInstant());
        assertEquals(event, timeseriesEntry.getEvent());
        assertEquals(eventId, timeseriesEntry.getEventId());
    }
}
