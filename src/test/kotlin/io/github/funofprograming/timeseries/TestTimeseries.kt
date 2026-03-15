package io.github.funofprograming.timeseries

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import java.time.Instant
import java.util.NavigableMap
import java.util.TreeMap
import kotlin.collections.containsKey

class TestTimeseries {

    @Test
    fun testInitializeTimeseries_empty() {
        val timeseries = timeseriesOf<Int>()
        assertNotNull(timeseries)
    }

    @Test
    fun testInitializeTimeseries_oneEntry() {
        val timeseries = timeseriesOf<Int>(timeseriesEntryOf(Instant.now(), 1))
        assertNotNull(timeseries)
        assertEquals(1, timeseries.countInstants())
        assertEquals(1, timeseries.countEvents())
    }

    @Test
    fun testInitializeTimeseries_entryCollection_differentTime() {
        val timeseries = timeseriesOf<Int>(
            setOf(
                timeseriesEntryOf(Instant.now(), 1)
                , timeseriesEntryOf(Instant.now(), 2)
                , timeseriesEntryOf(Instant.now(), 3)
        ))
        assertNotNull(timeseries)
        assertEquals(3, timeseries.countInstants())
        assertEquals(3, timeseries.countEvents())
    }

    @Test
    fun testInitializeTimeseries_entryCollection_sameTime() {
        val instant = Instant.now()
        val timeseries = timeseriesOf<Int>(
            setOf(
                timeseriesEntryOf(instant, 1)
                , timeseriesEntryOf(instant, 2)
                , timeseriesEntryOf(instant, 3)
            ))
        assertNotNull(timeseries)
        assertEquals(1, timeseries.countInstants())
        assertEquals(3, timeseries.countEvents())
    }

    @Test
    fun testInitializeTimeseries_entryMap() {
        val instant = Instant.now()
        val timeseriesEntrySet = setOf(
            timeseriesEntryOf(instant, 1)
            , timeseriesEntryOf(instant, 2)
            , timeseriesEntryOf(instant, 3)
        )
        val timeseriesEntryMap = TreeMap<Instant, Collection<TimeseriesEntry<Int>>>()
        timeseriesEntryMap[instant] = timeseriesEntrySet
        val timeseries = timeseriesOf<Int>(timeseriesEntryMap)

        assertNotNull(timeseries)
        assertEquals(1, timeseries.countInstants())
        assertEquals(3, timeseries.countEvents())
    }

    @Test
    fun testInitializeTimeseries_copy() {
        val instant = Instant.now()
        val timeseries = timeseriesOf<Int>(
            setOf(
                timeseriesEntryOf(instant, 1)
                , timeseriesEntryOf(instant, 2)
                , timeseriesEntryOf(instant, 3)
            ))
        assertNotNull(timeseries)
        assertEquals(1, timeseries.countInstants())
        assertEquals(3, timeseries.countEvents())

        val timeseriesCopy = timeseriesOf(timeseries)
        assertNotNull(timeseriesCopy)
        assertFalse { timeseriesCopy === timeseries } //different objects
        assertTrue { timeseriesCopy == timeseries } //same entries
        assertEquals(1, timeseriesCopy.countInstants())
        assertEquals(3, timeseriesCopy.countEvents())
    }

    @Test
    fun testPlus_singleEntry_overwriteFalse() {
        val timeseries = timeseriesOf<Int>(timeseriesEntryOf(Instant.now(), 1))
        assertNotNull(timeseries)
        assertEquals(1, timeseries.countInstants())
        assertEquals(1, timeseries.countEvents())
        val timeseries2 = timeseries.plus((timeseriesEntryOf(Instant.now(), 2)), false)
        assertNotNull(timeseries2)
        assertFalse { timeseries2 === timeseries } //different objects
        assertFalse { timeseries2 == timeseries } //different objects
        assertEquals(2, timeseries2.countInstants())
        assertEquals(2, timeseries2.countEvents())
    }

