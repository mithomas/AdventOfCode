package de.mthix.adventofcode.year2023

import de.mthix.adventofcode.linesOfDay
import kotlin.math.pow


fun main() {
    val linesOfDay = linesOfDay(2023, 4)

    val scratchcards = linesOfDay
        .map { it.split(":", "|") }
        .map { listOf(
                it[0].toSet(),
                it[1].split(" ").map { it.trim() }.filter { it.isNotEmpty() }.map { it.toInt() }.toSet(),
                it[2].split(" ").map { it.trim() }.filter { it.isNotEmpty() }.map { it.toInt() }.toSet()
            )}
        .map { it[1].intersect(it[2]) }

    println(scratchcards.filter { it.isNotEmpty() }.sumOf { 2.0.pow(it.size - 1) })

    val winningCopies =  MutableList(linesOfDay.size) { 1 }
    scratchcards.forEachIndexed { cardIdx, card ->
        repeat(card.size) { i ->
            val cardIdxToCopy = cardIdx+i+1
            winningCopies[cardIdxToCopy] += winningCopies[cardIdx]
        }
    }
    println(winningCopies.sum())
}
