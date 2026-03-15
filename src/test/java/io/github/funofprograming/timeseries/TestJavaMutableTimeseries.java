package io.github.funofprograming.timeseries;

import org.junit.jupiter.api.Test;
import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
// Assuming these are the static imports for the factory methods defined in Kotlin
import static io.github.funofprograming.timeseries.TimeseriesBuildersKt.*;

class TestJavaMutableTimeseries {

    @Test
    void testInitializeTimeseries_empty() {
        MutableTimeseries<Integer> timeseries = mutableTimeseriesOf();
        assertNotNull(timeseries);
    }

    @Test
    void testInitializeTimeseries_oneEntry() {
        MutableTimeseries<Integer> timeseries = mutableTimeseriesOf(timeseriesEntryOf(Instant.now(), 1, null));
        assertNotNull(timeseries);
        assertEquals(1, timeseries.countInstants());
        assertEquals(1, timeseries.countEvents());
    }

    @Test
    void testInitializeTimeseries_entryCollection_differentTime() {
        Set<TimeseriesEntry<Integer>> entries = new HashSet<>(Arrays.asList(
                timeseriesEntryOf(Instant.now(), 1, null),
                timeseriesEntryOf(Instant.now(), 2, null),
                timeseriesEntryOf(Instant.now(), 3, null)
        ));
        MutableTimeseries<Integer> timeseries = mutableTimeseriesOf(entries);
        assertNotNull(timeseries);
        assertEquals(3, timeseries.countInstants());
        assertEquals(3, timeseries.countEvents());
    }

    @Test
    void testInitializeTimeseries_entryCollection_sameTime() {
        Instant instant = Instant.now();
        Set<TimeseriesEntry<Integer>> entries = new HashSet<>(Arrays.asList(
                timeseriesEntryOf(instant, 1, null),
                timeseriesEntryOf(instant, 2, null),
                timeseriesEntryOf(instant, 3, null)
        ));
        MutableTimeseries<Integer> timeseries = mutableTimeseriesOf(entries);
        assertNotNull(timeseries);
        assertEquals(1, timeseries.countInstants());
        assertEquals(3, timeseries.countEvents());
    }

    @Test
    void testInitializeTimeseries_entryMap() {
        Instant instant = Instant.now();
        Set<TimeseriesEntry<Integer>> timeseriesEntrySet = new HashSet<>(Arrays.asList(
                timeseriesEntryOf(instant, 1, null),
                timeseriesEntryOf(instant, 2, null),
                timeseriesEntryOf(instant, 3, null)
        ));
        TreeMap<Instant, Collection<TimeseriesEntry<Integer>>> timeseriesEntryMap = new TreeMap<>();
        timeseriesEntryMap.put(instant, timeseriesEntrySet);

        MutableTimeseries<Integer> timeseries = mutableTimeseriesOf(timeseriesEntryMap);

        assertNotNull(timeseries);
        assertEquals(1, timeseries.countInstants());
        assertEquals(3, timeseries.countEvents());
    }

    @Test
    void testInitializeTimeseries_copy() {
        Instant instant = Instant.now();
        Set<TimeseriesEntry<Integer>> entries = new HashSet<>(Arrays.asList(
                timeseriesEntryOf(instant, 1, null),
                timeseriesEntryOf(instant, 2, null),
                timeseriesEntryOf(instant, 3, null)
        ));
        MutableTimeseries<Integer> timeseries = mutableTimeseriesOf(entries);

        MutableTimeseries<Integer> timeseriesCopy = mutableTimeseriesOf(timeseries);
        assertNotNull(timeseriesCopy);
        assertNotSame(timeseries, timeseriesCopy); // different objects
        assertEquals(timeseries, timeseriesCopy);   // same entries (equals)
        assertEquals(1, timeseriesCopy.countInstants());
        assertEquals(3, timeseriesCopy.countEvents());
    }

    @Test
    void testPlus_singleEntry_overwriteFalse() {
        MutableTimeseries<Integer> timeseries = mutableTimeseriesOf(timeseriesEntryOf(Instant.now(), 1, null));
        MutableTimeseries<Integer> timeseries2 = timeseries.plus(timeseriesEntryOf(Instant.now(), 2, null), false);

        assertSame(timeseries, timeseries2);
        assertEquals(2, timeseries2.countInstants());
        assertEquals(2, timeseries2.countEvents());
    }

    @Test
    void testPlus_collectionEntry_overwriteTrue() {
        Instant instant = Instant.now();
        Set<TimeseriesEntry<Integer>> entries = new HashSet<>(Arrays.asList(
                timeseriesEntryOf(instant, 2, null),
                timeseriesEntryOf(instant, 3, null),
                timeseriesEntryOf(instant, 4, null)
        ));
        MutableTimeseries<Integer> timeseries = mutableTimeseriesOf(entries);

        Set<TimeseriesEntry<Integer>> newEntries = new HashSet<>(Arrays.asList(
                timeseriesEntryOf(instant, 2, null),
                timeseriesEntryOf(instant, 5, null),
                timeseriesEntryOf(instant, 6, null)
        ));

        MutableTimeseries<Integer> timeseries2 = timeseries.plus(newEntries, true);
        assertEquals(1, timeseries2.countInstants());
        assertEquals(5, timeseries2.countEvents()); // one overwritten
    }