    @Test
    fun testPlus_singleEntry_overwriteTrue() {
        val instant = Instant.now()
        val timeseries = timeseriesOf<Int>(timeseriesEntryOf(instant, 1))
        assertNotNull(timeseries)
        assertEquals(1, timeseries.countInstants())
        assertEquals(1, timeseries.countEvents())
        val timeseries2 = timeseries.plus((timeseriesEntryOf(instant, 1)))
        assertNotNull(timeseries2)
        assertFalse { timeseries2 === timeseries } //different objects
        assertTrue { timeseries2 == timeseries } //same entries because overwrite=true
        assertEquals(1, timeseries2.countInstants())
        assertEquals(1, timeseries2.countEvents())
    }

    @Test
    fun testPlus_collectionEntry_overwriteFalse() {
        val timeseries = timeseriesOf<Int>(timeseriesEntryOf(Instant.now(), 1))
        assertNotNull(timeseries)
        assertEquals(1, timeseries.countInstants())
        assertEquals(1, timeseries.countEvents())
        val timeseries2 = timeseries.plus(setOf(
                                                            timeseriesEntryOf(Instant.now(), 2)
                                                            , timeseriesEntryOf(Instant.now(), 3)
                                                            , timeseriesEntryOf(Instant.now(), 4))
                                , false)
        assertNotNull(timeseries2)
        assertFalse { timeseries2 === timeseries } //different objects
        assertFalse { timeseries2 == timeseries } //different objects
        assertEquals(4, timeseries2.countInstants())
        assertEquals(4, timeseries2.countEvents())
    }

    @Test
    fun testPlus_collectionEntry_overwriteTrue() {
        val instant = Instant.now()
        val timeseries = timeseriesOf<Int>(setOf(
                                                            timeseriesEntryOf(instant, 2)
                                                            , timeseriesEntryOf(instant, 3)
                                                            , timeseriesEntryOf(instant, 4)))
        assertNotNull(timeseries)
        assertEquals(1, timeseries.countInstants())
        assertEquals(3, timeseries.countEvents())
        val timeseries2 = timeseries.plus(setOf(
                                                            timeseriesEntryOf(instant, 2)
                                                            , timeseriesEntryOf(instant, 5)
                                                            , timeseriesEntryOf(instant, 6)))
        assertNotNull(timeseries2)
        assertFalse { timeseries2 === timeseries } //different objects
        assertFalse { timeseries2 == timeseries } //different objects
        assertEquals(1, timeseries2.countInstants())
        assertEquals(5, timeseries2.countEvents()) //one entry is overwritten so total 5 events instead of 6
    }

    @Test
    fun testGet() {
        val instant = Instant.now()
        val timeseries = timeseriesOf<Int>(setOf(
            timeseriesEntryOf(instant, 2)
            , timeseriesEntryOf(instant, 3)
            , timeseriesEntryOf(instant, 4)))
        assertNotNull(timeseries)
        assertEquals(1, timeseries.countInstants())
        assertEquals(3, timeseries.countEvents())
        val instantEvents = timeseries.get(instant)
        assertNotNull(instantEvents)
        assertTrue { instantEvents.isNotEmpty() }
        assertTrue { instantEvents.size == 3 }
        assertTrue { instantEvents.contains(timeseriesEntryOf(instant, 2)) }
        assertTrue { instantEvents.contains(timeseriesEntryOf(instant, 3)) }
        assertTrue { instantEvents.contains(timeseriesEntryOf(instant, 4)) }
        assertFalse { instantEvents.contains(timeseriesEntryOf(instant, 5)) }

        val instantEvents2 = timeseries.get(Instant.now())
        assertNotNull(instantEvents2)
        assertTrue { instantEvents2.isEmpty() }
    }

    @Test
    fun testGetAll() {
        val instant = Instant.now()
        val timeseries = timeseriesOf<Int>(setOf(
            timeseriesEntryOf(instant, 2)
            , timeseriesEntryOf(instant, 3)
            , timeseriesEntryOf(instant, 4)))
        assertNotNull(timeseries)
        assertEquals(1, timeseries.countInstants())
        assertEquals(3, timeseries.countEvents())
        val instantEvents = timeseries.getAll()
        assertNotNull(instantEvents)
        assertTrue { instantEvents.isNotEmpty() }
        assertTrue { instantEvents.size == 1 }
        assertTrue { instantEvents.keys.contains(instant) }
        assertTrue { instantEvents[instant]?.contains(timeseriesEntryOf(instant, 2)) ?: false }
        assertTrue { instantEvents[instant]?.contains(timeseriesEntryOf(instant, 3)) ?: false }
        assertTrue { instantEvents[instant]?.contains(timeseriesEntryOf(instant, 4)) ?: false }
    }

