package io.github.funofprograming.timeseries;

import org.junit.jupiter.api.Test;
import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static io.github.funofprograming.timeseries.TimeseriesBuildersKt.*;

class TestJavaTimeseries {

    @Test
    void testInitializeTimeseries_empty() {
        Timeseries<Integer> timeseries = timeseriesOf();
        assertNotNull(timeseries);
    }

    @Test
    void testInitializeTimeseries_oneEntry() {
        Timeseries<Integer> timeseries = timeseriesOf(
                timeseriesEntryOf(Instant.now(), 1, null)
        );
        assertNotNull(timeseries);
        assertEquals(1, timeseries.countInstants());
        assertEquals(1, timeseries.countEvents());
    }

    @Test
    void testInitializeTimeseries_entryCollection_differentTime() {
        Timeseries<Integer> timeseries = timeseriesOf(
                Set.of(
                        timeseriesEntryOf(Instant.now(), 1, null),
                        timeseriesEntryOf(Instant.now().plusSeconds(1), 2, null),
                        timeseriesEntryOf(Instant.now().plusSeconds(2), 3, null)
                )
        );
        assertNotNull(timeseries);
        assertEquals(3, timeseries.countInstants());
        assertEquals(3, timeseries.countEvents());
    }

    @Test
    void testInitializeTimeseries_entryCollection_sameTime() {
        Instant instant = Instant.now();
        Timeseries<Integer> timeseries = timeseriesOf(
                Set.of(
                        timeseriesEntryOf(instant, 1, null),
                        timeseriesEntryOf(instant, 2, null),
                        timeseriesEntryOf(instant, 3, null)
                )
        );
        assertNotNull(timeseries);
        assertEquals(1, timeseries.countInstants());
        assertEquals(3, timeseries.countEvents());
    }

    @Test
    void testInitializeTimeseries_entryMap() {
        Instant instant = Instant.now();
        Collection<TimeseriesEntry<Integer>> timeseriesEntrySet = Set.of(
                timeseriesEntryOf(instant, 1, null),
                timeseriesEntryOf(instant, 2, null),
                timeseriesEntryOf(instant, 3, null)
        );

        NavigableMap<Instant, Collection<TimeseriesEntry<Integer>>> timeseriesEntryMap = new TreeMap<>();
        timeseriesEntryMap.put(instant, timeseriesEntrySet);

        Timeseries<Integer> timeseries = timeseriesOf(timeseriesEntryMap);

        assertNotNull(timeseries);
        assertEquals(1, timeseries.countInstants());
        assertEquals(3, timeseries.countEvents());
    }

    @Test
    void testInitializeTimeseries_copy() {
        Instant instant = Instant.now();
        Timeseries<Integer> timeseries = timeseriesOf(
                Set.of(
                        timeseriesEntryOf(instant, 1, null),
                        timeseriesEntryOf(instant, 2, null),
                        timeseriesEntryOf(instant, 3, null)
                )
        );

        Timeseries<Integer> timeseriesCopy = timeseriesOf(timeseries);
        assertNotNull(timeseriesCopy);
        assertNotSame(timeseries, timeseriesCopy); // different objects
        assertEquals(timeseries, timeseriesCopy);  // same entries
        assertEquals(1, timeseriesCopy.countInstants());
        assertEquals(3, timeseriesCopy.countEvents());
    }

    @Test
    void testPlus_singleEntry_overwriteFalse() {
        Timeseries<Integer> timeseries = timeseriesOf(timeseriesEntryOf(Instant.now(), 1, null));

        Timeseries<Integer> timeseries2 = timeseries.plus(timeseriesEntryOf(Instant.now(), 2, null), false);

        assertNotNull(timeseries2);
        assertNotSame(timeseries, timeseries2);
        assertNotEquals(timeseries, timeseries2);
        assertEquals(2, timeseries2.countInstants());
        assertEquals(2, timeseries2.countEvents());
    }

    @Test
    void testPlus_singleEntry_overwriteTrue() {
        Instant instant = Instant.now();
        Timeseries<Integer> timeseries = timeseriesOf(timeseriesEntryOf(instant, 1, null));

        Timeseries<Integer> timeseries2 = timeseries.plus(timeseriesEntryOf(instant, 1, null), true);

        assertNotNull(timeseries2);
        assertNotSame(timeseries, timeseries2);
        assertEquals(timeseries, timeseries2); // same entries because overwrite=true
        assertEquals(1, timeseries2.countInstants());
        assertEquals(1, timeseries2.countEvents());
    }

    @Test
    void testPlus_collectionEntry_overwriteFalse() {
        Timeseries<Integer> timeseries = timeseriesOf(timeseriesEntryOf(Instant.now(), 1, null));

        Timeseries<Integer> timeseries2 = timeseries.plus(Set.of(
                timeseriesEntryOf(Instant.now(), 2, null),
                timeseriesEntryOf(Instant.now(), 3, null),
                timeseriesEntryOf(Instant.now(), 4, null)
        ), false);

        assertNotNull(timeseries2);
        assertEquals(4, timeseries2.countInstants());
        assertEquals(4, timeseries2.countEvents());
    }