    @Test
    void testGet() {
        Instant instant = Instant.now();
        Set<TimeseriesEntry<Integer>> entries = new HashSet<>(Arrays.asList(
                timeseriesEntryOf(instant, 2, null),
                timeseriesEntryOf(instant, 3, null),
                timeseriesEntryOf(instant, 4, null)
        ));
        MutableTimeseries<Integer> timeseries = mutableTimeseriesOf(entries);

        Collection<TimeseriesEntry<Integer>> instantEvents = timeseries.get(instant);
        assertNotNull(instantEvents);
        assertFalse(instantEvents.isEmpty());
        assertEquals(3, instantEvents.size());
        assertTrue(instantEvents.contains(timeseriesEntryOf(instant, 2, null)));
    }

    @Test
    void testGet_instant_eventId() {
        Instant instant = Instant.now();
        MutableTimeseries<Integer> timeseries = mutableTimeseriesOf();
        Set<TimeseriesEntry<Integer>> entries = new HashSet<>(Arrays.asList(
                timeseriesEntryOf(instant, 2, null),
                timeseriesEntryOf(instant, 5, null)
        ));
        Collection<UUID> uuids = timeseries.add(entries, true);

        TimeseriesEntry<Integer> entry = timeseries.get(instant, uuids.iterator().next());
        assertNotNull(entry);
        assertEquals(2, entry.getEvent());
    }

    @Test
    void testMinus_instant() {
        Instant instant = Instant.now();
        MutableTimeseries<Integer> timeseries = mutableTimeseriesOf(new HashSet<>(Arrays.asList(
                timeseriesEntryOf(instant, 2, null)
        )));

        MutableTimeseries<Integer> timeseries2 = timeseries.minus(Instant.EPOCH);
        assertSame(timeseries, timeseries2);
        assertEquals(1, timeseries2.countInstants());

        MutableTimeseries<Integer> timeseries3 = timeseries.minus(instant);
        assertEquals(0, timeseries3.countInstants());
        assertEquals(0, timeseries3.countEvents());
    }

    @Test
    void testClear() {
        MutableTimeseries<Integer> timeseries = mutableTimeseriesOf(timeseriesEntryOf(Instant.now(), 2, null));
        timeseries.clear();
        assertEquals(0, timeseries.countInstants());
        assertEquals(0, timeseries.countEvents());
    }

    @Test
    void testGetEntriesSubMap_exclusive() {
        Instant i1 = Instant.parse("2024-01-01T00:00:00Z");
        Instant i2 = Instant.parse("2024-01-02T00:00:00Z");
        Instant i3 = Instant.parse("2024-01-03T00:00:00Z");

        MutableTimeseries<Integer> ts = mutableTimeseriesOf(new HashSet<>(Arrays.asList(
                timeseriesEntryOf(i1, 1, null)
                , timeseriesEntryOf(i2, 2, null)
                , timeseriesEntryOf(i3, 3, null)
        )));

        NavigableMap<Instant, Collection<TimeseriesEntry<Integer>>> subMap = ts.getEntriesSubMap(i1, i3);
        assertEquals(1, subMap.size());
        assertTrue(subMap.containsKey(i2));
        assertFalse(subMap.containsKey(i1));
    }

    @Test
    void testStart() {
        Instant i1 = Instant.parse("2024-01-01T00:00:00Z");
        Instant i2 = Instant.parse("2024-01-02T00:00:00Z");
        MutableTimeseries<Integer> ts = mutableTimeseriesOf(new HashSet<>(Arrays.asList(
                timeseriesEntryOf(i2, 2, null)
                , timeseriesEntryOf(i1, 1, null)
        )));

        Collection<TimeseriesEntry<Integer>> startEntries = ts.start();
        assertNotNull(startEntries);
        assertTrue(startEntries.contains(timeseriesEntryOf(i1, 1, null)));
    }

    @Test
    void testIterator_ascending() {
        Instant i1 = Instant.parse("2024-01-01T00:00:00Z");
        Instant i2 = Instant.parse("2024-01-02T00:00:00Z");
        MutableTimeseries<Integer> ts = mutableTimeseriesOf(new HashSet<>(Arrays.asList(
                timeseriesEntryOf(i2, 2, null)
                , timeseriesEntryOf(i1, 1, null)
        )));

        Iterator<Collection<TimeseriesEntry<Integer>>> iterator = ts.iterator();
        assertTrue(iterator.hasNext());
        assertTrue(iterator.next().contains(timeseriesEntryOf(i1, 1, null)));
        assertTrue(iterator.next().contains(timeseriesEntryOf(i2, 2, null)));
    }
}