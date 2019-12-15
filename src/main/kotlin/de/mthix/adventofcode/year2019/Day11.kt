package de.mthix.adventofcode.year2019

import de.mthix.adventofcode.Grid
import de.mthix.adventofcode.longArrayFromCsvInputForDay
import de.mthix.adventofcode.year2019.Direction.*

/**
```
--- Day 11: Space Police ---

On the way to Jupiter, you're pulled over by the Space Police.

"Attention, unmarked spacecraft! You are in violation of Space Law! All spacecraft must have a clearly visible registration identifier! You have 24 hours to comply or be sent to Space Jail!"

Not wanting to be sent to Space Jail, you radio back to the Elves on Earth for help. Although it takes almost three hours for their reply signal to reach you, they send instructions for how to power up the emergency hull painting robot and even provide a small Intcode program (your puzzle input) that will cause it to paint your ship appropriately.

There's just one problem: you don't have an emergency hull painting robot.

You'll need to build a new emergency hull painting robot. The robot needs to be able to move around on the grid of square panels on the side of your ship, detect the color of its current panel, and paint its current panel black or white. (All of the panels are currently black.)

The Intcode program will serve as the brain of the robot. The program uses input instructions to access the robot's camera: provide 0 if the robot is over a black panel or 1 if the robot is over a white panel. Then, the program will output two values:

First, it will output a value indicating the color to paint the panel the robot is over: 0 means to paint the panel black, and 1 means to paint the panel white.
Second, it will output a value indicating the direction the robot should turn: 0 means it should turn left 90 degrees, and 1 means it should turn right 90 degrees.

After the robot turns, it should always move forward exactly one panel. The robot starts facing up.

The robot will continue running for a while like this and halt when it is finished drawing. Do not restart the Intcode computer inside the robot during this process.

For example, suppose the robot is about to start running. Drawing black panels as ., white panels as #, and the robot pointing the direction it is facing (< ^ > v), the initial state and region near the robot looks like this:

.....
.....
..^..
.....
.....

The panel under the robot (not visible here because a ^ is shown instead) is also black, and so any input instructions at this point should be provided 0. Suppose the robot eventually outputs 1 (paint white) and then 0 (turn left). After taking these actions and moving forward one panel, the region now looks like this:

.....
.....
.<#..
.....
.....

Input instructions should still be provided 0. Next, the robot might output 0 (paint black) and then 0 (turn left):

.....
.....
..#..
.v...
.....

After more outputs (1,0, 1,0):

.....
.....
..^..
.##..
.....

The robot is now back where it started, but because it is now on a white panel, input instructions should be provided 1. After several more outputs (0,1, 1,0, 1,0), the area looks like this:

.....
..<#.
...#.
.##..
.....

Before you deploy the robot, you should probably have an estimate of the area it will cover: specifically, you need to know the number of panels it paints at least once, regardless of color. In the example above, the robot painted 6 panels at least once. (It painted its starting panel twice, but that panel is still only counted once; it also never painted the panel it ended on.)

Build a new emergency hull painting robot and run the Intcode program on it. How many panels does it paint at least once?

--- Part Two ---

You're not sure what it's trying to paint, but it's definitely not a registration identifier. The Space Police are getting impatient.

Checking your external ship cameras again, you notice a white panel marked "emergency hull painting robot starting panel". The rest of the panels are still black, but it looks like the robot was expecting to start on a white panel, not a black one.

Based on the Space Law Space Brochure that the Space Police attached to one of your windows, a valid registration identifier is always eight capital letters. After starting the robot on a single white panel instead, what registration identifier does it paint on your hull?
```
 *
 * See also [https://adventofcode.com/2019/day/11].
 */
fun main() {
    val ehpr1 = Robot()
    ehpr1.paint()

    println("Solution 1: ${ehpr1.grid.visited.count()}")

    val ehpr2 = Robot()
    ehpr2.grid.setCurrent(WHITE)
    ehpr2.paint()
    ehpr2.grid.print()
}

val BLACK = 0L
val WHITE = 1L

class HullGrid(width: Int, height: Int, initialValue: Long, curX: Int, curY: Int) : Grid<Long>(width, height, initialValue, curX, curY) {
    override fun mapToOutput(value: Long) = if(value == WHITE) 'X' else ' '
}

class Robot() {
    val cpu = IntComputer(longArrayFromCsvInputForDay(2019,11), 2000)
    var grid = HullGrid(60, 130, BLACK, 113, 6)

    var direction = UP

    fun paint() {
        do {
            val output = cpu.process(grid.getCurrent())
            grid.setCurrent(output[0])
            direction = direction.turn(output[1])

            when(direction) {
                UP -> grid.moveBy(-1,0)
                DOWN -> grid.moveBy(1,0)
                LEFT -> grid.moveBy(0,-1)
                RIGHT -> grid.moveBy(0,1)
            }

        } while(!cpu.completed)
    }
}

enum class Direction {
    UP,
    LEFT,
    DOWN,
    RIGHT;

    fun turn(direction:Long):Direction {
        when(direction) {
            0L -> return left()
            1L -> return right()
            else -> throw IllegalArgumentException("Wrong direction: $direction")
        }
    }

    fun left():Direction {
        when(this) {
            UP -> return LEFT
            LEFT -> return DOWN
            DOWN -> return RIGHT
            RIGHT -> return UP
        }
    }

    fun right():Direction {
        when(this) {
            UP -> return RIGHT
            LEFT -> return UP
            DOWN -> return LEFT
            RIGHT -> return DOWN
        }
    }
}
