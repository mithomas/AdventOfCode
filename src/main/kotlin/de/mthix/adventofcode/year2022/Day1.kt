package de.mthix.adventofcode.year2022

import de.mthix.adventofcode.linesOfDay

fun main() {
    val linesOfDay = linesOfDay(2022, 1)
        .joinToString(" ")
        .split("  ")
        .map { it.split(" ") }
        .map { it.sumOf { it.toInt() }}
        .sortedBy { it }
        .reversed()

    println(linesOfDay)
    println("Puzzle 1: " + linesOfDay.max())
    println("Puzzle 2: " + (linesOfDay[0] + linesOfDay[1] + linesOfDay[2]))
}
