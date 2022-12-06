package de.mthix.adventofcode.year2015

import de.mthix.adventofcode.Grid
import de.mthix.adventofcode.textOfDay

fun main() {
    val directions = textOfDay(2015, 3)

    println(directions)

    val singleHouses = Houses()
    directions.forEach { move(singleHouses, it) }

    val comboHouses1 = Houses()
    val comboHouses2 = Houses()
    directions.forEachIndexed { i, c ->
            if(i % 2 == 0)  { move(comboHouses1, c) }
            else            { move(comboHouses2, c) }
        }

    println("Puzzle 1: " + singleHouses.visited.size)
    println("Puzzle 2: " + (comboHouses1.visited + comboHouses2.visited).distinct().size)
}

fun move(houses: Houses, dir: Char) {
    when (dir) {
        '^' -> houses.moveNorth()
        '>' -> houses.moveEast()
        'v' -> houses.moveSouth()
        '<' -> houses.moveWest()
        else -> throw IllegalArgumentException("Wrong input: $dir")
    }
}

class Houses : Grid<Int>(1000, 1000, 0, 500, 500) {
    override fun mapToOutput(value: Int) = ' '
    override fun isBlocked(value: Int) = false
}