package de.mthix.adventofcode.year2022

import org.junit.jupiter.api.Test
import org.assertj.core.api.Assertions.*

class FindStartOfPacket {

    @Test
    fun example1() {
        assertThat(findStartOfPacket("mjqjpqmgbljsphdztnvjfqwrcgsmlb")).isEqualTo(7)
    }

    @Test
    fun example2() {
        assertThat(findStartOfPacket("bvwbjplbgvbhsrlpgdmjqwftvncz")).isEqualTo(5)
    }

    @Test
    fun example3() {
        assertThat(findStartOfPacket("nppdvjthqldpwncqszvftbrmjlhg")).isEqualTo(6)
    }

    @Test
    fun example4() {
        assertThat(findStartOfPacket("nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg")).isEqualTo(10)
    }

    @Test
    fun example5() {
        assertThat(findStartOfPacket("zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw")).isEqualTo(11)
    }
}