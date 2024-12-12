package de.mthix.adventofcode.year2024

import de.mthix.adventofcode.Coordinates
import de.mthix.adventofcode.linesOfDay

fun main() {
    val map = linesOfDay(2024,12)

    val areas = mutableListOf<GardenArea>()
    val visited = mutableSetOf<Coordinates>()

    for(y in map.indices) {
        for(x in map[0].indices) {
            val node = Coordinates(x,y)
            if(!visited.contains(node)) {
                val type = map[y][x]
                areas += GardenArea(type, buildArea(node,type,map,visited))
            }
        }
    }

    println(areas.sumOf { it.area() * it.perimeter()})
    println(areas.sumOf { it.area() * it.sides() })
}

fun buildArea(node:Coordinates, type:Char, map:List<String>, visited:MutableSet<Coordinates>): Set<Coordinates> {
    if(!visited.contains(node) && type == map[node.y][node.x]) {
        visited.add(node)
        return setOf(node) + node.getNeighbours().filter { it.isInStringMap(map) }.map { buildArea(it,type,map,visited) }.flatten()
    } else {
        return emptySet()
    }
}

class GardenArea(val type:Char, val nodes:Set<Coordinates>) {
    override fun toString() = "<$type:$nodes>"

    fun area() = nodes.size

    fun perimeter():Int {
        val neighbourNodes = neighbourNodes()
        return nodes.sumOf { node -> node.getNeighbours().count { neighbourNodes.contains(it) } }
    }

    fun sides():Int {
        var sides = 0

        val perimeterNodes = perimeterNodes()
        val perimeter =
            perimeterNodes
                .filter { !nodes.contains(it.getNorth()) }
                .groupBy { "T${it.y}" }
                .mapValues { it.value.map { it.x }.sorted() } +
            perimeterNodes
                .filter { !nodes.contains(it.getSouth()) }
                .groupBy { "B${it.y}"  }
                .mapValues { it.value.map { it.x }.sorted() } +
            perimeterNodes
                .filter { !nodes.contains(it.getEast()) }
                .groupBy { "R${it.x}"  }
                .mapValues { it.value.map { it.y }.sorted() } +
            perimeterNodes
                .filter { !nodes.contains(it.getWest()) }
                .groupBy { "L${it.x}"  }
                .mapValues { it.value.map { it.y }.sorted() }

        perimeter.forEach { (_, perimeterSide) ->
            sides++ // at least one side
            for (i in 1 until perimeterSide.size) {
                if (perimeterSide[i] != perimeterSide[i-1] + 1) sides++ // new side on break
            }
        }

        return sides
    }

    private fun neighbourNodes() = nodes.map { it.getNeighbours() }.flatten().toSet() - nodes
    private fun perimeterNodes() = nodes.filter { node -> node.getNeighbours().any { neighbourNodes().contains(it) } }
}
