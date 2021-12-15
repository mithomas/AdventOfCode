package de.mthix.adventofcode.year2021

import de.mthix.adventofcode.BaseGrid
import de.mthix.adventofcode.linesOfDay

data class BingoInfo(val value : Int, var hit : Boolean = false) {

    override fun toString() : String {
       return "$value:" + (if (hit)  "!" else " ")
    }
}

class BingoBoard(private val board: List<List<Int>>) : BaseGrid<BingoInfo>(board, { BingoInfo(it) } ) {

    fun markDrawn(drawn : Int) {
        nodeList.filter { it.value.value == drawn }.forEach { it.value.hit = true }
    }

    fun bingo() : Boolean {
        return nodeList.groupBy { it.x }.any { it.value.all { it.value.hit } } || nodeList.groupBy { it.y }.any { it.value.all { it.value.hit } }
    }
}

fun main() {
    val linesOfDay = linesOfDay(2021, 4)

    val boards : MutableList<BingoBoard> = mutableListOf()

    for (i in 2..linesOfDay.size step 6) {
        val fields = mutableListOf<List<Int>>()

        for (j in 0..4) {
            fields += linesOfDay[i+j].split(" ").filter { it.isNotEmpty() }.map { it.toInt() }
        }

        boards += BingoBoard(fields)
    }

    val drawnNumbers = linesOfDay[0].split(",").map { it.toInt() }.toMutableList()
    println(drawnNumbers)

    var drawn = -1
    // Puzzle 1
    /*while (!boards.any { it.bingo() } && drawnNumbers.isNotEmpty()) {
        drawn = drawnNumbers.removeAt(0)
        boards.forEach { it.markDrawn(drawn) }
        println(drawn)
    }
    println(boards.filter { it.bingo() })
    println(boards.filter { it.bingo() }.first().board.filter { !it.hit }.sumBy { it.value } * drawn)
    }*/
    var finalBoard : BingoBoard = boards.first()
    while (!boards.all { it.bingo() } && drawnNumbers.isNotEmpty()) {
        drawn = drawnNumbers.removeAt(0)
        boards.forEach { it.markDrawn(drawn) }
        println(drawn)

        if(boards.filter { !it.bingo() }.size == 1) {
            finalBoard = boards.filter { !it.bingo() }.first()
        }
    }

    println(finalBoard.toValueString())
    println(finalBoard.nodeList.filter { !it.value.hit }.sumBy { it.value.value } * drawn)
}
