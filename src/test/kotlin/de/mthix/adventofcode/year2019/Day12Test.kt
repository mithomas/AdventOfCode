package de.mthix.adventofcode.year2019

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet

class Day12Test {

    @Test
    fun aocSample1() {
        val moons = Pair(Moon(5, 2, 3), Moon(3, 2, 5))
        applyGravity(moons)

        assertThat(moons.first).isEqualTo(Moon(5, 2, 3, -1, 0, 1))
        assertThat(moons.second).isEqualTo(Moon(3, 2, 5, 1, 0, -1))
    }

    @Test
    fun negative() {
        val moons = Pair(Moon(-5, 2, 3), Moon(-3, 2, 5))
        applyGravity(moons)

        assertThat(moons.first).isEqualTo(Moon(-5, 2, 3, 1, 0, 1))
        assertThat(moons.second).isEqualTo(Moon(-3, 2, 5, -1, 0, -1))
    }

    @Test
    fun aocSample2() {
        val moon = Moon(1, 2, 3, -2, 0, 3)
        applyVelocity(moon)

        assertThat(moon).isEqualTo(Moon(-1, 2, 6, -2, 0, 3))
    }

    @Test
    fun aocSample3() {
        val moons = listOf(
                Moon(-1, 0, 2),
                Moon(2, -10, -7),
                Moon(4, -8, 8),
                Moon(3, 5, -1))

        performStep(moons)
        assertThat(moons).isEqualTo(listOf(
                Moon(2, -1, 1, 3, -1, -1),
                Moon(3, -7, -4, 1, 3, 3),
                Moon(1, -7, 5, -3, 1, -3),
                Moon(2, 2, 0, -1, -3, 1)))
    }

    @Nested
    inner class Part2 {

        @Test
        fun aocSample1() {
            val moons = listOf(
                    Moon(-1, 0, 2),
                    Moon(2, -10, -7),
                    Moon(4, -8, 8),
                    Moon(3, 5, -1))

            assertThat(findRepetition(moons)).isEqualTo(2772)
        }

        @Test
        fun aocSample2() {
            val moons = listOf(
                    Moon(-8, -10, 0),
                    Moon(5, 5, 10),
                    Moon(2, -7, 3),
                    Moon(9, -8, -3))

            assertThat(findRepetition(moons)).isEqualTo(4686774924)
        }
    }
}
