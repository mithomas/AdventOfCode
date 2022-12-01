package de.mthix.adventofcode.year2019

import de.mthix.adventofcode.Grid
import de.mthix.adventofcode.longArrayFromCsvInputForDay
import kotlin.math.sign

/**
```
--- Day 13: Care Package ---

As you ponder the solitude of space and the ever-increasing three-hour roundtrip for messages between you and Earth, you notice that the Space Mail Indicator Light is blinking. To help keep you sane, the Elves have sent you a care package.

It's a new game for the ship's arcade cabinet! Unfortunately, the arcade is all the way on the other end of the ship. Surely, it won't be hard to build your own - the care package even comes with schematics.

The arcade cabinet runs Intcode software like the game the Elves sent (your puzzle input). It has a primitive screen capable of drawing square tiles on a grid. The software draws tiles to the screen with output instructions: every three output instructions specify the x position (distance from the left), y position (distance from the top), and tile id. The tile id is interpreted as follows:

0 is an empty tile. No game object appears in this tile.
1 is a wall tile. Walls are indestructible barriers.
2 is a block tile. Blocks can be broken by the ball.
3 is a horizontal paddle tile. The paddle is indestructible.
4 is a ball tile. The ball moves diagonally and bounces off objects.

For example, a sequence of output values like 1,2,3,6,5,4 would draw a horizontal paddle tile (1 tile from the left and 2 tiles from the top) and a ball tile (6 tiles from the left and 5 tiles from the top).

Start the game. How many block tiles are on the screen when the game exits?

--- Part Two ---

The game didn't run because you didn't put in any quarters. Unfortunately, you did not bring any quarters. Memory address 0 represents the number of quarters that have been inserted; set it to 2 to play for free.

The arcade cabinet has a joystick that can move left and right. The software reads the position of the joystick with input instructions:

If the joystick is in the neutral position, provide 0.
If the joystick is tilted to the left, provide -1.
If the joystick is tilted to the right, provide 1.

The arcade cabinet also has a segment display capable of showing a single number that represents the player's current score. When three output instructions specify X=-1, Y=0, the third output instruction is not a tile; the value instead specifies the new score to show in the segment display. For example, a sequence of output values like -1,0,12345 would show 12345 as the player's current score.

Beat the game by breaking all the blocks. What is your score after the last block is broken?
```
 *
 * See also [https://adventofcode.com/2019/day/13].
 */
val JOYSTICK_LEFT = -1L
val JOYSTICK_NEUTRAL = 0L
val JOYSTICK_RIGHT = 1L

fun main() {
    val arcadeDemo = Arcade()
    arcadeDemo.demo()
    val solution1 = arcadeDemo.grid.cells().count { it == Tile.BLOCK }
    println("Solution 1: $solution1\n")

    val arcadePlay = Arcade()
    arcadePlay.insertQuarters()
    do {
        var joystick = JOYSTICK_NEUTRAL
        if (ballMovesToRight(arcadePlay) && paddleLeftFromBall(arcadePlay) && !ballIsOnPaddle(arcadePlay)) {
            joystick = JOYSTICK_RIGHT
        } else if (ballMovesToLeft(arcadePlay) && paddleRightFromBall(arcadePlay) && !ballIsOnPaddle(arcadePlay)) {
            joystick = JOYSTICK_LEFT
        }

        println("Joystick: $joystick")
        println("ball: ${arcadePlay.ballX} (${arcadePlay.ballXOffset}) paddle: ${arcadePlay.paddleX}")

        arcadePlay.play(joystick)

    } while (!arcadePlay.cpu.completed)
}

fun ballMovesToRight(arcade:Arcade) = arcade.ballXOffset > 0
fun paddleLeftFromBall(arcade:Arcade) = arcade.paddleX < arcade.ballX + arcade.ballXOffset
fun ballMovesToLeft(arcade:Arcade) = arcade.ballXOffset < 0
fun paddleRightFromBall(arcade:Arcade) = arcade.paddleX > arcade.ballX + arcade.ballXOffset
fun ballIsOnPaddle(arcade: Arcade) = arcade.ballY + 1 == arcade.paddleY && arcade.ballX == arcade.paddleX

data class OutputInstruction(val x: Int, val y: Int, val tile: Tile)

class Arcade {
    val cpu = IntComputer(longArrayFromCsvInputForDay(2019, 13))
    val grid = ArcadeGrid(45, 23, Tile.EMPTY)
    var ballX = 19
    var ballY = 19
    var ballXOffset = 1
    var paddleX = 21
    val paddleY = 21
    var score = 0

    fun demo() {
        processInstructions(cpu.process())
        grid.print()
    }

    fun play(joystickPosition: Long) {
        processInstructions(cpu.process(joystickPosition))
        grid.print()
        println("= Score: ${score} ===============================\n")
    }

    private fun processInstructions(cpuOutput: List<Long>) {
        score = cpuOutput.chunked(3).firstOrNull { !isDrawInstruction(it) }?.get(2)?.toInt() ?: score

        val instructions = cpuOutput.chunked(3).filter { isDrawInstruction(it) }.map { OutputInstruction(it[0].toInt(), it[1].toInt(), Tile.values()[it[2].toInt()]) }
        instructions.forEach { instruction ->
            if (instruction.tile == Tile.BALL) {
                ballXOffset = sign((instruction.x - ballX).toDouble()).toInt()
                ballX = instruction.x
                ballY = instruction.y
            }
            if (instruction.tile == Tile.PADDLE) paddleX = instruction.x
            grid.set(instruction.tile, instruction.x, instruction.y)
        }
    }

    private fun isDrawInstruction(instruction: List<Long>) = !(instruction[0] == -1L && instruction[1] == 0L)

    fun insertQuarters() {
        cpu.program[0] = 2
    }
}

class ArcadeGrid(width: Int, height: Int, initialValue: Tile) : Grid<Tile>(width, height, initialValue) {

    override fun mapToOutput(value: Tile): Char {
        return when (value) {
            Tile.EMPTY -> ' '
            Tile.WALL -> 'W'
            Tile.BLOCK -> 'B'
            Tile.PADDLE -> '='
            Tile.BALL -> 'o'
        }
    }

    override fun isBlocked(value: Tile) = false
}

enum class Tile(val id: Long) {
    EMPTY(0),
    WALL(1),
    BLOCK(2),
    PADDLE(3),
    BALL(4),
    ;
}
