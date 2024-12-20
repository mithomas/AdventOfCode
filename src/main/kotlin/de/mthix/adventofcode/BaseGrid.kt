package de.mthix.adventofcode

import kotlin.math.abs

/**
 * @param visitationLimit 0 for no visitation limit
 */
abstract class BaseNode<I, T>(val id: I, var visitationLimit: Int = 0, var weight: Int, var value: T) {

    abstract fun getNeighbors(): Set<BaseNode<I, T>>
    abstract fun getNeighborValues(): List<T>

    fun hasVisitationLimit() = visitationLimit != 0

    override fun toString() = value.toString()
}

class GraphNode<T>(id: String, visitationLimit: Int, value: T) : BaseNode<String, T>(id, visitationLimit, 1, value) {

    private val neighbors = mutableSetOf<GraphNode<T>>()

    fun addNeighbor(node: GraphNode<T>) {
        neighbors.add(node)
    }

    override fun getNeighbors(): Set<GraphNode<T>> {
        return neighbors
    }

    override fun getNeighborValues(): List<T> {
        return neighbors.map { value }
    }

    override fun equals(other: Any?) = (other is GraphNode<*>) && id == other.id
    override fun hashCode(): Int = id.hashCode()

    override fun toString(): String {
        return "$id:$visitationLimit:$value->${neighbors.map { it.id }}"
    }
}

data class Coordinates(val x: Int, val y: Int) {

    constructor(xyCommaSeparated: String) : this(
        xyCommaSeparated.split(",")[0].toInt(),
        xyCommaSeparated.split(",")[1].toInt()
    )

    constructor(xString: String, yString: String) : this(
        xString.trim().toInt(),
        yString.trim().toInt()
    )

    fun getManhattanDistanceTo(target: Coordinates) = abs(this.x - target.x) + abs(this.y - target.y)

    fun getNorth() = Coordinates(x,y-1)
    fun getEast() = Coordinates(x+1,y)
    fun getSouth() = Coordinates(x,y+1)
    fun getWest() = Coordinates(x-1,y)

    fun getNeighbours() = listOf(getNorth(),getSouth(),getWest(),getEast())

    fun isInStringMap(map:List<String>) = x >= 0 && x < map[0].length && y >= 0 && y < map.size

    fun getNext(d:Direction) = when(d) {
        Direction.NORTH -> getNorth()
        Direction.EAST -> getEast()
        Direction.SOUTH -> getSouth()
        Direction.WEST -> getWest()
    }

    override fun toString() = "[$x,$y]"
}

class GridNode<T>(val x: Int, val y: Int, weight: Int, value: T, private val grid: BaseGrid<T>) :
    BaseNode<Coordinates, T>(Coordinates(x, y), 1, weight, value) {

    override fun getNeighbors(): Set<GridNode<T>> = getNeighbors(false)
    fun getNeighbors(includeDiagonally: Boolean): Set<GridNode<T>> {
        val neighbors = mutableSetOf<GridNode<T>>()

        val isTop = y <= 0
        val isBottom = y >= grid.height - 1
        val isMostLeft = id.x <= 0
        val isMostRight = id.x >= grid.width - 1

        if (!isTop) neighbors.add(grid.getNode(x, id.y - 1)) // over
        if (!isBottom) neighbors.add(grid.getNode(x, id.y + 1)) // under
        if (!isMostLeft) neighbors.add(grid.getNode(x - 1, id.y)) // left
        if (!isMostRight) neighbors.add(grid.getNode(x + 1, id.y)) // right

        if (includeDiagonally) {
            if (!isTop && !isMostLeft) neighbors.add(grid.getNode(x - 1, id.y - 1)) // left over
            if (!isBottom && !isMostLeft) neighbors.add(grid.getNode(x + 1, id.y - 1)) // left under
            if (!isBottom && !isMostRight) neighbors.add(grid.getNode(x + 1, id.y + 1)) // right under
            if (!isTop && !isMostRight) neighbors.add(grid.getNode(x - 1, id.y + 1)) // right over
        }

        return neighbors
    }

    fun isSamePositionAs(node: GridNode<T>): Boolean = x == node.x && id.y == node.id.y

    override fun getNeighborValues(): List<T> = getNeighborValues(false)
    fun getNeighborValues(includeDiagonally: Boolean) = getNeighbors(includeDiagonally).map { it.value }

    override fun toString(): String {
        return "$value:$weight[${x},${id.y}]"
    }
}

