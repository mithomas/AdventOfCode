package de.mthix.adventofcode.year2018

import de.mthix.adventofcode.year2018.GuardLogEntryType.*
import de.mthix.adventofcode.year2018.GuardLogEntryType.START_OF_SLEEP
import java.io.File
import java.time.LocalDateTime

/**
```
You've sneaked into another supply closet - this time, it's across from the prototype suit manufacturing lab. You need to sneak inside and fix the issues with the suit, but there's a guardId stationed outside the lab, so this is as close as you can safely get.

As you search the closet for anything that might help, you discover that you're not the first person to want to sneak in. Covering the walls, someone has spent an hour starting every midnight for the past few months secretly observing this guardId post! They've been writing down the ID of the one guardId on duty that night - the Elves seem to have decided that one guardId was enough for the overnight shift - as well as when they fall asleep or wake up while at their post (your puzzle input).

For example, consider the following records, which have already been organized into chronological order:

[1518-11-01 00:00] Guard #10 begins shift
[1518-11-01 00:05] falls asleep
[1518-11-01 00:25] wakes up
[1518-11-01 00:30] falls asleep
[1518-11-01 00:55] wakes up
[1518-11-01 23:58] Guard #99 begins shift
[1518-11-02 00:40] falls asleep
[1518-11-02 00:50] wakes up
[1518-11-03 00:05] Guard #10 begins shift
[1518-11-03 00:24] falls asleep
[1518-11-03 00:29] wakes up
[1518-11-04 00:02] Guard #99 begins shift
[1518-11-04 00:36] falls asleep
[1518-11-04 00:46] wakes up
[1518-11-05 00:03] Guard #99 begins shift
[1518-11-05 00:45] falls asleep
[1518-11-05 00:55] wakes up

Timestamps are written using year-month-day hour:minute format. The guardId falling asleep or waking up is always the one whose shift most recently started. Because all asleep/awake times are during the midnight hour (00:00 - 00:59), only the minute portion (00 - 59) is relevant for those events.

Visually, these records show that the guards are asleep at these times:

Date   ID   Minute
000000000011111111112222222222333333333344444444445555555555
012345678901234567890123456789012345678901234567890123456789
11-01  #10  .....####################.....#########################.....
11-02  #99  ........................................##########..........
11-03  #10  ........................#####...............................
11-04  #99  ....................................##########..............
11-05  #99  .............................................##########.....

The columns are Date, which shows the month-day portion of the relevant day; ID, which shows the guardId on duty that day; and Minute, which shows the minutes during which the guardId was asleep within the midnight hour. (The Minute column's header shows the minute's ten's digit in the first row and the one's digit in the second row.) Awake is shown as ., and asleep is shown as #.

Note that guards count as asleep on the minute they fall asleep, and they count as awake on the minute they wake up. For example, because Guard #10 wakes up at 00:25 on 1518-11-01, minute 25 is marked as awake.

If you can figure out the guardId most likely to be asleep at a specific time, you might be able to trick that guardId into working tonight so you can have the best chance of sneaking in. You have two strategies for choosing the best guardId/minute combination.

Strategy 1: Find the guardId that has the most minutes asleep. What minute does that guardId spend asleep the most?

In the example above, Guard #10 spent the most minutes asleep, a total of 50 minutes (20+25+5), while Guard #99 only slept for a total of 30 minutes (10+10+10). Guard #10 was asleep most during minute 24 (on two days, whereas any other minute the guardId was asleep was only seen on one day).

While this example listed the entries in chronological order, your entries are in the order you found them. You'll need to organize them before they can be analyzed.

What is the ID of the guardId you chose multiplied by the minute you chose? (In the above example, the answer would be 10 * 24 = 240.)


--- Part Two ---

Strategy 2: Of all guards, which guard is most frequently asleep on the same minute?

In the example above, Guard #99 spent minute 45 asleep more than any other guard or minute - three times in total. (In all other cases, any guard spent any minute asleep at most twice.)

What is the ID of the guard you chose multiplied by the minute you chose? (In the above example, the answer would be 99 * 45 = 4455.)

```
 *
 * See also [https://adventofcode.com/2018/day/4].
 */

fun main(args: Array<String>) {
    val file = File(object {}.javaClass.getResource("input.day4.txt").file)
    val calculator = GuardShiftCalculator(file.readLines())

    val tiredestGuard = calculator.findTiredestGuard()
    println("Solution for step 1: tired guards: ${tiredestGuard.id*tiredestGuard.getSleepiestMinute()}")
    println("Solution for step 2: ${calculator.findOptimalGuardAndMinuteCode()}")
}