    @Test
    fun getAllInstants() {
        val instant = Instant.now()
        val timeseries = timeseriesOf<Int>(setOf(
            timeseriesEntryOf(instant, 2)
            , timeseriesEntryOf(instant, 3)
            , timeseriesEntryOf(instant, 4)))
        assertNotNull(timeseries)
        assertEquals(1, timeseries.countInstants())
        assertEquals(3, timeseries.countEvents())
        val instants = timeseries.getAllInstants()
        assertNotNull(instants)
        assertTrue { instants.isNotEmpty() }
        assertTrue { instants.size == 1 }
        assertTrue { instants.contains(instant) }
    }

    @Test
    fun testMinus_singleEntry() {
        val instant = Instant.now()
        val timeseries = timeseriesOf<Int>(setOf(
            timeseriesEntryOf(instant, 2)
            , timeseriesEntryOf(instant, 3)
            , timeseriesEntryOf(instant, 4)))
        assertNotNull(timeseries)
        assertEquals(1, timeseries.countInstants())
        assertEquals(3, timeseries.countEvents())
        val timeseries2 = timeseries.minus((timeseriesEntryOf(instant, 1)))
        assertNotNull(timeseries2)
        assertFalse { timeseries2 === timeseries } //different objects
        assertTrue { timeseries2 == timeseries } //same entries because minus entry does not exist
        assertEquals(1, timeseries2.countInstants())
        assertEquals(3, timeseries2.countEvents())
        val timeseries3 = timeseries.minus((timeseriesEntryOf(instant, 2)))
        assertFalse { timeseries3 === timeseries } //different objects
        assertFalse { timeseries3 == timeseries } //different objects
        assertEquals(1, timeseries3.countInstants())
        assertEquals(2, timeseries3.countEvents())
    }

    @Test
    fun testMinus_collectionEntry() {
        val instant = Instant.now()
        val timeseries = timeseriesOf<Int>(setOf(
            timeseriesEntryOf(instant, 2)
            , timeseriesEntryOf(instant, 3)
            , timeseriesEntryOf(instant, 4)))
        assertNotNull(timeseries)
        assertEquals(1, timeseries.countInstants())
        assertEquals(3, timeseries.countEvents())
        val timeseries2 = timeseries.minus(setOf(
            timeseriesEntryOf(instant, 1)
            , timeseriesEntryOf(instant, 7)))
        assertNotNull(timeseries2)
        assertFalse { timeseries2 === timeseries } //different objects
        assertTrue { timeseries2 == timeseries } //same entries because minus entry does not exist
        assertEquals(1, timeseries2.countInstants())
        assertEquals(3, timeseries2.countEvents())
        val timeseries3 = timeseries.minus(setOf(timeseriesEntryOf(instant, 2)
                                                            , timeseriesEntryOf(instant, 3)))
        assertFalse { timeseries3 === timeseries } //different objects
        assertFalse { timeseries3 == timeseries } //different objects
        assertEquals(1, timeseries3.countInstants())
        assertEquals(1, timeseries3.countEvents())
    }

    @Test
    fun testMinus_instant() {
        val instant = Instant.now()
        val timeseries = timeseriesOf<Int>(setOf(
            timeseriesEntryOf(instant, 2)
            , timeseriesEntryOf(instant, 3)
            , timeseriesEntryOf(instant, 4)))
        assertNotNull(timeseries)
        assertEquals(1, timeseries.countInstants())
        assertEquals(3, timeseries.countEvents())
        val timeseries2 = timeseries.minus(Instant.EPOCH)
        assertNotNull(timeseries2)
        assertFalse { timeseries2 === timeseries } //different objects
        assertTrue { timeseries2 == timeseries } //same entries because minus entry does not exist
        assertEquals(1, timeseries2.countInstants())
        assertEquals(3, timeseries2.countEvents())
        val timeseries3 = timeseries.minus(instant)
        assertFalse { timeseries3 === timeseries } //different objects
        assertFalse { timeseries3 == timeseries } //different objects
        assertEquals(0, timeseries3.countInstants())
        assertEquals(0, timeseries3.countEvents())
    }

