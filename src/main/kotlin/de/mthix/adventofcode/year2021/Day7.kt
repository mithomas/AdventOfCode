package de.mthix.adventofcode.year2021

import de.mthix.adventofcode.intsOfDay
import java.math.BigInteger
import kotlin.math.abs

import java.math.BigInteger.*

fun main() {
    val crabPositions = intsOfDay(2021,7)
    val distinctPositions = crabPositions.toSet()
    println("All crab positions: ${crabPositions.size} with ${distinctPositions.size} distinct positions")

    val allFuelChangesSimple = mutableMapOf<Int,Int>()
    val allFuelChangesAdvanced = mutableMapOf<Int,BigInteger>()

    for (i in (crabPositions.min() ?: 0)..(crabPositions.max() ?: 1500)) {
        allFuelChangesSimple[i] = crabPositions.map { abs(it-i) }.sum()
        allFuelChangesAdvanced[i] = crabPositions.map {
            val diff = abs(it-i).toBigInteger()
            diff*(diff+ ONE)/TWO }.reduce { sum, it -> sum+it }
    }

    println("Minimal fuel (simple): ${allFuelChangesSimple.minBy { it.value }}")
    println("Minimal fuel (better): ${allFuelChangesAdvanced.minBy { it.value }}")
}