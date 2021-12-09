package de.mthix.adventofcode

class GridNode<T>(val x : Int, val y : Int, var value : T, private val grid: BaseGrid<T>) {

    /* horizontally/vertically adjacent, not diagonally */
    fun getNeighbors() : List<GridNode<T>> {
        val neighbors = mutableListOf<GridNode<T>>()

        if (x > 0) neighbors.add(grid.getNode(x-1, y)) // over
        if (x < grid.height-1) neighbors.add(grid.getNode(x+1, y)) // under
        if (y > 0) neighbors.add(grid.getNode(x, y-1)) // left
        if (y < grid.width-1) neighbors.add(grid.getNode(x, y+1)) // right

        return neighbors
    }

    /* horizontally/vertically adjacent, not diagonally */
    fun getNeighborValues() : List<T> {
        return getNeighbors().map { it.value }
    }

    override fun toString() : String {
        return "$value[$x,$y]"
    }
}

open class BaseGrid<T>(elements : List<List<T>>) {

    val width : Int = elements[0].size
    val height : Int = elements.size
    val nodes : List<GridNode<T>>

    init {
        val grid: MutableList<GridNode<T>> = mutableListOf()

        for (x in 0 until height) {
            for (y in 0 until width) {
                grid += GridNode(x, y, elements[x][y],this)
            }
        }

        nodes = grid
    }

    fun get(node : GridNode<T>) : T {
        return get(node.x, node.y)
    }

    fun get(x : Int, y : Int) : T {
        return nodes[x*width+y].value
    }

    fun set(node : GridNode<T>, value : T) {
        set(node.x, node.y, value)
    }

    fun set(x : Int, y : Int, value : T) {
        nodes[x*width+y].value = value
    }

    fun getNode(x : Int, y : Int) : GridNode<T> {
        return nodes[x*width+y]
    }

    override fun toString() : String {
        var result = ""

        for (x in 0 until height) {
            for (y in 0 until width) {
                result += "${nodes[x*width+y]} "
            }
            result += "\n"
        }

        return result
    }

    companion object {

        fun fromUnseparatedIntLines(lines : List<String>) : BaseGrid<Int> {
            return BaseGrid(lines.map { it.map { it.toString().toInt() } })
        }
    }
}