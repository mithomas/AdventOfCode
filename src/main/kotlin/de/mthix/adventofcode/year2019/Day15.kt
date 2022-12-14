package de.mthix.adventofcode.year2019

import de.mthix.adventofcode.Coordinates
import de.mthix.adventofcode.Direction
import de.mthix.adventofcode.Grid
import de.mthix.adventofcode.longArrayFromCsvInputForDay
import kotlin.random.Random

/**
```
--- Day 15: Oxygen System ---

Out here in deep space, many things can go wrong. Fortunately, many of those things have indicator lights. Unfortunately, one of those lights is lit: the oxygen system for part of the ship has failed!

According to the readouts, the oxygen system must have failed days ago after a rupture in oxygen tank two; that section of the ship was automatically sealed once oxygen levels went dangerously low. A single remotely-operated repair droid is your only option for fixing the oxygen system.

The Elves' care package included an Intcode program (your puzzle input) that you can use to remotely control the repair droid. By running that program, you can direct the repair droid to the oxygen system and fix the problem.

The remote control program executes the following steps in a loop forever:

Accept a movement command via an input instruction.
Send the movement command to the repair droid.
Wait for the repair droid to finish the movement operation.
Report on the status of the repair droid via an output instruction.

Only four movement commands are understood: north (1), south (2), west (3), and east (4). Any other command is invalid. The movements differ in direction, but not in distance: in a long enough east-west hallway, a series of commands like 4,4,4,4,3,3,3,3 would leave the repair droid back where it started.

The repair droid can reply with any of the following status codes:

0: The repair droid hit a wall. Its position has not changed.
1: The repair droid has moved one step in the requested direction.
2: The repair droid has moved one step in the requested direction; its new position is the location of the oxygen system.

You don't know anything about the area around the repair droid, but you can figure it out by watching the status codes.

For example, we can draw the area using D for the droid, # for walls, . for locations the droid can traverse, and empty space for unexplored locations. Then, the initial state looks like this:



D



To make the droid go north, send it 1. If it replies with 0, you know that location is a wall and that the droid didn't move:


#
D



To move east, send 4; a reply of 1 means the movement was successful:


#
.D



Then, perhaps attempts to move north (1), south (2), and east (4) are all met with replies of 0:


##
.D#
#


Now, you know the repair droid is in a dead end. Backtrack with 3 (which you already know will get a reply of 1 because you already know that location is open):


##
D.#
#


Then, perhaps west (3) gets a reply of 0, south (2) gets a reply of 1, south again (2) gets a reply of 0, and then west (3) gets a reply of 2:


##
#..#
D.#
#

Now, because of the reply of 2, you know you've found the oxygen system! In this example, it was only 2 moves away from the repair droid's starting position.

What is the fewest number of movement commands required to move the repair droid from its starting position to the location of the oxygen system?

```
 *
 * See also [https://adventofcode.com/2019/day/13].
 */

fun main() {
    val discoverDroid = RepairDroid()
    discoverDroid.discover()

    println("Solution 1: ${discoverDroid.grid.getMinDistance(discoverDroid.startPosition, discoverDroid.oxygenPosition!!)}")
}

val MOVE_UP = 1L
val MOVE_DOWN = 2L
val MOVE_LEFT = 3L
val MOVE_RIGHT = 4L

class RepairDroid {
    var startPosition = Coordinates(21, 21)
    var oxygenPosition: Coordinates? = null

    val cpu = IntComputer(longArrayFromCsvInputForDay(2019, 15))

    val grid = RepairHullGrid(41, 41, AreaTile.UNKN, startPosition.x, startPosition.y)

    fun discover() {
        grid.setCurrent(AreaTile.STAR)

        while (grid.cells().count { it == AreaTile.UNKN } > 27) { //manually counted non-reachable borders
            println(grid.cells().count { it == AreaTile.UNKN } )
            val command = getCommand(grid.direction)

            grid.moveForward()
            val tile = AreaTile.values()[cpu.process(command).first().toInt()]
            grid.setCurrent(tile)

            val blocked = listOf(AreaTile.WALL, AreaTile.DEAD)
            val target = listOf(AreaTile.OXYG, AreaTile.STAR)
            if (blocked.contains(tile)) {
                grid.moveBackwards()
            } else if (tile == AreaTile.OXYG) {
                oxygenPosition = grid.position
            }

            while (blocked.contains(grid.lookAhead()) && blocked.contains(grid.lookLeft()) && blocked.contains(grid.lookRight())
                    && !target.contains(grid.getCurrent())) {
                grid.setCurrent(AreaTile.DEAD)

                cpu.process(getCommand(grid.direction.reverse()))
                grid.moveBackwards()
            }

            grid.print()
            if (grid.lookRight() == AreaTile.UNKN) {
                grid.turnRight()
            } else if (grid.lookLeft() == AreaTile.UNKN) {
                grid.turnLeft()
            } else if (grid.lookAhead() == AreaTile.UNKN) {

            } else if (grid.lookBehind() == AreaTile.UNKN) {
                grid.turnAround()
            } else {
                grid.direction = Direction.values()[Random.nextInt(4)]
            }
            println("o:$oxygenPosition s:$startPosition v:${grid.visited.size}")
        }
    }
}

fun getCommand(direction: Direction):Long {
    return when (direction) {
        Direction.NORTH -> MOVE_UP
        Direction.SOUTH -> MOVE_DOWN
        Direction.WEST -> MOVE_LEFT
        Direction.EAST -> MOVE_RIGHT
    }
}

class RepairHullGrid(width: Int, height: Int, initialValue: AreaTile, curX: Int, curY: Int) : Grid<AreaTile>(width, height, initialValue, curX, curY) {

    override fun mapToOutput(value: AreaTile): Char {
        return when (value) {
            AreaTile.EMPT -> '.'
            AreaTile.WALL -> '#'
            AreaTile.OXYG -> 'O'
            AreaTile.UNKN -> ' '
            AreaTile.STAR -> 'S'
            AreaTile.DEAD -> 'D'
        }
    }

    override fun isBlocked(value: AreaTile) =  listOf(AreaTile.WALL,AreaTile.DEAD).contains(value)
}

enum class AreaTile(val id: Long) {
    WALL(0),
    EMPT(1),
    OXYG(2),
    UNKN(3),
    STAR(4),
    DEAD(5),
    ;
}
