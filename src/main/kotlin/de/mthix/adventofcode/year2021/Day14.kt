package de.mthix.adventofcode.year2021

import de.mthix.adventofcode.linesOfDay
import java.math.BigInteger
import java.math.BigInteger.*
import java.util.*

fun main() {
    val lines = linesOfDay(2021,14,1)

    val counts = getNaivePolymerCount(lines)
    println("polymer count: " + counts.toSortedMap().toString() + " -> " + (counts.values.max()!! - counts.values.min()!!))
    println("\n\n")

    val largeCounts = morePerformantSolution(lines, 10)
    println("polymer count: " + largeCounts.toSortedMap().toString() + " -> " + (largeCounts.values.max()!! - largeCounts.values.min()!!))
}

fun getNaivePolymerCount(lines : List<String>, steps : Int = 10) : Map<Char,Int> {
    println("Simple approach:")
    var polymer = lines[0].toList()
    val rules = lines.subList(2, lines.size).map { Pair(it[0],it[1]) to it[6] }.toMap()
    println("rules: $rules")

    println("before steps: $polymer")
    var newPolymer : LinkedList<Char>
    for(step in 1..steps) {
        newPolymer = LinkedList()

        for(i in 0 until polymer.size-1) {
            newPolymer.add(polymer[i])
            newPolymer.add(rules[Pair(polymer[i],polymer[i+1])]!!)
        }
        newPolymer.add(polymer[polymer.size-1])
        polymer = newPolymer

        println("after step $step: size=${polymer.size} : ${if (polymer.size <= 50) polymer.toString() else ""}")
    }

    return polymer.groupingBy { it }.eachCount()
}

fun morePerformantSolution(lines : List<String>, steps : Int = 40) : Map<Char,BigInteger> {
    println("Better approach for many steps:")
    val polymer = lines[0].toList()
    val rules = lines.subList(2, lines.size).associate { Pair(it[0], it[1]) to listOf(Pair(it[0], it[6]), Pair(it[6], it[1])) }
    println("rules: $rules")

    val polymerPairs = LinkedList<Pair<Char, Char>>()
    for (i in 0 until polymer.size-1) {
        polymerPairs.add(Pair(polymer[i],polymer[i+1]))
    }
    var polyPairMap = polymerPairs.groupingBy { it }.eachCount().map { it.key to it.value.toBigInteger() }.toMap().toMutableMap()
    println("before steps: $polyPairMap")

    for(step in 1..steps) {
        val newPolyPairMap = mutableMapOf<Pair<Char,Char>,BigInteger>()
        polyPairMap.forEach { pairCount ->
            rules[pairCount.key]!!.forEach { newPolyPairMap[it] =  newPolyPairMap.getOrDefault(it, ZERO) + pairCount.value }
        }
        polyPairMap = newPolyPairMap

        println("after step $step: size=${ONE + polyPairMap.map { it.value }.reduce { acc, bigInteger -> acc + bigInteger }} : $polyPairMap")
    }

    val count = mutableMapOf<Char,BigInteger>()
    polyPairMap.forEach { count[it.key.second] = count.getOrDefault(it.key.second, ZERO) + it.value }
    count[polymer[0]] = count.getOrDefault(polymer[0], ZERO) + ONE // re-add first char

    return count
}