    @Test
    void testPlus_collectionEntry_overwriteTrue() {
        Instant instant = Instant.now();
        Timeseries<Integer> timeseries = timeseriesOf(Set.of(
                timeseriesEntryOf(instant, 2, null),
                timeseriesEntryOf(instant, 3, null),
                timeseriesEntryOf(instant, 4, null)
        ));

        Timeseries<Integer> timeseries2 = timeseries.plus(Set.of(
                timeseriesEntryOf(instant, 2, null),
                timeseriesEntryOf(instant, 5, null),
                timeseriesEntryOf(instant, 6, null)
        ), true);

        assertNotNull(timeseries2);
        assertEquals(1, timeseries2.countInstants());
        assertEquals(5, timeseries2.countEvents()); // one entry is overwritten, total 5
    }

    @Test
    void testGet() {
        Instant instant = Instant.now();
        Timeseries<Integer> timeseries = timeseriesOf(Set.of(
                timeseriesEntryOf(instant, 2, null),
                timeseriesEntryOf(instant, 3, null),
                timeseriesEntryOf(instant, 4, null)
        ));

        Collection<TimeseriesEntry<Integer>> instantEvents = timeseries.get(instant);
        assertNotNull(instantEvents);
        assertFalse(instantEvents.isEmpty());
        assertEquals(3, instantEvents.size());
        assertTrue(instantEvents.contains(timeseriesEntryOf(instant, 2, null)));
        assertFalse(instantEvents.contains(timeseriesEntryOf(instant, 5, null)));

        Collection<TimeseriesEntry<Integer>> instantEvents2 = timeseries.get(Instant.now());
        assertTrue(instantEvents2.isEmpty());
    }

    @Test
    void testGetAll() {
        Instant instant = Instant.now();
        Timeseries<Integer> timeseries = timeseriesOf(Set.of(
                timeseriesEntryOf(instant, 2, null),
                timeseriesEntryOf(instant, 3, null)
        ));

        Map<Instant, Collection<TimeseriesEntry<Integer>>> allEvents = timeseries.getAll();
        assertEquals(1, allEvents.size());
        assertTrue(allEvents.containsKey(instant));
    }

    @Test
    void testMinus_singleEntry() {
        Instant instant = Instant.now();
        Timeseries<Integer> timeseries = timeseriesOf(Set.of(
                timeseriesEntryOf(instant, 2, null),
                timeseriesEntryOf(instant, 3, null)
        ));

        Timeseries<Integer> timeseries2 = timeseries.minus(timeseriesEntryOf(instant, 1, null));
        assertEquals(2, timeseries2.countEvents()); // Not found, no change

        Timeseries<Integer> timeseries3 = timeseries.minus(timeseriesEntryOf(instant, 2, null));
        assertEquals(1, timeseries3.countEvents());
    }

    @Test
    void testMinus_instant() {
        Instant instant = Instant.now();
        Timeseries<Integer> timeseries = timeseriesOf(Set.of(
                timeseriesEntryOf(instant, 2, null)
        ));

        Timeseries<Integer> timeseries2 = timeseries.minus(instant);
        assertEquals(0, timeseries2.countInstants());
    }

    @Test
    void testGetEntriesSubMap_exclusive() {
        Instant i1 = Instant.parse("2024-01-01T00:00:00Z");
        Instant i2 = Instant.parse("2024-01-02T00:00:00Z");
        Instant i3 = Instant.parse("2024-01-03T00:00:00Z");

        Timeseries<Integer> timeseries = timeseriesOf(Set.of(
                timeseriesEntryOf(i1, 1, null),
                timeseriesEntryOf(i2, 2, null),
                timeseriesEntryOf(i3, 3, null)
        ));

        NavigableMap<Instant, Collection<TimeseriesEntry<Integer>>> subMap = timeseries.getEntriesSubMap(i1, i3);
        assertEquals(1, subMap.size());
        assertTrue(subMap.containsKey(i2));
    }

    @Test
    void testStart() {
        Instant i1 = Instant.parse("2024-01-01T00:00:00Z");
        Instant i2 = Instant.parse("2024-01-02T00:00:00Z");
        Timeseries<Integer> timeseries = timeseriesOf(Set.of(
                timeseriesEntryOf(i2, 2, null),
                timeseriesEntryOf(i1, 1, null)
        ));

        Collection<TimeseriesEntry<Integer>> startEntries = timeseries.start();
        assertNotNull(startEntries);
        assertTrue(startEntries.contains(timeseriesEntryOf(i1, 1, null)));
    }

    @Test
    void testIterator_ascending() {
        Instant i1 = Instant.parse("2024-01-01T00:00:00Z");
        Instant i2 = Instant.parse("2024-01-02T00:00:00Z");
        Timeseries<Integer> timeseries = timeseriesOf(Set.of(
                timeseriesEntryOf(i2, 2, null),
                timeseriesEntryOf(i1, 1, null)
        ));

        Iterator<Collection<TimeseriesEntry<Integer>>> iterator = timeseries.iterator();
        assertTrue(iterator.hasNext());
        assertTrue(iterator.next().contains(timeseriesEntryOf(i1, 1, null)));
        assertTrue(iterator.next().contains(timeseriesEntryOf(i2, 2, null)));
    }
}