    @Test
    fun testContains_instant() {
        val instant = Instant.now()
        val timeseries = timeseriesOf<Int>(
            setOf(
                timeseriesEntryOf(instant, 2)
                , timeseriesEntryOf(instant, 3)
                , timeseriesEntryOf(instant, 4)
            )
        )
        assertNotNull(timeseries)
        assertEquals(1, timeseries.countInstants())
        assertEquals(3, timeseries.countEvents())
        assertTrue { timeseries.contains(instant) }
        assertFalse { timeseries.contains(Instant.EPOCH) }
    }

    @Test
    fun testContains_event() {
        val instant = Instant.now()
        val timeseries = timeseriesOf<Int>(
            setOf(
                timeseriesEntryOf(instant, 2)
                , timeseriesEntryOf(instant, 3)
                , timeseriesEntryOf(instant, 4)
            )
        )
        assertNotNull(timeseries)
        assertEquals(1, timeseries.countInstants())
        assertEquals(3, timeseries.countEvents())
        assertTrue { timeseries.contains(2) }
        assertFalse { timeseries.contains(5) }
    }

    @Test
    fun testCountInstants() {
        val instant = Instant.now()
        val timeseries = timeseriesOf<Int>(
            setOf(
                timeseriesEntryOf(instant, 2)
                , timeseriesEntryOf(instant, 3)
                , timeseriesEntryOf(instant, 4)
            )
        )
        assertNotNull(timeseries)
        assertEquals(1, timeseries.countInstants())
        assertEquals(3, timeseries.countEvents())
    }

    @Test
    fun testCountEvents() {
        val instant = Instant.now()
        val timeseries = timeseriesOf<Int>(
            setOf(
                timeseriesEntryOf(instant, 2)
                , timeseriesEntryOf(instant, 3)
                , timeseriesEntryOf(instant, 4)
            )
        )
        assertNotNull(timeseries)
        assertEquals(1, timeseries.countInstants())
        assertEquals(3, timeseries.countEvents())
    }

    @Test
    fun testIsEmpty() {
        val instant = Instant.now()
        val timeseries = timeseriesOf<Int>(
            setOf(
                timeseriesEntryOf(instant, 2)
                , timeseriesEntryOf(instant, 3)
                , timeseriesEntryOf(instant, 4)
            )
        )
        assertNotNull(timeseries)
        assertFalse { timeseries.isEmpty() }
        val timeseries2 = timeseriesOf<Int>()
        assertNotNull(timeseries2)
        assertTrue { timeseries2.isEmpty() }
    }

    @Test
    fun testIsNotEmpty() {
        val instant = Instant.now()
        val timeseries = timeseriesOf<Int>(
            setOf(
                timeseriesEntryOf(instant, 2)
                , timeseriesEntryOf(instant, 3)
                , timeseriesEntryOf(instant, 4)
            )
        )
        assertNotNull(timeseries)
        assertTrue { timeseries.isNotEmpty() }
        val timeseries2 = timeseriesOf<Int>()
        assertNotNull(timeseries2)
        assertFalse { timeseries2.isNotEmpty() }
    }

    @Test
    fun testGetEntriesSubMap_exclusive() {
        val instant1 = Instant.parse("2024-01-01T00:00:00Z")
        val instant2 = Instant.parse("2024-01-02T00:00:00Z")
        val instant3 = Instant.parse("2024-01-03T00:00:00Z")
        val timeseries = timeseriesOf<Int>(setOf(
            timeseriesEntryOf(instant1, 1),
            timeseriesEntryOf(instant2, 2),
            timeseriesEntryOf(instant3, 3)
        ))

        val subMap = timeseries.getEntriesSubMap(instant1, instant3)
        assertNotNull(subMap)
        assertEquals(1, subMap.size)
        assertTrue { subMap.containsKey(instant2) }
        assertFalse { subMap.containsKey(instant1) }
        assertFalse { subMap.containsKey(instant3) }
    }

