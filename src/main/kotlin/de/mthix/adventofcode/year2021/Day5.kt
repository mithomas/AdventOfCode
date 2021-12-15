package de.mthix.adventofcode.year2021

import de.mthix.adventofcode.BaseGrid
import de.mthix.adventofcode.GridNode
import de.mthix.adventofcode.linesOfDay
import kotlin.math.sign

class Line(val line : String, ocean : OceanFloor) {

    var coords : MutableList<GridNode<Int>> = mutableListOf()

    init {
        val c = line.split("->").map { it.trim() }

        val startXY = c[0].split(",").map { it.toInt() }
        val start = GridNode(startXY[0], startXY[1],1,1, ocean)

        val endXY = c[1].split(",").map { it.toInt() }
        val end = GridNode(endXY[0], endXY[1], 1, 1, ocean)

        var offsetY = (end.y-start.y).sign
        var offsetX = (end.x-start.x).sign
        println("$offsetX $offsetY")

        if(offsetX != 0 || offsetY != 0) {
            coords.add(start)
            var x = start.x
            var y = start.y

            while (x != end.x || y != end.y) {
                x += offsetX
                y += offsetY

                coords.add(GridNode(x,y, 1,1, ocean))
            }
        }
    }
}

class OceanFloor(size : Int = 1000) : BaseGrid<Int>(List(size) { List(size) { 0 } }, { it }) {

    fun register(lineCoords : List<GridNode<Int>>) {
        println("register line: $lineCoords")
        lineCoords.forEach { this.set(it, this.get(it) + 1)}
    }
}

fun main() {
    val floor = OceanFloor()

    val lines = linesOfDay(2021,5).map { Line(it,floor) }.filter { it.coords.isNotEmpty() }

    lines.forEach { floor.register(it.coords) }
    println(floor.nodeList.filter { it.value >= 2 }.size)
}