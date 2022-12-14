package de.mthix.adventofcode.year2022

import de.mthix.adventofcode.*
import kotlin.math.abs

fun main() {
    val lines = linesOfDay(2022, 9)

    val grids = (0..9).map { KnotGrid(it.toString()) }.onEach { it.setCurrent(true) }

    lines.map { it.split(" ") }.forEach { move ->
        (1..move[1].toInt()).forEach {
            movePart(grids[0], move[0])
            grids.windowed(2).forEach { moveTail(it[0], it[1], move[0]) }
        }
    }

    // just for example display
    //val ropeGrid = RopeGrid()
    //grids.forEachIndexed{ i, n -> ropeGrid.set(i, n.position.first, n.position.second) }
    //ropeGrid.print()

    println("Puzzle 1: " + grids[1].cells().count { it })
    println("Puzzle 2: " + grids[9].cells().count { it })
}

class RopeGrid : Grid<Int>(50, 50, -1, 25, 25) {
    override fun mapToOutput(value: Int) = if (value == -1) '.' else value.toString()[0]

    override fun isBlocked(value: Int) = false
}

class KnotGrid(name: String) : Grid<Boolean>(1000, 1000, false, 500, 500, name) {
    override fun mapToOutput(value: Boolean) = if (value) '#' else '.'

    override fun isBlocked(value: Boolean) = false
}

fun movePart(grid: KnotGrid, dir: String) {
    when (dir) {
        "U" -> grid.moveNorth()
        "R" -> grid.moveEast()
        "D" -> grid.moveSouth()
        "L" -> grid.moveWest()
        else -> throw IllegalArgumentException("Wrong input: $dir")
    }

    grid.setCurrent(true)
}

fun moveTail(headGrid: KnotGrid, tailGrid: KnotGrid, dir: String) {
    val actDir = getTailDir(headGrid, tailGrid, dir)

    if (abs(tailGrid.position.y - headGrid.position.y) > 1) {
        when (actDir) {
            "U" -> tailGrid.moveNorth()
            "D" -> tailGrid.moveSouth()
        }

        if (tailGrid.position.x < headGrid.position.x) {
            tailGrid.moveEast()
        } else if (tailGrid.position.x > headGrid.position.x) {
            tailGrid.moveWest()
        }
    }

    if (abs(tailGrid.position.x - headGrid.position.x) > 1) {
        when (actDir) {
            "R" -> tailGrid.moveEast()
            "L" -> tailGrid.moveWest()
        }

        if (tailGrid.position.y < headGrid.position.y) {
            tailGrid.moveSouth()
        } else if (tailGrid.position.y > headGrid.position.y) {
            tailGrid.moveNorth()
        }
    }

    tailGrid.setCurrent(true)
}

fun getTailDir(headGrid: KnotGrid, tailGrid: KnotGrid, dir: String): String {
    return if (headGrid.position.x - tailGrid.position.x < -1) {
        "L"
    } else if (headGrid.position.x - tailGrid.position.x > 1) {
        "R"
    } else if (headGrid.position.y - tailGrid.position.y < -1) {
        "U"
    } else if (headGrid.position.y - tailGrid.position.y > 1) {
        "D"
    } else {
        dir
    }
}