    @Test
    fun testGetEntriesSubMap_inclusive() {
        val instant1 = Instant.parse("2024-01-01T00:00:00Z")
        val instant2 = Instant.parse("2024-01-02T00:00:00Z")
        val instant3 = Instant.parse("2024-01-03T00:00:00Z")
        val timeseries = timeseriesOf<Int>(setOf(
            timeseriesEntryOf(instant1, 1),
            timeseriesEntryOf(instant2, 2),
            timeseriesEntryOf(instant3, 3)
        ))

        val subMap = timeseries.getEntriesSubMap(instant1, true, instant3, true)
        assertNotNull(subMap)
        assertEquals(3, subMap.size)
        assertTrue { subMap.containsKey(instant1) }
        assertTrue { subMap.containsKey(instant2) }
        assertTrue { subMap.containsKey(instant3) }
    }

    @Test
    fun testGetEntriesHeadMap_exclusive() {
        val instant1 = Instant.parse("2024-01-01T00:00:00Z")
        val instant2 = Instant.parse("2024-01-02T00:00:00Z")
        val instant3 = Instant.parse("2024-01-03T00:00:00Z")
        val timeseries = timeseriesOf<Int>(setOf(
            timeseriesEntryOf(instant1, 1),
            timeseriesEntryOf(instant2, 2),
            timeseriesEntryOf(instant3, 3)
        ))

        val headMap = timeseries.getEntriesHeadMap(instant3)
        assertNotNull(headMap)
        assertEquals(2, headMap.size)
        assertTrue { headMap.containsKey(instant1) }
        assertTrue { headMap.containsKey(instant2) }
        assertFalse { headMap.containsKey(instant3) }
    }

    @Test
    fun testGetEntriesHeadMap_inclusive() {
        val instant1 = Instant.parse("2024-01-01T00:00:00Z")
        val instant2 = Instant.parse("2024-01-02T00:00:00Z")
        val instant3 = Instant.parse("2024-01-03T00:00:00Z")
        val timeseries = timeseriesOf<Int>(setOf(
            timeseriesEntryOf(instant1, 1),
            timeseriesEntryOf(instant2, 2),
            timeseriesEntryOf(instant3, 3)
        ))

        val headMap = timeseries.getEntriesHeadMap(instant3, true)
        assertNotNull(headMap)
        assertEquals(3, headMap.size)
        assertTrue { headMap.containsKey(instant1) }
        assertTrue { headMap.containsKey(instant2) }
        assertTrue { headMap.containsKey(instant3) }
    }

    @Test
    fun testGetEntriesTailMap_exclusive() {
        val instant1 = Instant.parse("2024-01-01T00:00:00Z")
        val instant2 = Instant.parse("2024-01-02T00:00:00Z")
        val instant3 = Instant.parse("2024-01-03T00:00:00Z")
        val timeseries = timeseriesOf<Int>(setOf(
            timeseriesEntryOf(instant1, 1),
            timeseriesEntryOf(instant2, 2),
            timeseriesEntryOf(instant3, 3)
        ))

        val tailMap = timeseries.getEntriesTailMap(instant1)
        assertNotNull(tailMap)
        assertEquals(2, tailMap.size)
        assertFalse { tailMap.containsKey(instant1) }
        assertTrue { tailMap.containsKey(instant2) }
        assertTrue { tailMap.containsKey(instant3) }
    }

    @Test
    fun testGetEntriesTailMap_inclusive() {
        val instant1 = Instant.parse("2024-01-01T00:00:00Z")
        val instant2 = Instant.parse("2024-01-02T00:00:00Z")
        val instant3 = Instant.parse("2024-01-03T00:00:00Z")
        val timeseries = timeseriesOf<Int>(setOf(
            timeseriesEntryOf(instant1, 1),
            timeseriesEntryOf(instant2, 2),
            timeseriesEntryOf(instant3, 3)
        ))

        val tailMap = timeseries.getEntriesTailMap(instant1, true)
        assertNotNull(tailMap)
        assertEquals(3, tailMap.size)
        assertTrue { tailMap.containsKey(instant1) }
        assertTrue { tailMap.containsKey(instant2) }
        assertTrue { tailMap.containsKey(instant3) }
    }

