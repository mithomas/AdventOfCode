package de.mthix.adventofcode.year2024

import de.mthix.adventofcode.Coordinates
import de.mthix.adventofcode.linesOfDay

fun main() {
    val map = linesOfDay(2024,10).map { it.toList().map { it.digitToInt() } }

    // build nodes
    val trailNodes = LinkedHashMap<Coordinates,Int>()
    for(y in map.indices) {
        for(x in map[y].indices) {
            trailNodes[Coordinates(x,y)] = map[y][x]
        }
    }

    println(trailNodes
            .filter { it.value == 0 }
            .map { calcTrailScore(it.key, emptyList(), trailNodes) }
            .sumOf { it.map { it.last() }.distinct().size }
    )

    println(trailNodes
        .filter { it.value == 0 }
        .map { calcTrailScore(it.key, emptyList(), trailNodes).size }
        .sum()
    )
}

fun calcTrailScore(curNode:Coordinates, curTrail:List<Coordinates>, trailNodes:Map<Coordinates,Int>): Set<List<Coordinates>> {
    val curHeight = trailNodes.getOrDefault(curNode,99)

    val trails = if(curTrail.isEmpty() || curHeight == trailNodes[curTrail.last()]!!+1) {
        if(curHeight == 9) {
            setOf(curTrail+curNode)
        } else {
            listOf(curNode.getNorth(),curNode.getEast(), curNode.getSouth(),curNode.getWest())
                .map { (calcTrailScore(it,curTrail+curNode, trailNodes)) }
                .flatten()
                .toSet()
        }
    } else emptySet()


    return trails
}

