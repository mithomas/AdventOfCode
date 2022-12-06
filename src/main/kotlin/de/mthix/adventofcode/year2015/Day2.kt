package de.mthix.adventofcode.year2015

import de.mthix.adventofcode.linesOfDay

fun main() {
    val lines = linesOfDay(2015, 2).map { it.split("x").map { it.toInt() } }

    println(lines)

    val paper = lines.map { listOf(2*it[0]*it[1],2*it[0]*it[2],2*it[1]*it[2]) }.map { it.sum() + it.min()/2 }
    val ribbon = lines.map { it.sorted() }.map { 2*it[0]+ 2*it[1] + it[0]*it[1]*it[2] }

    println("Puzzle 1: " + paper.sum())
    println("Puzzle 2: " + ribbon.sum())
}