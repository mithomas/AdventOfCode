package de.mthix.adventofcode.year2024

import de.mthix.adventofcode.linesOfDay

fun main() {
    val levels = linesOfDay(2024, 2)
        .map { it.split(" ").map { it.toInt() } }

    println("Safe levels: ${levels.filter { isSafe(it) }.size}")
    println("Safe levels (with dampener): ${levels.filter { isSafe(it) || isSafeWithDampener(it) }.size}")
}

private fun isSafe(level: List<Int>) = level.size == level.distinct().size && (
             (level == level.sorted() && level.windowed(2).all { it[1]-it[0] <=3  })
            || (level == level.sortedDescending() && level.windowed(2).all { it[0]-it[1]<=3  }))

private fun isSafeWithDampener(level: List<Int>): Boolean {
    for (i in level.indices) {
        val dampenedLevel = level.toMutableList()
        dampenedLevel.removeAt(i)
        if(isSafe(dampenedLevel)) return true
    }
    return false
}