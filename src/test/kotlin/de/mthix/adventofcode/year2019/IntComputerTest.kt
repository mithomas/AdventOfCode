package de.mthix.adventofcode.year2019

import de.mthix.adventofcode.longArrayFromCsvInputForDay
import de.mthix.adventofcode.year2019.IntComputer.ParamMode
import de.mthix.adventofcode.year2019.IntComputer.ParamMode.IMMEDIATE
import de.mthix.adventofcode.year2019.IntComputer.ParamMode.POSITION
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class IntComputerTest {

    private var computer: IntComputer? = null


    @Nested
    inner class Day2 {

        @Nested
        inner class Process {

            @Test
            fun aocDay2Sample1() {
                computer = IntComputer(arrayOf(1, 0, 0, 0, 99))
                assertThat(computer?.process(42)).isEmpty()
                assertThat(computer?.program).startsWith(2, 0, 0, 0, 99)
            }

            @Test
            fun aocDay2Sample2() {
                computer = IntComputer(arrayOf(2, 3, 0, 3, 99))
                assertThat(computer?.process(42)).isEmpty()
                assertThat(computer?.program).startsWith(2, 3, 0, 6, 99)
            }

            @Test
            fun aocDay2Sample3() {
                computer = IntComputer(arrayOf(2, 4, 4, 5, 99, 0))
                assertThat(computer?.process(42)).isEmpty()
                assertThat(computer?.program).startsWith(2, 4, 4, 5, 99, 9801)
            }

            @Test
            fun aocDay2Sample4() {
                computer = IntComputer(arrayOf(1, 1, 1, 4, 99, 5, 6, 0, 99))
                assertThat(computer?.process(42)).isEmpty()
                assertThat(computer?.program).startsWith(30, 1, 1, 4, 2, 5, 6, 0, 99)
            }

            @Test
            fun aocDay5Sample1() {
                computer = IntComputer(arrayOf(3, 0, 4, 0, 99))
                assertThat(computer?.process(42)).containsExactly(42)
            }

            @Test
            fun aocDay5Sample2() {
                computer = IntComputer(arrayOf(1002, 4, 3, 4, 33))
                assertThat(computer?.process(42)).isEmpty()
                assertThat(computer?.program).startsWith(1002, 4, 3, 4, 99)
            }
        }

        @Test
        fun solutionPart1() {
            assertThat(process(longArrayFromCsvInputForDay(2019,2), 12, 2)).isEqualTo(5534943L)
        }

        @Test
        fun solutionPart2() {
            assertThat(process(longArrayFromCsvInputForDay(2019,2), 76, 3)).isEqualTo(19690720L)
        }
    }

    @Nested
    inner class ParamModeOf {

        @Test
        fun default() {
            assertThat(ParamMode.of("2", 1)).isEqualTo(POSITION)
        }

        @Nested
        inner class Normal {

            @Test
            fun normalOp1() {
                assertThat(ParamMode.of("1002", 1)).isEqualTo(POSITION)
            }

            @Test
            fun normalOp2() {
                assertThat(ParamMode.of("1002", 2)).isEqualTo(IMMEDIATE)
            }

            @Test
            fun normalOp3() {
                assertThat(ParamMode.of("1002", 3)).isEqualTo(POSITION)
            }
        }
    }

    @Nested
    inner class Day5 {

        @Nested
        inner class Equals8Position {

            @BeforeEach
            fun initComputer() {
                computer = IntComputer(arrayOf(3, 9, 8, 9, 10, 9, 4, 9, 99, -1, 8))
            }

            @Test
            fun equal() {
                assertThat(computer?.process(8)).containsExactly(1)
            }

            @Test
            fun larger() {
                assertThat(computer?.process(9)).containsExactly(0)
            }

            @Test
            fun smaller() {
                assertThat(computer?.process(7)).containsExactly(0)
            }
        }

        @Nested
        inner class LessThan8Position {

            @BeforeEach
            fun initComputer() {
                computer = IntComputer(arrayOf(3, 9, 7, 9, 10, 9, 4, 9, 99, -1, 8))
            }

            @Test
            fun equals() {
                assertThat(computer?.process(8)).containsExactly(0)
            }

            @Test
            fun smaller() {
                assertThat(computer?.process(7)).containsExactly(1)
            }

            @Test
            fun larger() {
                assertThat(computer?.process(9)).containsExactly(0)
            }
        }

        @Nested
        inner class Equals8Immediate {

            @BeforeEach
            fun initComputer() {
                computer = IntComputer(arrayOf(3, 3, 1108, -1, 8, 3, 4, 3, 99))
            }

            @Test
            fun equal() {
                assertThat(computer?.process(8)).containsExactly(1)
            }

            @Test
            fun smaller() {
                assertThat(computer?.process(7)).containsExactly(0)
            }

            @Test
            fun larger() {
                assertThat(computer?.process(9)).containsExactly(0)
            }
        }


        @Nested
        inner class LessThan8Immediate {

            @BeforeEach
            fun initComputer() {
                computer = IntComputer(arrayOf(3, 3, 1107, -1, 8, 3, 4, 3, 99))
            }

            @Test
            fun equal() {
                assertThat(computer?.process(8)).containsExactly(0)
            }

            @Test
            fun smaller() {
                assertThat(computer?.process(7)).containsExactly(1)
            }

            @Test
            fun larger() {
                assertThat(computer?.process(9)).containsExactly(0)
            }
        }

        @Nested
        inner class JumpsPosition {

            @BeforeEach
            fun initComputer() {
                computer = IntComputer(arrayOf(3, 12, 6, 12, 15, 1, 13, 14, 13, 4, 13, 99, -1, 0, 1, 9))
            }

            @Test
            fun zero() {
                assertThat(computer?.process(0)).containsExactly(0)
            }

            @Test
            fun nonzero() {
                assertThat(computer?.process(1)).containsExactly(1)
            }
        }

        @Nested
        inner class JumpsImmediate {

            @BeforeEach
            fun initComputer() {
                computer = IntComputer(arrayOf(3, 3, 1105, -1, 9, 1101, 0, 0, 12, 4, 12, 99, 1))
            }

            @Test
            fun zero() {
                assertThat(computer?.process(0)).containsExactly(0)
            }

            @Test
            fun nonzero() {
                assertThat(computer?.process(1)).containsExactly(1)
            }
        }

        @Nested
        inner class Around8Test {

            @BeforeEach
            fun initComputer() {
                computer = IntComputer(arrayOf(3, 21, 1008, 21, 8, 20, 1005, 20, 22, 107, 8, 21, 20, 1006, 20, 31,
                        1106, 0, 36, 98, 0, 0, 1002, 21, 125, 20, 4, 20, 1105, 1, 46, 104,
                        999, 1105, 1, 46, 1101, 1000, 1, 20, 4, 20, 1105, 1, 46, 98, 99))
            }

            @Test
            fun equal() {
                assertThat(computer?.process(8)).containsExactly(1000)
            }

            @Test
            fun smaller() {
                assertThat(computer?.process(7)).containsExactly(999)
            }

            @Test
            fun larger() {
                assertThat(computer?.process(9)).containsExactly(1001)
            }
        }

        @Nested
        inner class Solution {

            @BeforeEach
            fun initComputer() {
                computer = IntComputer(longArrayFromCsvInputForDay(2019,5))
            }

            @Test
            fun part1() {
                assertThat(computer?.process(1)?.last()).isEqualTo(5346030)
            }

            @Test
            fun part2() {
                assertThat(computer?.process(5)).containsOnly(513116)
            }
        }
    }

    @Nested
    inner class Day9 {

        @Test
        fun sample1() {
            val program = arrayOf(109L,1,204,-1,1001,100,1,100,1008,100,16,101,1006,101,0,99)
            assertThat(IntComputer(program, 1000).process()).isEqualTo(program.asList())
        }

        @Test
        fun sample2() {
            assertThat(IntComputer(arrayOf(1102,34915192,34915192,7,4,7,99,0)).process()[0].toString().length).isEqualTo(16)
        }

        @Test
        fun sample3() {
            assertThat(IntComputer(arrayOf(104,1125899906842624,99)).process()).containsOnly(1125899906842624)
        }

        @Nested
        inner class Solution {

            @BeforeEach
            fun initComputer() {
                computer = IntComputer(longArrayFromCsvInputForDay(2019,9), 1500)
            }

            @Test
            fun part1() {
                assertThat(computer?.process(1)).containsOnly(2714716640)
            }

            @Test
            fun part2() {
                assertThat(computer?.process(2)).containsOnly(58879)
            }
        }
    }
}