package de.mthix.adventofcode.year2022

import de.mthix.adventofcode.linesOfDay

fun main() {
    val linesOfDay = linesOfDay(2022, 4)
        .map { it.split(",") }
        .map { it.map { it.split("-") }.map { (it[0].toInt()..it[1].toInt()).toList() } }

    println(linesOfDay)
    println("Puzzle 1: " + linesOfDay.filter { it[0].containsAll(it[1]) || it[1].containsAll(it[0]) }.size)
    println("Puzzle 2: " + linesOfDay.filter { it[0].union(it[1]).size < (it[0].size + it[1].size)}.size)
}