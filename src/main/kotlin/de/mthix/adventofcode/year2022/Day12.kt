package de.mthix.adventofcode.year2022

import de.mthix.adventofcode.BaseGrid
import de.mthix.adventofcode.Coordinates
import de.mthix.adventofcode.linesOfDay

fun main() {
    val lines = linesOfDay(2022, 12)

    val charMap = BaseGrid(BaseGrid.fromUnseparatedCharLines(lines), { it }, { it })
    val start = charMap.nodeList.first { it.value == 'S'.code }
    val target = charMap.nodeList.first { it.value == 'E'.code }

    println("$start $target")

    val map = BaseGrid(BaseGrid.fromUnseparatedCharLines(lines).map {
        it.map {
            when (it) {
                'S'.code -> 'a'.code
                'E'.code -> 'z'.code
                else -> it
            }
        }
    }, { it }, { 1 })

    //println(map.toString("") { Char(it.value).toString() })
    //println(map)

    val startCoordinates = Coordinates(start.x, start.y)
    val distances = map.findAllDistancesByDijkstra(
        Coordinates(target.x, target.y),
        startCoordinates
    ) { c, n ->
        c.value - n.value <= 1
    }

    println("Puzzle 1: " + distances[startCoordinates])
    println("Puzzle 2: " + distances.filter { it.value.node.value == 'a'.code }.minBy { it.value.totalDistance })
}
