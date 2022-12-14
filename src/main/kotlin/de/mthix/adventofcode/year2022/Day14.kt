package de.mthix.adventofcode.year2022

import de.mthix.adventofcode.Coordinates
import de.mthix.adventofcode.Grid
import de.mthix.adventofcode.linesOfDay
import de.mthix.adventofcode.year2022.CaveState.*

fun main() {
    val coordinatesOfTheDay = linesOfDay(2022, 14).map { it.split(" -> ") }.map { it.map { Coordinates(it) } }

    val start = Coordinates(500, 0)

    val caves = (1..2).map { Cave() }
    coordinatesOfTheDay.forEach {
        it.windowed(2).forEach { coord ->
            caves.forEach { it.moveStraightAndSet(coord[0], coord[1], ROCK) }
        }
    }

    println("Puzzle 1: " + ripple(caves[0], start))
    //caves[0].print()

    val bottomY = coordinatesOfTheDay.flatten().map { it.y }.max() + 2
    caves[1].moveStraightAndSet(Coordinates(0, bottomY), Coordinates(caves[1].width - 1, bottomY), ROCK)
    println("Puzzle 2: " + ripple(caves[1], start))
    //caves[1].print()
}

fun ripple(cave: Cave, start: Coordinates): Int {
    var finished = false
    var counter = 0
    do {
        cave.moveTo(start)
        do {
            if (cave.lookSouth() == AIR) {
                if (((cave.position.y + 1) until cave.height).map { cave.get(cave.position.x, it) }.any { it != AIR }) {
                    cave.moveSouth()
                } else {
                    finished = true
                }
            } else if (cave.lookSouthWest() == AIR) {
                cave.moveSouth()
                cave.moveWest()
            } else if (cave.lookSouthEast() == AIR) {
                cave.moveSouth()
                cave.moveEast()
            } else {
                cave.setCurrent(SAND)
                counter++

                if (cave.position == start) {
                    finished = true
                }
            }
        } while (cave.getCurrent() != SAND && !finished)
    } while (!finished)

    return counter
}

class Cave : Grid<CaveState>(700, 200, AIR, 500, 0) {
    override fun mapToOutput(value: CaveState): Char {
        return when (value) {
            ROCK -> '#'
            AIR -> '.'
            SAND -> 'O'
        }
    }

    override fun isBlocked(value: CaveState) = value != AIR
}

enum class CaveState {
    ROCK, AIR, SAND
}