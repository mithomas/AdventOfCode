package de.mthix.adventofcode.year2024

import de.mthix.adventofcode.Coordinates
import de.mthix.adventofcode.linesOfDay
import de.mthix.adventofcode.textOfDay
import java.math.BigInteger
import java.math.BigInteger.*

fun main() {
    var stones = textOfDay(2024,11).split(" ").map { it.toBigInteger() }.associateWith { ONE }
    val YEAR = BigInteger("2024")

    for(i in 1..75) {
        val newStones = stones.toMutableMap()

        stones.filter { it.value > ZERO }.forEach {
            newStones[it.key] = newStones[it.key]!!-it.value

            if(it.key == ZERO) {
                incStoneCount(newStones, ONE, it.value)
            } else if(it.key.toString().length % 2 == 0) {
                it.key.toString().chunked(it.key.toString().length/2).forEach { half -> incStoneCount(newStones, half.toBigInteger(), it.value) }
            } else {
                incStoneCount(newStones, it.key * YEAR, it.value)
            }
        }

        stones = newStones
    }

    println(stones.map { it.value }.reduce { acc, it -> acc + it })
}

fun incStoneCount(stones:MutableMap<BigInteger,BigInteger>, value:BigInteger, count:BigInteger) {
    stones[value] = stones.getOrDefault(value,ZERO)+count
}