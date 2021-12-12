package de.mthix.adventofcode.year2021

import de.mthix.adventofcode.Graph
import de.mthix.adventofcode.GraphNode
import de.mthix.adventofcode.linesOfDay

fun main() {
    val graph = Graph<Int>()
    linesOfDay(2021,12,0).forEach {
        val names = it.split("-")
        graph.registerEdge(names[0], names[1], { id -> GraphNode(id, if (id.toLowerCase() == id) 1 else 0, 0)})
    }

    println(graph)
    println(graph.findAllPaths("start", "end").size)

    val allPaths = mutableSetOf<List<String>>()
    graph.nodes
        .filter { it.value.visitationLimit == 1 && !(it.key in listOf("start", "end")) }
        .forEach {
            it.value.visitationLimit = 2
            allPaths.addAll(graph.findAllPaths("start", "end"))
            it.value.visitationLimit = 1
        }
    println(allPaths.size)
}
