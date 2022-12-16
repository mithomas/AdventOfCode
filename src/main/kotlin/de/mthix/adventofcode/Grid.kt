package de.mthix.adventofcode

import de.mthix.adventofcode.Direction.*
import java.util.*

/** Top-left = 0,0 */
abstract class Grid<T>(
    val width: Int,
    val height: Int,
    initialValue: T,
    curX: Int = 0,
    curY: Int = 0,
    val name: String = ""
) {

    var grid = List(height) { MutableList(width) { initialValue } }
    var position = Coordinates(curX, curY)
    var direction = NORTH
    var visited = mutableSetOf(position)

    fun getCurrent() = get(position)
    fun get(cell: Coordinates) = get(cell.x, cell.y)
    fun get(x: Int, y: Int) = grid[y][x]

    fun setCurrent(value: T) {
        set(value, position.x, position.y)
    }

    fun set(value: T, x: Int, y: Int) {
        grid[y][x] = value
    }

    fun lookNorth() = get(position.x, position.y - 1)
    fun lookSouth() = get(position.x, position.y + 1)
    fun lookSouthWest() = get(position.x - 1, position.y + 1)
    fun lookSouthEast() = get(position.x + 1, position.y + 1)
    fun lookWest() = get(position.x - 1, position.y)
    fun lookEast() = get(position.x + 1, position.y)
    fun lookLeft() = when (direction) {
        NORTH -> lookWest()
        SOUTH -> lookEast()
        WEST -> lookSouth()
        EAST -> lookNorth()
    }

    fun lookRight() = when (direction) {
        NORTH -> lookEast()
        SOUTH -> lookWest()
        WEST -> lookNorth()
        EAST -> lookSouth()
    }

    fun lookAhead() = when (direction) {
        NORTH -> lookNorth()
        SOUTH -> lookSouth()
        WEST -> lookWest()
        EAST -> lookEast()
    }

    fun lookBehind() = when (direction) {
        NORTH -> lookSouth()
        SOUTH -> lookNorth()
        WEST -> lookEast()
        EAST -> lookWest()
    }

    fun moveNorth() = moveBy(0, -1)
    fun moveSouth() = moveBy(0, 1)
    fun moveWest() = moveBy(-1, 0)
    fun moveEast() = moveBy(1, 0)
    fun moveBy(offsetX: Int, offsetY: Int) = moveTo(position.x + offsetX, position.y + offsetY)
    fun moveTo(x: Int, y: Int) {
        position = Coordinates(x, y)
        visited.add(position)
    }

    fun moveTo(coordinates: Coordinates) {
        position = coordinates
        visited.add(position)
    }

    fun moveStraightAndSet(from: Coordinates, to: Coordinates, value: T) {
        moveTo(from)
        setCurrent(value)

        if (from.x == to.x) {
            if (to.y - from.y < 0) {
                while (position != to) {
                    moveNorth()
                    setCurrent(value)
                }
            } else if (to.y - from.y > 0) {
                while (position != to) {
                    moveSouth()
                    setCurrent(value)
                }
            }
        } else if (from.y == to.y) {
            if (to.x - from.x < 0) {
                while (position != to) {
                    moveWest()
                    setCurrent(value)
                }
            } else if (to.x - from.x > 0) {
                while (position != to) {
                    moveEast()
                    setCurrent(value)
                }
            }
        } else {
            throw IllegalArgumentException("No straight line from $from to $to!")
        }
    }

    fun moveForward() {
        when (direction) {
            NORTH -> moveNorth()
            SOUTH -> moveSouth()
            WEST -> moveWest()
            EAST -> moveEast()
        }
    }

    fun moveBackwards() {
        when (direction) {
            NORTH -> moveSouth()
            SOUTH -> moveNorth()
            WEST -> moveEast()
            EAST -> moveWest()
        }
    }

    fun turnLeft() {
        direction = direction.left()
    }

    fun turnRight() {
        direction = direction.right()
    }

    fun turnAround() {
        direction = direction.right().right()
    }


    fun cells() = grid.flatten()
    fun neighbourValues(cell: Coordinates): List<T> {
        return listOf(lookSouth(), lookWest(), lookEast(), lookNorth())
    }

    fun print() {
        grid.forEach { line ->
            println(line.map { cell -> mapToOutput(cell) }.joinToString(""))
        }
        println("$position $direction\n")
    }

    fun printReversed() {
        grid.reversed().forEach { line ->
            println(line.map { cell -> mapToOutput(cell) }.joinToString(""))
        }
        println("$position $direction\n")
    }

    abstract fun mapToOutput(value: T): Char
    abstract fun isBlocked(value: T): Boolean

    fun getMinDistance(start: Coordinates, target: Coordinates): Int {
        val source = PathElementOld(start, 0)

        // keep track of visited cells; marking blocked cells as visited. 
        val visited = List(height) { y -> MutableList(width) { x -> isBlocked(get(x, y)) } }

        // applying BFS on matrix cells starting from source 
        val q = ArrayDeque<PathElementOld>()
        q.push(source)
        visited[source.y][source.x] = true
        while (!q.isEmpty()) {
            val p = q.pop()


            if (p.coordinates == target) return p.dist // destination found

            // moving up 
            if (p.y - 1 >= 0 && !visited[p.y - 1][p.x]) {
                q.push(PathElementOld(Coordinates(p.y - 1, p.x), p.dist + 1))
                visited[p.y - 1][p.x] = true
            }

            // moving down 
            if (p.y + 1 < height && !visited[p.y + 1][p.x]) {
                q.push(PathElementOld(Coordinates(p.y + 1, p.x), p.dist + 1))
                visited[p.y + 1][p.x] = true
            }

            // moving left 
            if (p.x - 1 >= 0 && !visited[p.y][p.x - 1]) {
                q.push(PathElementOld(Coordinates(p.y, p.x - 1), p.dist + 1))
                visited[p.y][p.x - 1] = true
            }

            // moving right 
            if (p.x + 1 < width && !visited[p.y][p.x + 1]) {
                q.push(PathElementOld(Coordinates(p.y, p.x + 1), p.dist + 1))
                visited[p.y][p.x + 1] = true
            }
        }
        return -1
    }
}

data class PathElementOld(val coordinates: Coordinates, var dist: Int = 0) {
    val x = coordinates.x
    val y = coordinates.x
}

enum class Direction {
    NORTH,
    WEST,
    SOUTH,
    EAST;

    fun turn(direction: Long) = when (direction) {
        0L -> left()
        1L -> right()
        else -> throw IllegalArgumentException("Wrong direction: $direction")
    }

    fun left() = when (this) {
        NORTH -> WEST
        WEST -> SOUTH
        SOUTH -> EAST
        EAST -> NORTH
    }


    fun right() = when (this) {
        NORTH -> EAST
        WEST -> NORTH
        SOUTH -> WEST
        EAST -> SOUTH
    }


    fun reverse() = when (this) {
        NORTH -> SOUTH
        WEST -> EAST
        SOUTH -> NORTH
        EAST -> WEST
    }
}