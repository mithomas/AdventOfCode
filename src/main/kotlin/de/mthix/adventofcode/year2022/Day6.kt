package de.mthix.adventofcode.year2022

import de.mthix.adventofcode.textOfDay

fun main() {
    val textOfDay = textOfDay(2022, 6)

    println("Puzzle 1: " + findStartOfPacket(textOfDay))
    println("Puzzle 2: " + findStartOfMessage(textOfDay))
}

fun findStartOfPacket(stream: String) = findMarker(4, stream)
fun findStartOfMessage(stream: String) = findMarker(14, stream)

fun findMarker(markerLength : Int, stream: String): Int {
    val list = stream.toList()

    for(i in markerLength..list.size) {
        if (list.subList(i-markerLength,i).toSet().size == markerLength) return i
    }

    throw NoSuchElementException()
}