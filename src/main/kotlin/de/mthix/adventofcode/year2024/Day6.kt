package de.mthix.adventofcode.year2024

import de.mthix.adventofcode.Direction.*
import de.mthix.adventofcode.Coordinates
import de.mthix.adventofcode.Direction
import de.mthix.adventofcode.linesOfDay

fun main() {
    val map = linesOfDay(2024,6)

    val startY = map.indexOfFirst { it.contains("^") }
    val startX = map[startY].indexOfFirst { it == '^' }

    val start = Coordinates(startX,startY)
    println("start: $start")

    val path = getPath(start, map).map { it.first }.distinct()
    println("path of guard: ${path.size}") // 5329

    val loops = path
        .filter { step -> step != start }
        .parallelStream()
        .filter { potentialObstacle -> hasLoop(getPath(start,map,potentialObstacle)) }
        .toList()
    println("loop obstacles: ${loops.size}") // 2113 < x (< 5329)
}

fun getPath(start:Coordinates, map: List<String>, obstacle:Coordinates=Coordinates(-1,-1)):List<Pair<Coordinates,Direction>> {
    var position = start
    var direction = NORTH
    val path = mutableListOf(Pair(position,direction))

    while(true) {
        position = position.getNext(direction)

        path.add(Pair(position,direction))

        var ahead = position.getNext(direction)
        if (ahead.x < 0 || ahead.x >= map[0].length || ahead.y < 0 || ahead.y >= map.size || hasLoop(path)) return path
        while(map[ahead.y][ahead.x] == '#' || ahead == obstacle) {
            direction = direction.right()
            ahead = position.getNext(direction)
        }
    }
}

fun hasLoop(path:List<Pair<Coordinates,Direction>>) = path.size > path.distinct().size