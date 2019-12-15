package de.mthix.adventofcode

/** Top-left = 0,0 */
abstract class Grid<T>(val width: Int, val height: Int, initialValue: T, var curX: Int = 0, var curY: Int = 0) {

    var grid = List(height) { MutableList(width) { initialValue } }
    var visited = mutableSetOf(Pair(curX, curY))

    fun getCurrent() = grid[curY][curX]
    fun setCurrent(value: T) {
        set(value, curY, curX)
    }

    fun set(value: T, x: Int, y: Int) {
        grid[y][x] = value
    }

    fun moveBy(offsetX: Int, offsetY: Int) {
        moveTo(curX + offsetX, curY + offsetY)
    }

    fun moveTo(x: Int, y: Int) {
        curX = x
        curY = y

        visited.add(Pair(curX, curY))
    }

    fun cells() = grid.flatten()

    fun print() {
        grid.forEach { line ->
            println(line.map { cell -> mapToOutput(cell) }.joinToString(""))
        }
    }

    abstract fun mapToOutput(value: T): Char
}