data class PathElement<N>(val node: N, var predecessor: N? = null, var totalDistance: Int = 100000)

open class Traversable<I, T, N : BaseNode<I, T>>(val nodes: MutableMap<I, N> = mutableMapOf()) {

    fun findAllPathsByDFS(
        startId: I,
        endId: I,
        isReachableNeighbor: (BaseNode<I, T>, BaseNode<I, T>) -> Boolean = { c, n -> true }
    ): MutableList<List<I>> {
        val allPaths = mutableListOf<List<I>>()
        findAllPathsByDFS(
            nodes[startId],
            nodes[endId],
            allPaths,
            mutableListOf(),
            nodes.keys.associateWith { 0 }.toMutableMap(),
            isReachableNeighbor
        )
        return allPaths
    }

    private fun findAllPathsByDFS(
        current: N?,
        end: N?,
        allPaths: MutableList<List<I>>,
        currentPath: MutableList<I>,
        visited: MutableMap<I, Int?>,
        isReachableNeighbor: (BaseNode<I, T>, BaseNode<I, T>) -> Boolean
    ) {

        if (current == null || end == null || (current.hasVisitationLimit() && visited[current.id]!! >= current.visitationLimit)) {
            return
        }

        currentPath.add(current.id)

        if (current.hasVisitationLimit()) {
            visited[current.id] = visited[current.id]!! + 1
        }

        if (current == end) {
            allPaths.add(currentPath)
        }

        (current.getNeighbors().filter { isReachableNeighbor(current, it) } as Set<N>).forEach {
            findAllPathsByDFS(
                it,
                end,
                allPaths,
                currentPath.toMutableList(),
                visited,
                isReachableNeighbor
            )
        }

        currentPath.removeAt(currentPath.size - 1)
        if (current.hasVisitationLimit()) {
            visited[current.id] = visited[current.id]!! - 1
        }
    }


    fun findAllDistancesByDijkstra(
        start: I,
        target: I? = null,
        isReachableNeighbor: (BaseNode<I, T>, BaseNode<I, T>) -> Boolean = { c, n -> true }
    ): Map<I, PathElement<N>> {
        // init
        val pathsToTarget =
            nodes.map { it.key to PathElement(it.value) }.toMap() // distances to each node initially infinite
        pathsToTarget[start]!!.totalDistance = 0 // distance to start is known
        val unvisitedPathElements = pathsToTarget.toMutableMap()

        // actual algorithm
        while (unvisitedPathElements.keys.isNotEmpty()) {
            val currentPathElement = unvisitedPathElements.values.minBy { it.totalDistance }
            unvisitedPathElements.remove(currentPathElement.node.id)

            if (currentPathElement.node.id == target) {
                return pathsToTarget
            }

            currentPathElement.node.getNeighbors()
                .filter { isReachableNeighbor(currentPathElement.node, it) }
                .filter { neighbor -> unvisitedPathElements.keys.contains(neighbor.id) }
                .forEach { neighbor ->
                    val alternativeDistance =
                        pathsToTarget[currentPathElement.node.id]!!.totalDistance + neighbor.weight
                    if (alternativeDistance < pathsToTarget[neighbor.id]!!.totalDistance) {
                        pathsToTarget[neighbor.id]!!.totalDistance = alternativeDistance
                        pathsToTarget[neighbor.id]!!.predecessor = currentPathElement.node
                    }
                }
        }

        return pathsToTarget
    }

    fun getPath(target: I, paths: Map<I, PathElement<N>>) : List<N> {
        val path = mutableListOf<N>()

        var curElement = paths[target]
        while(curElement != null) {
            path.add(curElement!!.node)
            curElement = paths[curElement.predecessor?.id]
        }

        return path.reversed()
    }
}



class Graph<T>(nodes: MutableMap<String,GraphNode<T>> = mutableMapOf()) : Traversable<String, T, GraphNode<T>>(nodes) {

