package de.mthix.adventofcode.year2022

import de.mthix.adventofcode.linesOfDay
import de.mthix.adventofcode.year2022.CubeType.*

fun main() {
    val lavaCubes = linesOfDay(2022, 18).map { Cube(it, LAVA) }

    val totalUniqueSides = getUniqueSides(lavaCubes)
    println("Puzzle 1: $totalUniqueSides")


    val maxCoord = 20
    val tGrid = (0 until maxCoord).map { x ->
        (0 until 20).map { y ->
            (0 until 20).map { z ->
                Cube("$x,$y,$z", if (lavaCubes.any { it.coordinates == TCoords(x, y, z) }) LAVA else AIR)
            }
        }
    }

    floodWithSteam(tGrid, tGrid[0][0][0], 0, maxCoord)

    val allCubes = tGrid.map { xList -> xList.flatten() }.flatten()
    println("Puzzle 2: " + (totalUniqueSides-getUniqueSides(allCubes.filter { it.type == AIR })))
}

fun getUniqueSides(cubes: List<Cube>) : Int {
    val allSides = cubes.map { it.sides }.flatten()
    val uniqueSides = allSides.distinct()

    return allSides.size - 2 * (allSides.size - uniqueSides.size)
}

fun floodWithSteam(grid: List<List<List<Cube>>>, cube: Cube, minCoord: Int, maxCoord: Int) {
    val cubes = ArrayDeque<Cube>()
    cubes.add(cube)

    while(cubes.isNotEmpty()) {
        val cur = cubes.removeFirst()
        cur.type = STEAM

        cur.getNeighbors()
            .filter { listOf(it.x, it.y, it.z).all { it in minCoord until maxCoord } }
            .map { grid[it.x][it.y][it.z] }
            .filter { it.type == AIR }
            .filter { !cubes.contains(it)}
            .forEach { cubes.add(it) }
    }
}

enum class CubeType { LAVA, STEAM, AIR }
class Cube(input: String, var type: CubeType) {

    val coordinates: TCoords
    val sides: Set<Set<TCoords>>

    init {
        coordinates = TCoords(input)

        sides = setOf(
            setOf(
                // front
                TCoords(coordinates.x, coordinates.y, coordinates.z),
                TCoords(coordinates.x + 1, coordinates.y, coordinates.z),
                TCoords(coordinates.x, coordinates.y + 1, coordinates.z),
                TCoords(coordinates.x + 1, coordinates.y + 1, coordinates.z),
            ),
            setOf(
                // back
                TCoords(coordinates.x, coordinates.y, coordinates.z + 1),
                TCoords(coordinates.x + 1, coordinates.y, coordinates.z + 1),
                TCoords(coordinates.x, coordinates.y + 1, coordinates.z + 1),
                TCoords(coordinates.x + 1, coordinates.y + 1, coordinates.z + 1),
            ),
            setOf(
                // top
                TCoords(coordinates.x, coordinates.y + 1, coordinates.z),
                TCoords(coordinates.x + 1, coordinates.y + 1, coordinates.z),
                TCoords(coordinates.x + 1, coordinates.y + 1, coordinates.z + 1),
                TCoords(coordinates.x, coordinates.y + 1, coordinates.z + 1),
            ),
            setOf(
                // bottom
                TCoords(coordinates.x, coordinates.y, coordinates.z),
                TCoords(coordinates.x + 1, coordinates.y, coordinates.z),
                TCoords(coordinates.x + 1, coordinates.y, coordinates.z + 1),
                TCoords(coordinates.x, coordinates.y, coordinates.z + 1),
            ),
            setOf(
                // left
                TCoords(coordinates.x, coordinates.y, coordinates.z),
                TCoords(coordinates.x, coordinates.y + 1, coordinates.z),
                TCoords(coordinates.x, coordinates.y, coordinates.z + 1),
                TCoords(coordinates.x, coordinates.y + 1, coordinates.z + 1),
            ),
            setOf(
                // right
                TCoords(coordinates.x + 1, coordinates.y, coordinates.z),
                TCoords(coordinates.x + 1, coordinates.y + 1, coordinates.z),
                TCoords(coordinates.x + 1, coordinates.y, coordinates.z + 1),
                TCoords(coordinates.x + 1, coordinates.y + 1, coordinates.z + 1),
            )
        )
    }

    fun getNeighbors() = listOf(
        TCoords(coordinates.x + 1, coordinates.y, coordinates.z),
        TCoords(coordinates.x - 1, coordinates.y, coordinates.z),
        TCoords(coordinates.x, coordinates.y + 1, coordinates.z),
        TCoords(coordinates.x, coordinates.y - 1, coordinates.z),
        TCoords(coordinates.x, coordinates.y, coordinates.z + 1),
        TCoords(coordinates.x, coordinates.y, coordinates.z - 1)
    )

    override fun toString() = "$type[$coordinates]"
}

data class TCoords(val x: Int, val y: Int, val z: Int) {
    constructor(xyz: String) : this(
        xyz.split(",")[0].toInt(),
        xyz.split(",")[1].toInt(),
        xyz.split(",")[2].toInt()
    )
}
