package de.mthix.adventofcode.year2022

import de.mthix.adventofcode.BaseGrid
import de.mthix.adventofcode.GridNode
import de.mthix.adventofcode.linesOfDay

fun main() {
    val forest = Forest(BaseGrid.fromUnseparatedIntLines(linesOfDay(2022, 8)))
    println(forest)

    val visibleTrees = setOf(
        lookAtTrees(forest.getRows()),
        lookAtTrees(forest.getRows().map { it.reversed() }),
        lookAtTrees(forest.getColumns()),
        lookAtTrees(forest.getColumns().map { it.reversed() }),
    ).flatten().toSet()

    println("Puzzle 1: " + visibleTrees.size)
    println("Puzzle 2: " + forest.nodeList.maxOfOrNull { getScenicScore(it, forest) })
}

class Forest(elements: List<List<Int>>) : BaseGrid<Int>(elements, { it })

fun lookAtTrees(nodeList: List<List<GridNode<Int>>>): Set<GridNode<Int>> {
    val totalVisibleTres = mutableSetOf<GridNode<Int>>()

    for (row in nodeList) {
        var currentHeight = -1
        for (node in row) {
            if (node.value > currentHeight) {
                currentHeight = node.value
                totalVisibleTres.add(node)
            }
        }
    }

    return totalVisibleTres
}

fun getScenicScore(tree: GridNode<Int>, forest: Forest) =
    getScenicScore(tree, forest.getNorth(tree)) *
            getScenicScore(tree, forest.getEast(tree)) *
            getScenicScore(tree, forest.getWest(tree)) *
            getScenicScore(tree, forest.getSouth(tree))


fun getScenicScore(tree: GridNode<Int>, view: List<GridNode<Int>>): Int {
    var score: Int

    score = if (view.isEmpty()) {
        0
    } else {
        view.indexOfFirst { it.value >= tree.value }
    }

    if (score == -1) {
        score = view.size
    } else {
        score++
    }

    //println("$tree: $score")
    return score
}