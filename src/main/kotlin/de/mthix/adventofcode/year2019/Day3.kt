package de.mthix.adventofcode.year2019

import java.io.File
import kotlin.math.abs

/**
```
--- Day 3: Crossed Wires ---

The gravity assist was successful, and you're well on your way to the Venus refuelling station. During the rush back on Earth, the fuel management system wasn't completely installed, so that's next on the priority list.

Opening the front panel reveals a jumble of wires. Specifically, two wires are connected to a central port and extend outward on a grid. You trace the path each wire takes as it leaves the central port, one wire per line of text (your puzzle input).

The wires twist and turn, but the two wires occasionally cross paths. To fix the circuit, you need to find the intersection point closest to the central port. Because the wires are on a grid, use the Manhattan distance for this measurement. While the wires do technically cross right at the central port where they both start, this point does not count, nor does a wire count as crossing with itself.

For example, if the first wire's path is R8,U5,L5,D3, then starting from the central port (o), it goes right 8, up 5, left 5, and finally down 3:

...........
...........
...........
....+----+.
....|....|.
....|....|.
....|....|.
.........|.
.o-------+.
...........

Then, if the second wire's path is U7,R6,D4,L4, it goes up 7, right 6, down 4, and left 4:

...........
.+-----+...
.|.....|...
.|..+--X-+.
.|..|..|.|.
.|.-X--+.|.
.|..|....|.
.|.......|.
.o-------+.
...........

These wires cross at two locations (marked X), but the lower-left one is closer to the central port: its distance is 3 + 3 = 6.

Here are a few more examples:

R75,D30,R83,U83,L12,D49,R71,U7,L72
U62,R66,U55,R34,D71,R55,D58,R83 = distance 159
R98,U47,R26,D63,R33,U87,L62,D20,R33,U53,R51
U98,R91,D20,R16,D67,R40,U7,R15,U6,R7 = distance 135

What is the Manhattan distance from the central port to the closest intersection?

--- Part Two ---

It turns out that this circuit is very timing-sensitive; you actually need to minimize the signal delay.

To do this, calculate the number of steps each wire takes to reach each intersection; choose the intersection where the sum of both wires' steps is lowest. If a wire visits a position on the grid multiple times, use the steps value from the first time it visits that position when calculating the total value of a specific intersection.

The number of steps a wire takes is the total number of grid squares the wire has entered to get to that location, including the intersection being considered. Again consider the example from above:

...........
.+-----+...
.|.....|...
.|..+--X-+.
.|..|..|.|.
.|.-X--+.|.
.|..|....|.
.|.......|.
.o-------+.
...........

In the above example, the intersection closest to the central port is reached after 8+5+5+2 = 20 steps by the first wire and 7+6+4+3 = 20 steps by the second wire for a total of 20+20 = 40 steps.

However, the top-right intersection is better: the first wire takes only 8+5+2 = 15 and the second wire takes only 7+6+2 = 15, a total of 15+15 = 30 steps.

Here are the best steps for the extra examples from above:

R75,D30,R83,U83,L12,D49,R71,U7,L72
U62,R66,U55,R34,D71,R55,D58,R83 = 610 steps
R98,U47,R26,D63,R33,U87,L62,D20,R33,U53,R51
U98,R91,D20,R16,D67,R40,U7,R15,U6,R7 = 410 steps

What is the fewest combined steps the wires must take to reach an intersection?

```
 *
 * See also [https://adventofcode.com/2019/day/3].
 */
fun main() {
    val file = File(object {}.javaClass.getResource("input.day3.txt").file)

    println("initializing grid...")
    val grid = initGrid(30000, 20000)
    val centralPort = getGridElement(grid, 5000,5000)

    file.readLines().forEachIndexed { i, wire ->
        println("adding wire $i...")
        wire2grid(grid, centralPort, wire, i)
    }

    val intersections = getIntersections(grid)
    println("Intersections: $intersections")

    println("Calculating solutions...")
    println("Solution for part 1: ${getClosestDistance(intersections, centralPort)}")
    println("Solution for part 2: ${getFewestSteps(intersections)}")
}

fun initGrid(height: Int, width: Int): Array<Array<GridElement?>> {
    return Array(height) { arrayOfNulls<GridElement>(width) }// lazy init - high memory consumption otherwise
}

fun wire2grid(grid: Array<Array<GridElement?>>, centralPort: GridElement, wirePath: String, wire: Int) {
    var current = centralPort
    var steps = 0

    wirePath.split(',').forEach {
        val direction = it[0]

        var xOffset = 0
        when (direction) {
            'L' -> xOffset = -1
            'R' -> xOffset = 1
        }
        var yOffset = 0
        when (direction) {
            'D' -> yOffset = -1
            'U' -> yOffset = 1
        }

        val length = it.substring(1).toInt()
        for (i in 1..length) {
            try {
                current = getGridElement(grid, current.x + xOffset, current.y + yOffset)
                current.wires[wire] = true
                current.stepsForWire[wire] = ++steps
            } catch (e: ArrayIndexOutOfBoundsException) {
                println("$wire:$current")
                throw e
            }
        }
    }
}

fun getGridElement(grid:Array<Array<GridElement?>>, x:Int, y:Int):GridElement {
    var gridElement = grid[x][y]
    if(gridElement == null) {
        gridElement = GridElement(x,y, 2)
        grid[x][y] = gridElement
    }
    return gridElement
}

fun getIntersections(grid: Array<Array<GridElement?>>):Set<GridElement> {
    return grid.map { it.filterNotNull() }.flatten().filter { it.wires.all { it } }.toSet()
}

fun getClosestDistance(intersections:Set<GridElement>, centralPort: GridElement): Int? {
    return intersections.map { getDistance(centralPort, it) }.min()
}

fun getFewestSteps(intersections: Set<GridElement>): Int? {
    return intersections.map { it.stepsForWire.sum() }.min()
}

fun getDistance(location1: GridElement, location2: GridElement): Int {
    return abs(location1.x - location2.x) + abs(location1.y - location2.y)
}

class GridElement(val x: Int, val y: Int, numberOfWires: Int) {
    val wires: Array<Boolean> = BooleanArray(numberOfWires) { false }.toTypedArray()
    val stepsForWire: Array<Int> = IntArray(numberOfWires) { 0 }.toTypedArray()

    override fun toString(): String {
        return "$x,$y:${stepsForWire.contentToString()}/${wires.contentToString()}"
    }
}
