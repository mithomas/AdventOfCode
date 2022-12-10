package de.mthix.adventofcode.year2022

import de.mthix.adventofcode.*
import kotlin.math.abs

fun main() {
    val device = CommDevice().execute(linesOfDay(2022, 10))

    println("Puzzle 1: " + device.getSignalStrength(listOf(20, 60, 100, 140, 180, 220)))
    println("Puzzle 2: \n" + device.screen.map { if(it) '█' else '.' }.joinToString("").chunked(device.screenLineLength).joinToString("\n"))
}

class CommDevice {

    val screen = (0..239).map { false }.toMutableList()
    val screenLineLength = 40

    private var screenPos = 0
    private val regxValues = mutableListOf(1)

    fun execute(commands: List<String>) : CommDevice {
        commands.forEach { execute(it) }
        return this
    }
    private fun execute(command: String) {
        val params = command.split(" ")

        if(params[0] == "noop") {
            moveScreen(regxValues.last())
            regxValues.add(regxValues.last())
        } else if(params[0].startsWith("addx")) {
            moveScreen(regxValues.last())
            regxValues.add(regxValues.last())
            moveScreen(regxValues.last())
            regxValues.add(regxValues.last() + params[1].toInt())
        }
    }

    fun getSignalStrength(indexes: List<Int>) : Int {
        return indexes.sumOf { it * regxValues[it-1] }
    }

    private fun moveScreen(spritePos: Int) {
        screen[screenPos] = abs(spritePos-(screenPos % screenLineLength)) <= 1

        screenPos++
        if(screenPos == screen.size) screenPos = 0
    }
}
