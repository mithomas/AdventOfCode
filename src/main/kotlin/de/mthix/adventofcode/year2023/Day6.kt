package de.mthix.adventofcode.year2023

import java.math.BigInteger

data class Race(val time: Int, val dist: Long, var losingOptions: Int = 0) {
    fun winningOptions() = this.time+1-this.losingOptions
}
fun main() {
    val useExample = false

    // part 1
    val races = if(useExample) {
        listOf(Race(7,9), Race(15,40), Race(30,200))
    } else {
        listOf(Race(44,202), Race(82,1076), Race(69,1138), Race(81,1458))
    }

    races.forEach { calculateLoosingOptions(it) }

    println(races)
    println(races.map { race -> race.winningOptions() }.reduce { acc, i -> acc*i })


    // part 2
    val race = if(useExample) Race(71530, 940200) else Race(44826981, 202107611381458)
    calculateLoosingOptions(race)
    println(race.toString() + " " + race.winningOptions())
}

fun isLoosing(holdTime: Long, race: Race) = holdTime*(race.time-holdTime) <= race.dist

fun calculateLoosingOptions(race:Race) {
    // up until first winning
    for(i in 0..race.time) {
        if(isLoosing(i.toLong(), race)) race.losingOptions++
        else break
    }

    // down until first losing
    for(i in race.time downTo 0) {
        if(isLoosing(i.toLong(), race)) race.losingOptions++
        else break
    }
}