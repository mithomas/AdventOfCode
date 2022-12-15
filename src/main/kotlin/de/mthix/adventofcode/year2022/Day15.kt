package de.mthix.adventofcode.year2022

import de.mthix.adventofcode.Coordinates
import de.mthix.adventofcode.linesOfDay
import kotlin.math.abs

fun main() {
    val example = 0
    val relevantLine = if (example == 0) 2000000 else 10

    val coordinatesOfTheDay = linesOfDay(2022, 15, example)
        .map {
            it.replace("Sensor at ", "")
                .replace(": closest beacon is at ", ",")
                .replace("[xy]=".toRegex(), "")
                .split(",")
        }
        .map { listOf(Coordinates(it[0], it[1]), Coordinates(it[2], it[3])) }

    val sensors = coordinatesOfTheDay.map { it[0] }.toSet()
    val beacons = coordinatesOfTheDay.map { it[1] }.toSet()
    val blockedSpots = mutableSetOf<Coordinates>()
    coordinatesOfTheDay.forEach { findBlockedSpots(it[0], it[1], relevantLine, blockedSpots) }

    val distances = coordinatesOfTheDay.associate { it[0] to it[0].getManhattanDistanceTo(it[1]) }
    val potentialPlaces = distances.map {
        val edge = mutableSetOf<Coordinates>()
        for (xOff in (-it.value - 1..it.value - 1)) {
            val x = it.key.x + xOff

            if (x >= 0 && x <= relevantLine * 2) {
                for (yOff in (((it.value - 1) - abs(xOff))..((it.value + 1) - abs(xOff)))) {
                    val y = it.key.y + yOff

                    if (abs(xOff) + abs(yOff) == it.value + 1 && y >= 0 && y <= relevantLine * 2) {
                        val current = Coordinates(x, y)

                        if (distances.all { current.getManhattanDistanceTo(it.key) > it.value }) {
                            edge.add(current)
                        }
                    }
                }
            }
        }

        edge
    }.flatten().toSet()

    println("Puzzle 1: " + blockedSpots.filter { !sensors.contains(it) && !beacons.contains(it) }.count { it.y == relevantLine })
    println("Puzzle 2: " + (potentialPlaces.first().x.toBigInteger() * 4000000.toBigInteger() + potentialPlaces.first().y.toBigInteger()))
}

fun findBlockedSpots(
    sensor: Coordinates,
    beacon: Coordinates,
    relevantLine: Int,
    blockedSpots: MutableSet<Coordinates>
) {
    val distance = sensor.getManhattanDistanceTo(beacon)
    val yOffsetToRelevantLine = relevantLine - sensor.y
    print("distance from $sensor to $beacon: $distance; offset to relevant line $relevantLine: $yOffsetToRelevantLine")

    if (distance < abs(yOffsetToRelevantLine)) {
        println("; skipping since sensor can't reach it")
        return
    }

    val maxXOffset = distance - abs(yOffsetToRelevantLine)
    for (xOffset in (-maxXOffset..maxXOffset)) {
        blockedSpots.add(Coordinates(sensor.x + xOffset, sensor.y + yOffsetToRelevantLine))
    }

    println("; total of ${blockedSpots.size} blocked spots")
    return
}