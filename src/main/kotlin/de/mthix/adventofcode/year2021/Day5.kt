package de.mthix.adventofcode.year2021

import de.mthix.adventofcode.linesOfDay
import kotlin.math.sign

data class FloorCoord(val x : Int, val y : Int, var vents : Int=0)

class Line(val line : String) {

    var coords : MutableList<FloorCoord> = mutableListOf()

    init {
        val c = line.split("->").map { it.trim() }

        val startXY = c[0].split(",").map { it.toInt() }
        val start = FloorCoord(startXY[0], startXY[1],1)

        val endXY = c[1].split(",").map { it.toInt() }
        val end = FloorCoord(endXY[0], endXY[1], 1)

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

                coords.add(FloorCoord(x,y))
            }
        }
    }
}

class OceanFloor(val size : Int = 1000, var floorCoords : MutableList<FloorCoord> = mutableListOf()) {

    init {
        for (x in 0 until size) {
            for (y in 0 until size) {
                floorCoords.add(FloorCoord(x, y))
            }
        }
        println("init done: ${floorCoords.size}")
    }

    fun register(lineCoords : List<FloorCoord>) {
        println("register line: $lineCoords")
        lineCoords.forEach { floorCoords[it.x*size + it.y].vents++ }
    }

    override fun toString(): String {
        var result = ""

        for (x in 0 until size) {
            for (y in 0 until size) {
                result += floorCoords[x*size+y]
            }
            result += "\n"
        }

        return result
    }
}

fun main() {
    val floor = OceanFloor()

    val lines = linesOfDay(2021,5).map { Line(it) }.filter { it.coords.isNotEmpty() }

    lines.forEach { floor.register(it.coords) }
    println(floor.floorCoords.filter { it.vents >= 2 }.size)


}