package de.mthix.adventofcode

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
    var direction = Direction.NORTH
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
    fun lookSouthWest() = get(position.x-1, position.y + 1)
    fun lookSouthEast() = get(position.x+1, position.y + 1)
    fun lookWest() = get(position.x - 1, position.y)
    fun lookEast() = get(position.x + 1, position.y)
    fun lookLeft(): T {
        return when (direction) {
            Direction.NORTH -> lookWest()
            Direction.SOUTH -> lookEast()
            Direction.WEST -> lookSouth()
            Direction.EAST -> lookNorth()
        }
    }

    fun lookRight(): T {
        return when (direction) {
            Direction.NORTH -> lookEast()
            Direction.SOUTH -> lookWest()
            Direction.WEST -> lookNorth()
            Direction.EAST -> lookSouth()
        }
    }

    fun lookAhead(): T {
        return when (direction) {
            Direction.NORTH -> lookNorth()
            Direction.SOUTH -> lookSouth()
            Direction.WEST -> lookWest()
            Direction.EAST -> lookEast()
        }
    }

    fun lookBehind(): T {
        return when (direction) {
            Direction.NORTH -> lookSouth()
            Direction.SOUTH -> lookNorth()
            Direction.WEST -> lookEast()
            Direction.EAST -> lookWest()
        }
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

        if(from.x == to.x) {
            if(to.y - from.y < 0) {
                while(position != to) {
                    moveNorth()
                    setCurrent(value)
                }
            } else if(to.y - from.y > 0) {
                while(position != to) {
                    moveSouth()
                    setCurrent(value)
                }
            }
        } else if(from.y == to.y) {
            if(to.x - from.x < 0) {
                while(position != to) {
                    moveWest()
                    setCurrent(value)
                }
            } else if(to.x - from.x > 0) {
                while(position != to) {
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
            Direction.NORTH -> moveNorth()
            Direction.SOUTH -> moveSouth()
            Direction.WEST -> moveWest()
            Direction.EAST -> moveEast()
        }
    }

    fun moveBackwards() {
        when (direction) {
            Direction.NORTH -> moveSouth()
            Direction.SOUTH -> moveNorth()
            Direction.WEST -> moveEast()
            Direction.EAST -> moveWest()
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

    fun turn(direction: Long): Direction {
        when (direction) {
            0L -> return left()
            1L -> return right()
            else -> throw IllegalArgumentException("Wrong direction: $direction")
        }
    }

    fun left(): Direction {
        when (this) {
            NORTH -> return WEST
            WEST -> return SOUTH
            SOUTH -> return EAST
            EAST -> return NORTH
        }
    }

    fun right(): Direction {
        when (this) {
            NORTH -> return EAST
            WEST -> return NORTH
            SOUTH -> return WEST
            EAST -> return SOUTH
        }
    }

    fun reverse(): Direction {
        when (this) {
            NORTH -> return SOUTH
            WEST -> return EAST
            SOUTH -> return NORTH
            EAST -> return WEST
        }
    }
}