package de.mthix.adventofcode.year2022

import de.mthix.adventofcode.linesOfDay

fun main() {
    val points = mapOf(
        "A X" to (1 + 3), // Rock - Rock
        "A Y" to (2 + 6), // Rock - Paper
        "A Z" to (3 + 0), // Rock - Scissors
        "B X" to (1 + 0), // Paper - Rock
        "B Y" to (2 + 3), // Paper - Paper
        "B Z" to (3 + 6), // Paper - Scissors
        "C X" to (1 + 6), // Scissors - Rock
        "C Y" to (2 + 0), // Scissors - Paper
        "C Z" to (3 + 3)) // Scissors - Scissors

    val strategy = mapOf(
        "A X" to (3 + 0), // Rock - loose > Scissors
        "A Y" to (1 + 3), // Rock - draw > Rock
        "A Z" to (2 + 6), // Rock - win > Paper
        "B X" to (1 + 0), // Paper - loose > Rock
        "B Y" to (2 + 3), // Paper - draw > Paper
        "B Z" to (3 + 6), // Paper - win > Scissors
        "C X" to (2 + 0), // Scissors - loose > Paper
        "C Y" to (3 + 3), // Scissors - draw > Scissors
        "C Z" to (1 + 6)) // Scissors - win > Rock

    val linesOfDay = linesOfDay(2022, 2)

    println(linesOfDay)
    println("Puzzle 1: " + linesOfDay.map { points[it] }.filterNotNull().sumOf { it })
    println("Puzzle 2: " + linesOfDay.map { strategy[it] }.filterNotNull().sumOf { it })
}
