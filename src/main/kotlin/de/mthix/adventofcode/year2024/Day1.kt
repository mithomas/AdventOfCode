package de.mthix.adventofcode.year2024

import de.mthix.adventofcode.linesOfDay
import de.mthix.adventofcode.textOfDay
import kotlin.math.abs

fun main() {
    val sides = linesOfDay(2024, 1)
        .map { it.replace("\\s+".toRegex(), " ").split(" ") }
        .map { Pair(it[0].toInt(),it[1].toInt())}
        .toList().unzip().toList().map { it.sorted() }

    val distances = sides[0].mapIndexed { i,it -> abs(it - sides[1][i]) }
    println("Distances: ${distances.sum()}")

    val counts = sides[1].groupingBy { it }.eachCount()
    val similarity = sides[0]
        .map { it*counts.getOrDefault(it,0) }
        .toList()
    println("Similarity: ${similarity.sum()}")
}