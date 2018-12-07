package de.mthix.adventofcode.year2018

import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class Day5Test {

    @Nested
    class ReducePolymer {

        @Test
        fun aocSample() {
            assertThat(reducePolymer("dabAcCaCBAcCcaDA")).isEqualTo("dabCBAcaDA")
        }
    }

    @Nested
    class IsSameTypeWithDifferentPolarity {

        @Nested
        class Same {

            @Test
            fun upper() {
                assertThat(isSameTypeWithDifferentPolarity('A', 'A')).isFalse()
            }

            @Test
            fun lower() {
                assertThat(isSameTypeWithDifferentPolarity('a', 'a')).isFalse()
            }
        }

        @Nested
        class Different {

            @Test
            fun upperLower() {
                assertThat(isSameTypeWithDifferentPolarity('A', 'a')).isTrue()
            }

            @Test
            fun lowerUpper() {
                assertThat(isSameTypeWithDifferentPolarity('a', 'A')).isTrue()
            }
        }
    }
}