    fun registerEdge(id1: String, id2: String, initNode: (String) -> GraphNode<T>, undirected: Boolean = true) {
        val node1 = nodes.getOrPut(id1) { initNode(id1) }
        val node2 = nodes.getOrPut(id2) { initNode(id2) }

        node1.addNeighbor(node2)
        if (undirected) node2.addNeighbor(node1)
    }

    override fun toString(): String {
        return nodes.values.toString()
    }
}

/**
 * @param elements outer list over height, inner list over width
 */
open class BaseGrid<T>(elements: List<List<Int>>, transformValue: (Int) -> T, transformWeight: (Int) -> Int = { it }) :
    Traversable<Coordinates, T, GridNode<T>>() {

    val width: Int = elements[0].size
    val height: Int = elements.size
    val nodeList: List<GridNode<T>>

    init {
        val grid: MutableList<GridNode<T>> = mutableListOf()

        for (y in 0 until height) {
            for (x in 0 until width) {
                val value = elements[y][x]
                grid += GridNode(x, y, transformWeight(value), transformValue(value), this)
            }
        }

        nodeList = grid

        nodeList.forEach { nodes[it.id] = it }
    }

    fun get(node: GridNode<T>): T {
        return get(node.x, node.id.y)
    }

    fun get(x: Int, y: Int): T {
        return nodeList[x + y * width].value
    }

    fun set(node: GridNode<T>, value: T) {
        set(node.x, node.id.y, value)
    }

    fun set(x: Int, y: Int, value: T) {
        nodeList[x + y * width].value = value
    }

    fun setWeight(x: Int, y: Int, weight: Int) {
        nodeList[x + y * width].weight = weight
    }

    fun getNode(x: Int, y: Int): GridNode<T> {
        return nodeList[x + y * width]
    }

    fun getRows(): List<List<GridNode<T>>> {
        val rows = mutableListOf<List<GridNode<T>>>()

        for (i in 0..height) {
            rows.add(nodeList.filter { it.y == i })
        }

        return rows
    }

    fun getColumns(): List<List<GridNode<T>>> {
        val rows = mutableListOf<List<GridNode<T>>>()

        for (i in 0..width) {
            rows.add(nodeList.filter { it.x == i })
        }

        return rows
    }

    /** in viewing direction */
    fun getNorth(node: GridNode<T>) = nodeList.filter { it.x == node.x && it.y < node.y }
        .sortedWith(compareBy<GridNode<T>> { it.x }.thenByDescending { it.y })

    /** in viewing direction */
    fun getSouth(node: GridNode<T>) =
        nodeList.filter { it.x == node.x && it.y > node.y }.sortedWith(compareBy<GridNode<T>> { it.x }.thenBy { it.y })

    /** in viewing direction */
    fun getEast(node: GridNode<T>) =
        nodeList.filter { it.x > node.x && it.y == node.y }.sortedWith(compareBy<GridNode<T>> { it.x }.thenBy { it.y })

    /** in viewing direction */
    fun getWest(node: GridNode<T>) = nodeList.filter { it.x < node.x && it.y == node.y }
        .sortedWith(compareByDescending<GridNode<T>> { it.x }.thenBy { it.y })


    override fun toString(): String {
        return toString(" ") { it.toString() }
    }

    fun toIntString(): String {
        return toString("") { it.value.toString().padStart(0) }
    }

    fun toValueString(nodeSeparator: String = " ", pad: Int = 4): String {
        return toString("") { it.value.toString().padStart(pad) }
    }

    fun toBoolString(): String {
        return toString("") { if (it.value as Boolean) "█" else "." }
    }

    fun toString(nodeSeparator: String, render: (GridNode<T>) -> String): String {
        var result = ""

        for (y in 0 until height) {
            for (x in 0 until width) {
                result += "${render(nodeList[x + y * width])}$nodeSeparator"
            }
            result += "\n"
        }

        return result
    }

    companion object {

        /* when returning a list of list as input => outer list gives height/y, inner gives width/x! */

        fun fromUnseparatedIntLines(lines: List<String>): List<List<Int>> {
            return lines.map { it.map { it.toString().toInt() } }
        }

        fun fromUnseparatedCharLines(lines: List<String>): List<List<Int>> {
            return lines.map { it.map { it.code } }
        }

        fun emptyGrid(width: Int, height: Int): List<List<Int>> {
            return List(height) { List(width) { 0 } }
        }
    }
}