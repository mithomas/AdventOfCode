package de.mthix.adventofcode.year2019

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class Day3Test {

    @Test
    fun aocSample1() {
        val grid = initGrid(20, 20)
        val centralPort = getGridElement(grid, 1,1)

        wire2grid(grid, centralPort, "R8,U5,L5,D3",0)
        wire2grid(grid, centralPort, "U7,R6,D4,L4",1)
        println(grid.contentDeepToString())

        val intersections = getIntersections(grid)
        assertThat(getClosestDistance(intersections, centralPort)).isEqualTo(6)
        assertThat(getFewestSteps(intersections)).isEqualTo(30)
    }

    @Test
    fun aocSample2() {
        val grid = initGrid(2000, 2000)
        val centralPort = getGridElement(grid, 1000,1000)

        wire2grid(grid, centralPort, "R75,D30,R83,U83,L12,D49,R71,U7,L72",0)
        wire2grid(grid, centralPort, "U62,R66,U55,R34,D71,R55,D58,R83",1)

        val intersections = getIntersections(grid)
        assertThat(getClosestDistance(intersections, centralPort)).isEqualTo(159)
        assertThat(getFewestSteps(intersections)).isEqualTo(610)
    }

    @Test
    fun aocSample3() {
        val grid = initGrid(2000, 2000)
        val centralPort = getGridElement(grid, 1000,1000)

        wire2grid(grid, centralPort, "R98,U47,R26,D63,R33,U87,L62,D20,R33,U53,R51",0)
        wire2grid(grid, centralPort, "U98,R91,D20,R16,D67,R40,U7,R15,U6,R7",1)

        val intersections = getIntersections(grid)
        assertThat(getClosestDistance(intersections, centralPort)).isEqualTo(135)
        assertThat(getFewestSteps(intersections)).isEqualTo(410)
    }
}