const val LOG_HEADER_PATTERN = "\\[([0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2})\\]"
val SIMPLE_ISO_FORMAT = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

enum class GuardLogEntryType(val pattern: Regex) {
    /* [1518-11-01 00:00] Guard #10 begins shift */
    START_OF_SHIFT("$LOG_HEADER_PATTERN Guard #([0-9]+) begins shift".toRegex()),
    /** [1518-11-01 00:05] falls asleep */
    START_OF_SLEEP("$LOG_HEADER_PATTERN falls asleep".toRegex()),
    /** [1518-11-02 00:50] wakes up */
    END_OF_SLEEP("$LOG_HEADER_PATTERN wakes up".toRegex());
}

data class GuardLogEntry(private val entry: String) : Comparable<GuardLogEntry> {

    val timestamp: LocalDateTime
    val entryType: GuardLogEntryType
    val guardId: Int?

    override fun compareTo(other: GuardLogEntry): Int {
        return timestamp.compareTo(other.timestamp)
    }

    init {
        if(START_OF_SHIFT.pattern.containsMatchIn(entry)) {
            val (t, g) = START_OF_SHIFT.pattern.matchEntire(entry)!!.destructured
            timestamp = LocalDateTime.parse(t, SIMPLE_ISO_FORMAT)
            entryType = START_OF_SHIFT
            guardId = g.toInt()
        } else if(START_OF_SLEEP.pattern.containsMatchIn(entry)) {
            val (t) = START_OF_SLEEP.pattern.matchEntire(entry)!!.destructured
            timestamp = LocalDateTime.parse(t, SIMPLE_ISO_FORMAT)
            entryType = START_OF_SLEEP
            guardId = null
        } else if(GuardLogEntryType.END_OF_SLEEP.pattern.containsMatchIn(entry)) {
            val (t) = END_OF_SLEEP.pattern.matchEntire(entry)!!.destructured
            timestamp = LocalDateTime.parse(t, SIMPLE_ISO_FORMAT)
            entryType = END_OF_SLEEP
            guardId = null
        } else {
            throw IllegalArgumentException("Unknown entry type: $entry")
        }
    }
}

data class Guard(val id: Int) {
    val minutesList = (0..59).map { 0 }.toIntArray()

    fun addMinutes(startMinute: Int, length: Int) {
        println("Add $length minutes from $startMinute for #$id")
        for(i in startMinute..startMinute+length-1) {
            minutesList[i]++
        }
    }

    fun getMinutesAsleep(): Int {
        return minutesList.sum()
    }

    fun getSleepiestMinute(): Int {
        return minutesList.withIndex().maxBy { it.value }?.index ?: -1
    }
}

class GuardShiftCalculator(private val logEntries: List<String>) {

    val guardLog : List<GuardLogEntry> = logEntries.map { GuardLogEntry(it) }.sorted()
    val guardMap = HashMap<Int, Guard>()

    init {
        guardLog.forEach { println(it) }

        var expectedTypes = listOf(START_OF_SHIFT)
        var currentGuard = Guard(-1)
        var currentStartMinute = -1
        for(entry in guardLog) {
            if(!(entry.entryType in expectedTypes)) throw IllegalArgumentException("Unexpected type at $entry! Had ${entry.entryType}, wanted $expectedTypes!")

            when(entry.entryType) {
                START_OF_SHIFT -> {
                    if (entry.guardId != null) {
                        currentGuard = guardMap.getOrPut(entry.guardId) { Guard(entry.guardId) }
                    } else {
                        IllegalArgumentException("GuardId must not be null!")
                    }
                    expectedTypes = listOf(START_OF_SLEEP, START_OF_SHIFT)
                }
                START_OF_SLEEP -> {
                    currentStartMinute = entry.timestamp.minute
                    expectedTypes = listOf(END_OF_SLEEP)
                }
                END_OF_SLEEP -> {
                    currentGuard.addMinutes(currentStartMinute, entry.timestamp.minute-currentStartMinute)
                    expectedTypes = listOf(START_OF_SHIFT, START_OF_SLEEP)
                }
            }
        }
    }

    fun findTiredestGuard(): Guard {
        return guardMap.values.maxBy { it.getMinutesAsleep() } ?: Guard(-1)
    }

    fun findOptimalGuardAndMinuteCode(): Int {
        val guardWithMaxMinute = guardMap.mapValues { it.value.minutesList.withIndex().maxBy { it.value } }
        val optimalGuardAndMinute = guardWithMaxMinute.maxBy { it.value!!.value }
        println(optimalGuardAndMinute)
        return optimalGuardAndMinute!!.key * optimalGuardAndMinute.value!!.index
    }
}