package de.mthix.adventofcode.year2022

import de.mthix.adventofcode.Graph
import de.mthix.adventofcode.GraphNode
import de.mthix.adventofcode.PathElement
import de.mthix.adventofcode.linesOfDay

fun main() {
    val inputOfDay = linesOfDay(2022, 16).map {
        "Valve ([A-Z]{2}) has flow rate=([0-9]+); tunnels? leads? to valves? ([A-Z, ]+)"
            .toRegex()
            .find(it)!!.groupValues
    }

    val pipes =
        Graph(inputOfDay.associate { it[1] to GraphNode(it[1], Int.MAX_VALUE, Valve(it[2].toInt())) }
            .toMutableMap())
    inputOfDay.forEach { g -> g[3].split(",").forEach { pipes.registerEdge(g[1], it.trim(), { null!! }) } }

    val firstId = "AA"
    val first = pipes.nodes[firstId]!!

    val relevantValves = pipes.nodes.values.filter { it.value.shouldBeOpened() }.toList()
    val nodePairsDistances = elementPairs(relevantValves + first)
        .filter { it.first.id == firstId || it.second.id == firstId || (it.first.value.flowRate > 0 && it.second.value.flowRate > 0) }
        .toList()
        .associateWith { pipes.findAllDistancesByDijkstra(it.first.id, it.second.id) { _, _ -> true } }
    println(nodePairsDistances.map { "${it.key.first.id}->${it.key.second.id}:${it.value[it.key.second.id]!!.totalDistance}" }
        .sorted())

    println("Puzzle 1: " + traverse(30, first, first, relevantValves, nodePairsDistances))


    val valvesToOpen = (relevantValves-first).toSet()
    println(relevantValves)
    val valvesets = powerset(valvesToOpen) // each possible distribution of valves between the players
        .filter { it.isNotEmpty() && it.size < valvesToOpen.size}
        .map { setOf(it, valvesToOpen-it) }
        .distinct()
        .mapIndexed { i,sets ->
            println("Calculating #$i: ${sets.map { it.map { it.id }.sorted() }}")
            sets.map { traverse(26, first, first, it.toList() + first, nodePairsDistances) } }

    println("Puzzle 2: " + valvesets.maxOfOrNull { it.sum() })
}

private fun <T> powerset(set: Set<T>): Set<Set<T>> =
    if (set.isEmpty()) setOf(emptySet())
    else {
        val powersetOfRest = powerset(set.drop(1).toSet())
        powersetOfRest + powersetOfRest.map { it + set.first() }
    }

private fun traverse(
    minutes: Int,
    start: GraphNode<Valve>,
    current: GraphNode<Valve>,
    remaining: List<GraphNode<Valve>>,
    nodePairsDistances: Map<Pair<GraphNode<Valve>, GraphNode<Valve>>, Map<String, PathElement<GraphNode<Valve>>>>
): Int {
    val newRemaining = remaining - current
    val currentFlow = minutes * current.value.flowRate

    return currentFlow + (newRemaining
        .filter {
            //println(minutes.toString() + ":" + Pair(current, it))
            nodePairsDistances[Pair(current, it)]!![it.id]!!.totalDistance < minutes
        }
        .takeIf { it.isNotEmpty() }
        ?.maxOf {
            val remainingMinutes = minutes - 1 - nodePairsDistances[Pair(current, it)]!![it.id]!!.totalDistance
            traverse(remainingMinutes, start, it, newRemaining, nodePairsDistances)
        }
        ?: 0)
}

fun <T> elementPairs(arr: List<T>): Sequence<Pair<T, T>> = sequence {
    for (i in 0 until arr.size - 1)
        for (j in i + 1 until arr.size) {
            yield(arr[j] to arr[i])
            yield(arr[i] to arr[j])
        }
}

data class Valve(val flowRate: Int) {
    fun shouldBeOpened() = flowRate > 0
}
