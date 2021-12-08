package de.mthix.adventofcode.year2015

import de.mthix.adventofcode.textOfDay

fun main() {
    val brackets = textOfDay(2015, 1)
    var floor = 0
    var firstTimeBasement = false

    for (i in brackets.indices) {
        when(brackets[i]) {
            '(' -> floor++
            ')' -> floor--
            else -> throw IllegalArgumentException("unknown char '${brackets[i]}'")
        }

        if (!firstTimeBasement && floor < 0) {
            println("Negative floor at: ${i+1}")
            firstTimeBasement = true
        }
    }

    println("final floor: $floor")
}