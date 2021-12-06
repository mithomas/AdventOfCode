package de.mthix.adventofcode.year2021

import de.mthix.adventofcode.intsOf
import de.mthix.adventofcode.linesOfDay
import de.mthix.adventofcode.year2019.RepairDroid

data class BingoField(val value : Int, val x : Int, val y : Int, var hit : Boolean = false) {

    override fun toString() : String {
       return "$value:" + (if (hit)  "!" else " ")
    }
}

class BingoBoard(val board: List<BingoField>, val size : Int = 5) {

    override fun toString() : String {
        var result = ""

        for (i in 0 until size) {
            for (j in 0 until size) {
                result += board[i*size+j]
            }
            result += "\n"
        }

        return result
    }

    fun markDrawn(drawn : Int) {
        board.filter { it.value == drawn }.forEach { it.hit = true }
    }

    fun bingo() : Boolean {
        return board.groupBy { it.x }.any { it.value.all { it.hit } } || board.groupBy { it.y }.any { it.value.all { it.hit } }
    }
}

fun main() {
    val linesOfDay = linesOfDay(2021, 4)

    val boards : MutableList<BingoBoard> = mutableListOf()

    for (i in 2..linesOfDay.size step 6) {
        val board : MutableList<BingoField> = mutableListOf()

        for (j in 0..4) {
            board += linesOfDay[i+j].split(" ").filter { it.isNotEmpty() }.mapIndexed() { idx,it -> BingoField(it.toInt(), j, idx) }
        }
        boards += BingoBoard(board)
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

    println(finalBoard)
    println(finalBoard.board.filter { !it.hit }.sumBy { it.value } * drawn)
}
