package de.mthix.adventofcode.year2019

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class Day5Test {

    @Nested
    inner class Process {

        @Test
        fun aocDay2Sample1() {
            val program = arrayOf(1, 0, 0, 0, 99)
            assertThat(process(program, 42)).isEmpty()
            assertThat(program).isEqualTo(arrayOf(2, 0, 0, 0, 99))
        }

        @Test
        fun aocDay2Sample2() {
            val program = arrayOf(2, 3, 0, 3, 99)
            assertThat(process(program, 42)).isEmpty()
            assertThat(program).isEqualTo(arrayOf(2, 3, 0, 6, 99))
        }

        @Test
        fun aocDay2Sample3() {
            val program = arrayOf(2, 4, 4, 5, 99, 0)
            assertThat(process(program, 42)).isEmpty()
            assertThat(program).isEqualTo(arrayOf(2, 4, 4, 5, 99, 9801))
        }

        @Test
        fun aocDay2Sample4() {
            val program = arrayOf(1, 1, 1, 4, 99, 5, 6, 0, 99)
            assertThat(process(program, 42)).isEmpty()
            assertThat(program).isEqualTo(arrayOf(30, 1, 1, 4, 2, 5, 6, 0, 99))
        }

        @Test
        fun aocDay5Sample1() {
            assertThat(process(arrayOf(3, 0, 4, 0, 99), 42)).containsExactly(42)
        }

        @Test
        fun aocDay5Sample2() {
            val program = arrayOf(1002,4,3,4,33)
            assertThat(process(program, 42)).isEmpty()
            assertThat(program).isEqualTo(arrayOf(1002,4,3,4,99))
        }
    }

    @Nested
    inner class ParameterModeOf {

        @Test
        fun default() {
            assertThat(ParameterMode.of("2", 1)).isEqualTo(ParameterMode.POSITION)
        }

        @Nested
        inner class Normal {

            @Test
            fun normalOp1() {
                assertThat(ParameterMode.of("1002", 1)).isEqualTo(ParameterMode.POSITION)
            }

            @Test
            fun normalOp2() {
                assertThat(ParameterMode.of("1002", 2)).isEqualTo(ParameterMode.IMMEDIATE)
            }

            @Test
            fun normalOp3() {
                assertThat(ParameterMode.of("1002", 3)).isEqualTo(ParameterMode.POSITION)
            }
        }
    }

    @Nested
    inner class Equals8Position {

        @Test
        fun equal() {
            assertThat(process(arrayOf(3, 9, 8, 9, 10, 9, 4, 9, 99, -1, 8), 8)).containsExactly(1)
        }

        @Test
        fun larger() {
            assertThat(process(arrayOf(3, 9, 8, 9, 10, 9, 4, 9, 99, -1, 8), 9)).containsExactly(0)
        }

        @Test
        fun smaller() {
            assertThat(process(arrayOf(3, 9, 8, 9, 10, 9, 4, 9, 99, -1, 8), 7)).containsExactly(0)
        }
    }

    @Nested
    inner class LessThan8Position {

        @Test
        fun equals() {
            assertThat(process(arrayOf(3, 9, 7, 9, 10, 9, 4, 9, 99, -1, 8), 8)).containsExactly(0)
        }

        @Test
        fun smaller() {
            assertThat(process(arrayOf(3, 9, 7, 9, 10, 9, 4, 9, 99, -1, 8), 7)).containsExactly(1)
        }

        @Test
        fun larger() {
            assertThat(process(arrayOf(3, 9, 7, 9, 10, 9, 4, 9, 99, -1, 8), 9)).containsExactly(0)
        }
    }

    @Nested
    inner class Equals8Immediate {

        @Test
        fun equal() {
            assertThat(process(arrayOf(3, 3, 1108, -1, 8, 3, 4, 3, 99), 8)).containsExactly(1)
        }

        @Test
        fun smaller() {
            assertThat(process(arrayOf(3, 3, 1108, -1, 8, 3, 4, 3, 99), 7)).containsExactly(0)
        }

        @Test
        fun larger() {
            assertThat(process(arrayOf(3, 3, 1108, -1, 8, 3, 4, 3, 99), 9)).containsExactly(0)
        }
    }


    @Nested
    inner class LessThan8Immediate {


        @Test
        fun equal() {
            assertThat(process(arrayOf(3, 3, 1107, -1, 8, 3, 4, 3, 99), 8)).containsExactly(0)
        }

        @Test
        fun smaller() {
            assertThat(process(arrayOf(3, 3, 1107, -1, 8, 3, 4, 3, 99), 7)).containsExactly(1)
        }

        @Test
        fun larger() {
            assertThat(process(arrayOf(3, 3, 1107, -1, 8, 3, 4, 3, 99), 9)).containsExactly(0)
        }
    }

    @Nested
    inner class JumpsPosition {

        @Test
        fun zero() {
            assertThat(process(arrayOf(3,12,6,12,15,1,13,14,13,4,13,99,-1,0,1,9), 0)).containsExactly(0)
        }

        @Test
        fun nonzero() {
            assertThat(process(arrayOf(3,12,6,12,15,1,13,14,13,4,13,99,-1,0,1,9), 1)).containsExactly(1)
        }
    }

    @Nested
    inner class JumpsImmediate {

        @Test
        fun zero() {
            assertThat(process(arrayOf(3,3,1105,-1,9,1101,0,0,12,4,12,99,1), 0)).containsExactly(0)
        }

        @Test
        fun nonzero() {
            assertThat(process(arrayOf(3,3,1105,-1,9,1101,0,0,12,4,12,99,1), 1)).containsExactly(1)
        }
    }

    @Nested
    inner class Around8 {

        val program = arrayOf(3, 21,1008,21,8,20,1005,20,22,107,8,21,20,1006,20,31,
                1106,0,36,98,0,0,1002,21,125,20,4,20,1105,1,46,104,
                999,1105,1,46,1101,1000,1,20,4,20,1105,1,46,98,99)

        @Test
        fun equal() {
            assertThat(process(program, 8)).containsExactly(1000)
        }

        @Test
        fun smaller() {
            assertThat(process(program, 7)).containsExactly(999)
        }

        @Test
        fun larger() {
            assertThat(process(program, 9)).containsExactly(1001)
        }
    }
}