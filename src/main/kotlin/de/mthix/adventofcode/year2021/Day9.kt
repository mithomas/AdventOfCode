package de.mthix.adventofcode.year2021

import de.mthix.adventofcode.BaseGrid
import de.mthix.adventofcode.GridNode
import de.mthix.adventofcode.linesOfDay

fun main() {
    val heatmap = BaseGrid(BaseGrid.fromUnseparatedIntLines(linesOfDay(2021,9, 0))) { it }
    println(heatmap)

    val lowPoints = heatmap.nodeList.filter { it.getNeighborValues().all { neighbourValue -> neighbourValue > it.value } }

    val riskLevelSum = lowPoints.map { it.value+1 }.sum()
    println("risk level sum: $riskLevelSum")

    val basins = lowPoints.map { findBasin(it, heatmap, mutableSetOf()) }
    println(basins.map { it.size }.sortedDescending().take(3).reduce { product, size -> product*size })
}

fun findBasin(currentNode : GridNode<Int>, grid : BaseGrid<Int>, basinNodes : MutableSet<GridNode<Int>>) : MutableSet<GridNode<Int>>{
    if(!basinNodes.contains(currentNode)) {
        basinNodes.add(currentNode)

        currentNode.getNeighbors().filter { it.value < 9 }.forEach {
            findBasin(it, grid, basinNodes)
        }
    }

    return basinNodes
}