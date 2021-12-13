package de.mthix.adventofcode

import java.util.*
import kotlin.math.sign

abstract class BaseNode<T>(var value : T) {

    abstract fun getNeighbors() : List<BaseNode<T>>
    abstract fun getNeighborValues() : List<T>

    override fun toString() : String {
        return "$value"
    }
}

/**
 * @param visitationLimit 0 for no visitation limit
 */
class GraphNode<T>(val id : String, var visitationLimit : Int = 0, value : T) : BaseNode<T>(value) {

    private val neighbors = mutableListOf<GraphNode<T>>()

    fun addNeighbor(node : GraphNode<T>) {
        neighbors.add(node)
    }

    override fun getNeighbors(): List<GraphNode<T>> {
        return neighbors
    }

    override fun getNeighborValues(): List<T> {
        return neighbors.map { value }
    }

    fun hasVisitationLimit() = visitationLimit != 0

    override fun equals(other: Any?) = (other is GraphNode<*>) && id == other.id
    override fun hashCode(): Int = id.hashCode()

    override fun toString() : String {
        return "$id:$visitationLimit:$value->${neighbors.map { it.id }}"
    }
}


class GridNode<T>(val x : Int, val y : Int, value : T, private val grid: BaseGrid<T>) : BaseNode<T>(value) {

    override fun getNeighbors() : List<GridNode<T>> = getNeighbors(false)
    fun getNeighbors(includeDiagonally : Boolean) : List<GridNode<T>> {
        val neighbors = mutableListOf<GridNode<T>>()

        val isTop = x <= 0
        val isBottom = x >= grid.height-1
        val isMostLeft =  y <= 0
        val isMostRight = y >= grid.width-1


        if (!isTop) neighbors.add(grid.getNode(x-1, y)) // over
        if (!isBottom) neighbors.add(grid.getNode(x+1, y)) // under
        if (!isMostLeft) neighbors.add(grid.getNode(x, y-1)) // left
        if (!isMostRight) neighbors.add(grid.getNode(x, y+1)) // right

        if (includeDiagonally) {
            if (!isTop && !isMostLeft) neighbors.add(grid.getNode(x-1, y-1)) // left over
            if (!isBottom && !isMostLeft) neighbors.add(grid.getNode(x+1, y-1)) // left under
            if (!isBottom && !isMostRight) neighbors.add(grid.getNode(x+1, y+1)) // right under
            if (!isTop && !isMostRight) neighbors.add(grid.getNode(x-1, y+1)) // right over
        }

        return neighbors
    }

    override fun getNeighborValues() : List<T> = getNeighborValues(false)
    fun getNeighborValues(includeDiagonally : Boolean) = getNeighbors(includeDiagonally).map { it.value }

    override fun toString() : String {
        return "$value[$x,$y]"
    }
}


class Graph<T>() {

    val nodes = mutableMapOf<String, GraphNode<T>>()

    fun registerEdge(id1 : String, id2: String, initNode : (String) -> GraphNode<T>, undirected : Boolean= true) {
        val node1 = nodes.getOrPut(id1) { initNode(id1) }
        val node2 = nodes.getOrPut(id2) { initNode(id2) }

        node1.addNeighbor(node2)
        if(undirected) node2.addNeighbor(node1)
    }

    fun findAllPaths(startId : String, endId : String): MutableList<List<String>> {
        val allPaths = mutableListOf<List<String>>()
        findAllPaths(nodes[startId], nodes[endId], allPaths, mutableListOf(), nodes.keys.associateWith { 0 }.toMutableMap() )
        return allPaths
    }

    private fun findAllPaths(current : GraphNode<T>?,
                             end : GraphNode<T>?,
                             allPaths : MutableList<List<String>>,
                             currentPath : MutableList<String>,
                             visited : MutableMap<String, Int?>) {

        if(current == null || end == null || (current.hasVisitationLimit() && visited[current.id]!! >= current.visitationLimit)) {
            return
        }

        currentPath.add(current.id)

        if(current.hasVisitationLimit()) { visited[current.id] = visited[current.id]!!+1 }

        if(current == end) {
            allPaths.add(currentPath)
        }

        current.getNeighbors().forEach { findAllPaths(it, end, allPaths, currentPath.toMutableList(), visited) }

        currentPath.removeAt(currentPath.size-1)
        if(current.hasVisitationLimit()) { visited[current.id] = visited[current.id]!!-1 }
    }

    override fun toString() : String {
        return nodes.values.toString()
    }
}

/**
 * @param elements outer list over height, inner list over width
 */
open class BaseGrid<T>(elements : List<List<Int>>, transform : (Int) -> T) {

    val width : Int = elements[0].size
    val height : Int = elements.size
    val nodes : List<GridNode<T>>

    init {
        val grid: MutableList<GridNode<T>> = mutableListOf()

        for (y in 0 until height) {
            for (x in 0 until width) {
                grid += GridNode(x, y, transform(elements[y][x]),this)
            }
        }

        nodes = grid
    }

    fun get(node : GridNode<T>) : T {
        return get(node.x, node.y)
    }

    fun get(x : Int, y : Int) : T {
        return nodes[x+y*width].value
    }

    fun set(node : GridNode<T>, value : T) {
        set(node.x, node.y, value)
    }

    fun set(x : Int, y : Int, value : T) {
        nodes[x+y*width].value = value
    }

    fun getNode(x : Int, y : Int) : GridNode<T> {
        return nodes[x+y*width]
    }

    override fun toString() : String {
        return toString(" ") { it.toString() }
    }

    fun toValueString() : String {
        return toString(" ") { it.value.toString().padStart(4) }
    }

    fun toBoolString() : String {
        return toString("") { if (it.value as Boolean) "█" else "." }
    }

    fun toString(nodeSeparator : String, render : (GridNode<T>) -> String) : String {
        var result = ""

        for (y in 0 until height) {
            for (x in 0 until width) {
                result += "${render(nodes[x+y*width])}$nodeSeparator"
            }
            result += "\n"
        }

        return result
    }

    companion object {

        /* when returning a list of list as input => outer list gives height/y, inner gives width/x! */

        fun fromUnseparatedIntLines(lines : List<String>) : List<List<Int>> {
            return lines.map { it.map { it.toString().toInt() } }
        }

        fun emptyGrid(width : Int, height : Int) : List<List<Int>> {
            return List(height) { List(width) { 0 } }
        }
    }
}