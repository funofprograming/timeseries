package io.github.funofprograming.timeseries

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import java.time.Instant
import java.util.NavigableMap
import java.util.TreeMap

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
}