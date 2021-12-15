package de.mthix.adventofcode.year2021

import de.mthix.adventofcode.BaseGrid
import de.mthix.adventofcode.linesOfDay
import kotlin.math.abs

class Paper(width : Int, height : Int) : BaseGrid<Boolean>(emptyGrid(width, height), { it > 0 } )

fun main() {
    val lines = linesOfDay(2021,13,0)
    val marked = mutableListOf<Pair<Int, Int>>()
    val fold = mutableListOf<Pair<String,Int>>()

    lines.forEach {
        if(it.contains(",")) {
            val xy = it.split(",")
            marked.add(Pair(xy[0].toInt(), xy[1].toInt()))
        } else if(it.startsWith("fold along")) {
            val line = it.replace("fold along ", "").split("=")
            fold.add(Pair(line[0], line[1].toInt()))
        }
    }

    var paper = Paper(marked.maxBy { it.first }!!.first+1,marked.maxBy { it.second }!!.second+1)

    marked.forEach { paper.set(it.first, it.second, true) }

    fold.forEach {
        val foldHorizontally = it.first == "y"
        val foldVertically = !foldHorizontally

        val newWidth : Int = if (foldHorizontally) paper.width else paper.width / 2
        val newHeight : Int = if (foldHorizontally) paper.height / 2 else paper.height

        val newPaper = Paper(newWidth, newHeight)
        var newX : Int
        var newY : Int

        paper.nodeList.filter { it.value }.forEach { oldNode ->
            if(foldVertically && oldNode.x > it.second) {
                newX = it.second - abs(oldNode.x - it.second)
            } else {
                newX = oldNode.x
            }

            if(foldHorizontally && oldNode.y > it.second) {
                newY = it.second - abs(oldNode.y - it.second)
            } else {
                newY = oldNode.y
            }

            newPaper.set(newX, newY, true)
        }

        paper = newPaper
        println(paper.nodeList.filter { it.value }.size)
    }

    println("\n${paper.toBoolString()}")
}