    @Test
    fun testStart() {
        val instant1 = Instant.parse("2024-01-01T00:00:00Z")
        val instant2 = Instant.parse("2024-01-02T00:00:00Z")
        val instant3 = Instant.parse("2024-01-03T00:00:00Z")
        val timeseries = timeseriesOf<Int>(setOf(
            timeseriesEntryOf(instant3, 3),
            timeseriesEntryOf(instant1, 1),
            timeseriesEntryOf(instant2, 2)
        ))

        val startEntries = timeseries.start()
        assertNotNull(startEntries)
        assertTrue { startEntries?.isNotEmpty() ?: false }
        assertTrue { startEntries?.contains(timeseriesEntryOf(instant1, 1)) ?: false }
    }

    @Test
    fun testStart_empty() {
        val timeseries = timeseriesOf<Int>()
        val startEntries = timeseries.start()
        assertNull(startEntries)
    }

    @Test
    fun testEnd() {
        val instant1 = Instant.parse("2024-01-01T00:00:00Z")
        val instant2 = Instant.parse("2024-01-02T00:00:00Z")
        val instant3 = Instant.parse("2024-01-03T00:00:00Z")
        val timeseries = timeseriesOf<Int>(setOf(
            timeseriesEntryOf(instant1, 1),
            timeseriesEntryOf(instant3, 3),
            timeseriesEntryOf(instant2, 2)
        ))

        val endEntries = timeseries.end()
        assertNotNull(endEntries)
        assertTrue { endEntries?.isNotEmpty() ?: false }
        assertTrue { endEntries?.contains(timeseriesEntryOf(instant3, 3)) ?: false }
    }

    @Test
    fun testEnd_empty() {
        val timeseries = timeseriesOf<Int>()
        val endEntries = timeseries.end()
        assertNull(endEntries)
    }

    @Test
    fun testIterator_ascending() {
        val instant1 = Instant.parse("2024-01-01T00:00:00Z")
        val instant2 = Instant.parse("2024-01-02T00:00:00Z")
        val instant3 = Instant.parse("2024-01-03T00:00:00Z")
        val timeseries = timeseriesOf<Int>(setOf(
            timeseriesEntryOf(instant3, 3),
            timeseriesEntryOf(instant1, 1),
            timeseriesEntryOf(instant2, 2)
        ))

        val iterator = timeseries.iterator()
        assertNotNull(iterator)
        assertTrue { iterator.hasNext() }

        val first = iterator.next()
        assertTrue { first.contains(timeseriesEntryOf(instant1, 1)) }

        val second = iterator.next()
        assertTrue { second.contains(timeseriesEntryOf(instant2, 2)) }

        val third = iterator.next()
        assertTrue { third.contains(timeseriesEntryOf(instant3, 3)) }

        assertFalse { iterator.hasNext() }
    }

    @Test
    fun testDescendingIterator() {
        val instant1 = Instant.parse("2024-01-01T00:00:00Z")
        val instant2 = Instant.parse("2024-01-02T00:00:00Z")
        val instant3 = Instant.parse("2024-01-03T00:00:00Z")
        val timeseries = timeseriesOf<Int>(
            setOf(
                timeseriesEntryOf(instant1, 1),
                timeseriesEntryOf(instant3, 3),
                timeseriesEntryOf(instant2, 2)
            )
        )

        val iterator = timeseries.descendingIterator()
        assertNotNull(iterator)
        assertTrue { iterator.hasNext() }

        val first = iterator.next()
        assertTrue { first.contains(timeseriesEntryOf(instant3, 3)) }

        val second = iterator.next()
        assertTrue { second.contains(timeseriesEntryOf(instant2, 2)) }

        val third = iterator.next()
        assertTrue { third.contains(timeseriesEntryOf(instant1, 1)) }

        assertFalse { iterator.hasNext() }
    }
}