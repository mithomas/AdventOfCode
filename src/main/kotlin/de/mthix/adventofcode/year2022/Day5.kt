package de.mthix.adventofcode.year2022

import de.mthix.adventofcode.linesOfDay

fun main() {
    val linesOfDay = linesOfDay(2022, 5)

    val stackIdx = listOf(1, 5, 9, 13, 17, 21, 25, 29, 33)
    //val stackIdx = listOf(1, 5, 9) //example
    val stacks9000 = List<ArrayDeque<Char>>(stackIdx.size) { ArrayDeque() }
    val stacks9001 = List<ArrayDeque<Char>>(stackIdx.size) { ArrayDeque() }

    var i = 0
    var line = linesOfDay[i++]
    while(line.isNotEmpty()) {
        stackIdx.map { line[it] }.forEachIndexed { idx, c-> if(c.isLetter()) {
            stacks9000[idx].addFirst(c)
            stacks9001[idx].addFirst(c)
        } }
        line = linesOfDay[i++]
    }
    println(stacks9000)
    println(stacks9001)

    line = linesOfDay[i++]
    while(line.isNotEmpty()) {
        val tokens = line.split(" ")
        val count = tokens[1].toInt()
        val from = tokens[3].toInt()-1
        val to = tokens[5].toInt()-1

        // puzzle 1
        (1..count).forEach { _ -> stacks9000[to].add(stacks9000[from].removeLast()) }

        // puzzle 2
        val cargo = stacks9001[from].subList(stacks9001[from].size-count, stacks9001[from].size)
        println("move $cargo from $from to $to")
        stacks9001[to].addAll(cargo)
        (1..count).forEach { _ -> stacks9001[from].removeLast() }

        line = linesOfDay[i++]
    }
    println("Puzzle 1: " + stacks9000.map { it.last() }.joinToString(""))
    println("Puzzle 2: " + stacks9001.map { it.last() }.joinToString(""))
}