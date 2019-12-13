package de.mthix.adventofcode.year2019

import de.mthix.adventofcode.linesOfDay
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class Day10Test {

    @Test
    fun aocSample1() {
        val grid = inputAsBitGrid(listOf(".#..#", ".....", "#####", "....#", "...##"))
        println(grid)

        val asteroids = getAsteroids(grid)
        assertThat(asteroids).hasSize(10)
        println(asteroids)

        val linesOfSight = getLinesOfSight(grid[Pair(4,2)]!!, asteroids)
        assertThat(linesOfSight).hasSize(5)
        println(linesOfSight)

        assertThat(getLinesOfSight(grid[Pair(1,0)]!!, asteroids)).hasSize(7)
        assertThat(getLinesOfSight(grid[Pair(4,0)]!!, asteroids)).hasSize(7)
        assertThat(getLinesOfSight(grid[Pair(0,2)]!!, asteroids)).hasSize(6)
        assertThat(getLinesOfSight(grid[Pair(1,2)]!!, asteroids)).hasSize(7)
        assertThat(getLinesOfSight(grid[Pair(2,2)]!!, asteroids)).hasSize(7)
        assertThat(getLinesOfSight(grid[Pair(3,2)]!!, asteroids)).hasSize(7)
        assertThat(getLinesOfSight(grid[Pair(4,3)]!!, asteroids)).hasSize(7)
        assertThat(getLinesOfSight(grid[Pair(3,4)]!!, asteroids)).hasSize(8)
        assertThat(getLinesOfSight(grid[Pair(4,4)]!!, asteroids)).hasSize(7)

        assertThat(getMaxLinesOfSight(mapToLinesOfSight(asteroids))?.value).isEqualTo(8)
    }

    @Test
    fun aocSample2() {
        val grid = inputAsBitGrid(listOf("......#.#",
                "#..#.#....",
                "..#######.",
                ".#.#.###..",
                ".#..#.....",
                "..#....#.#",
                "#..#....#.",
                ".##.#..###",
                "##...#..#.",
                ".#....####"))

        val asteroids = getAsteroids(grid)
        assertThat(getMaxLinesOfSight(mapToLinesOfSight(asteroids))?.value).isEqualTo(33)
    }

    @Nested
    inner class LineOfSight {


        @Test
        fun aocSampleA() {
            val grid = inputAsBitGrid(listOf("#.........",
                    "...A......",
                    "......a...",
                    ".........a",
                    "..........",
                    "..........",
                    "..........",
                    "..........",
                    "..........",
                    ".........."))

            val asteroids = getAsteroids(grid)
            assertThat(asteroids).hasSize(4)
            println(asteroids)

            val linesOfSight = getLinesOfSight(grid[Pair(0,0)]!!, asteroids)
            assertThat(linesOfSight).hasSize(1)
            println(linesOfSight)
        }

        @Test
        fun aocSampleB() {
            val grid = inputAsBitGrid(listOf("#.........",
                    "..........",
                    "...B......",
                    "..........",
                    "......b...",
                    "..........",
                    ".........b",
                    "..........",
                    "..........",
                    ".........."))

            val asteroids = getAsteroids(grid)
            assertThat(asteroids).hasSize(4)
            println(asteroids)

            val linesOfSight = getLinesOfSight(grid[Pair(0,0)]!!, asteroids)
            assertThat(linesOfSight).hasSize(1)
            println(linesOfSight)
        }

        @Test
        fun aocSampleC() {
            val grid = inputAsBitGrid(listOf("#.........",
                    "..........",
                    "..........",
                    "...C......",
                    "....c.....",
                    ".....c....",
                    "......c...",
                    ".......c..",
                    "........c.",
                    ".........c"))

            val asteroids = getAsteroids(grid)
            assertThat(asteroids).hasSize(8)
            println(asteroids)

            val linesOfSight = getLinesOfSight(grid[Pair(0,0)]!!, asteroids)
            assertThat(linesOfSight).hasSize(1)
            println(linesOfSight)
        }

        @Test
        fun aocSampleD() {
            val grid = inputAsBitGrid(listOf("#.........",
                    "..........",
                    "..........",
                    "..D.......",
                    "..........",
                    "..........",
                    "....d.....",
                    "..........",
                    "..........",
                    "......d..."))

            val asteroids = getAsteroids(grid)
            assertThat(asteroids).hasSize(4)
            println(asteroids)

            val linesOfSight = getLinesOfSight(grid[Pair(0,0)]!!, asteroids)
            assertThat(linesOfSight).hasSize(1)
            println(linesOfSight)
        }

        @Test
        fun aocSampleE() {
            val grid = inputAsBitGrid(listOf("#.........",
                    "..........",
                    "..........",
                    ".E........",
                    "..........",
                    "..........",
                    "..e.......",
                    "..........",
                    "..........",
                    "...e......"))

            val asteroids = getAsteroids(grid)
            assertThat(asteroids).hasSize(4)
            println(asteroids)

            val linesOfSight = getLinesOfSight(grid[Pair(0,0)]!!, asteroids)
            assertThat(linesOfSight).hasSize(1)
            println(linesOfSight)
        }

        @Test
        fun aocSampleF() {
            val grid = inputAsBitGrid(listOf("#.........",
                    "..........",
                    "..........",
                    "..........",
                    "..F.......",
                    "..........",
                    "...f......",
                    "..........",
                    "....f.....",
                    ".........."))

            val asteroids = getAsteroids(grid)
            assertThat(asteroids).hasSize(4)
            println(asteroids)

            val linesOfSight = getLinesOfSight(grid[Pair(0,0)]!!, asteroids)
            assertThat(linesOfSight).hasSize(1)
            println(linesOfSight)
        }

        @Test
        fun aocSampleG() {
            val grid = inputAsBitGrid(listOf("#.........",
                    "..........",
                    "..........",
                    "....G.....",
                    "..........",
                    "..........",
                    "........g.",
                    "..........",
                    "..........",
                    ".........."))

            val asteroids = getAsteroids(grid)
            assertThat(asteroids).hasSize(3)
            println(asteroids)

            val linesOfSight = getLinesOfSight(grid[Pair(0,0)]!!, asteroids)
            assertThat(linesOfSight).hasSize(1)
            println(linesOfSight)
        }
    }
}
