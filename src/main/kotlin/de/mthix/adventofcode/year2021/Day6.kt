package de.mthix.adventofcode.year2021

import de.mthix.adventofcode.textOfDay
import java.math.BigInteger
import kotlin.collections.HashMap

fun main() {
    var fish = textOfDay(2021,6)
        .split(",")
        .map { it.toInt() }
        .groupBy { it }
        .mapValues { it.value.size.toBigInteger() }
        .toMutableMap()

    repeat(9) { if (!fish.containsKey(it)) fish[it] = 0.toBigInteger() }
    println(fish)

    repeat(256) {
        fish = day(fish) as MutableMap<Int, BigInteger>
        println("$it: ${fish.values.reduce { sum, value -> sum + value }} $fish")
    }
}

fun day(fish : Map<Int,BigInteger>) : Map<Int,BigInteger> {
    val newFish : MutableMap<Int,BigInteger> = HashMap()

    for(i in 8 downTo 1) {
        newFish[i-1] = fish.getOrDefault(i,0.toBigInteger())
    }
    newFish[6] = newFish.getOrDefault(6,0.toBigInteger()) + fish.getOrDefault(0,0.toBigInteger())
    newFish[8] = fish.getOrDefault(0,0.toBigInteger())

    return newFish
}