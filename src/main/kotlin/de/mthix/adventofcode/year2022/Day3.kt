package de.mthix.adventofcode.year2022

import de.mthix.adventofcode.linesOfDay

fun main() {
    val linesOfDay = linesOfDay(2022, 3)
        .map { it.toCharArray().asList() }

    val elements = linesOfDay
        .map { it.chunked(it.size/2) }
        .map { it[0].intersect(it[1]).elementAt(0) }
        .map { calcPriority(it) }

    println(elements)
    println("Puzzle 1: " + elements.sum())

    val badges = linesOfDay.chunked(3)
        .map { it[0].intersect(it[1]).intersect(it[2]).elementAt(0) }
        .map { calcPriority(it) }

    println(badges)
    println("Puzzle 2: " + badges.sum())
}

fun calcPriority(c: Char) = if(c.isLowerCase()) { c - 'a' + 1; } else { c - 'A' + 27; }
