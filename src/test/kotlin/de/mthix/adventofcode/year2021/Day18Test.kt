package de.mthix.adventofcode.year2021

import org.junit.jupiter.api.*
import org.assertj.core.api.Assertions.*

class ToNumbers {

    @Test
    fun example1() {
        assertThat(toSFN("[1,2]")).isEqualTo(listOf(
            SnailfishNumber(1,1),
            SnailfishNumber(2,1)
        ))
    }

    @Test
    fun example2() {
        assertThat(toSFN("[[1,2],3]")).isEqualTo(listOf(
            SnailfishNumber(1,2),
            SnailfishNumber(2,2),
            SnailfishNumber(3,1)
        ))
    }

    @Test
    fun example3() {
        assertThat(toSFN("[9,[8,7]]")).isEqualTo(listOf(
            SnailfishNumber(9,1),
            SnailfishNumber(8,2),
            SnailfishNumber(7,2)
        ))
    }

    @Test
    fun example4() {
        assertThat(toSFN("[[[[1,2],[3,4]],[[5,6],[7,8]]],9]")).isEqualTo(listOf(
            SnailfishNumber(1,4),
            SnailfishNumber(2,4),
            SnailfishNumber(3,4),
            SnailfishNumber(4,4),
            SnailfishNumber(5,4),
            SnailfishNumber(6,4),
            SnailfishNumber(7,4),
            SnailfishNumber(8,4),
            SnailfishNumber(9,1)
        ))
    }
}

class Add {

    @Test
    fun example1() {
        assertThat(add(toSFN("[1,2]"), toSFN("[[3,4],5]"))).isEqualTo(toSFN("[[1,2],[[3,4],5]]"))
    }

    @Test
    fun example2() {
        assertThat(add(add(add(toSFN("[1,1]"),toSFN("[2,2]")),toSFN("[3,3]")), toSFN("[4,4]"))).isEqualTo(toSFN("[[[[1,1],[2,2]],[3,3]],[4,4]]"))
    }

    @Test
    fun example3() {
        assertThat(add(toSFN("[[[0,[4,5]],[0,0]],[[[4,5],[2,6]],[9,5]]]"), toSFN("[7,[[[3,7],[4,3]],[[6,3],[8,8]]]]"))).isEqualTo(
            toSFN("[[[[4,0],[5,4]],[[7,7],[6,0]]],[[8,[7,7]],[[7,9],[5,0]]]]"))
    }
}

class Explode {

    @Test
    fun example1() {
        assertThat(explode(toSFN("[[[[[9,8],1],2],3],4]"), 0, 1)).isEqualTo(toSFN("[[[[0,9],2],3],4]"))
    }

    @Test
    fun example2() {
        assertThat(explode(toSFN("[7,[6,[5,[4,[3,2]]]]]"), 4,5)).isEqualTo(toSFN("[7,[6,[5,[7,0]]]]"))
    }

    @Test
    fun example3() {
        assertThat(explode(toSFN("[[6,[5,[4,[3,2]]]],1]"), 3,4)).isEqualTo(toSFN("[[6,[5,[7,0]]],3]"))
    }

    @Test
    fun example4() {
        assertThat(explode(toSFN("[[3,[2,[1,[7,3]]]],[6,[5,[4,[3,2]]]]]"), 3,4)).isEqualTo(toSFN("[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]"))
    }

    @Test
    fun example5() {
        assertThat(explode(toSFN("[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]"), 7, 8)).isEqualTo(toSFN("[[3,[2,[8,0]]],[9,[5,[7,0]]]]"))
    }

    @Test
    fun example6() {
        assertThat(explode(toSFN("[[[[[4,3],4],4],[7,[[8,4],9]]],[1,1]]"), 0, 1)).isEqualTo(toSFN("[[[[0,7],4],[7,[[8,4],9]]],[1,1]]"))
    }

    @Test
    fun example7() {
        assertThat(explode(toSFN("[[[[0,7],4],[7,[[8,4],9]]],[1,1]]"), 4, 5)).isEqualTo(toSFN("[[[[0,7],4],[F,[0,D]]],[1,1]]"))
    }
}

class Split {

    @Test
    fun example1() {
        assertThat(split(toSFN("[[[[0,7],4],[F,[0,D]]],[1,1]]"), 3)).isEqualTo(toSFN("[[[[0,7],4],[[7,8],[0,D]]],[1,1]]"))
    }

    @Test
    fun example2() {
        assertThat(split(toSFN("[[[[0,7],4],[[7,8],[0,D]]],[1,1]]"), 6)).isEqualTo(toSFN("[[[[0,7],4],[[7,8],[0,[6,7]]]],[1,1]]"))
    }
}

class Reduce {

    @Test
    fun example1() {
        assertThat(reduce(toSFN("[[[[[4,3],4],4],[7,[[8,4],9]]],[1,1]]"))).isEqualTo(toSFN("[[[[0,7],4],[[7,8],[6,0]]],[8,1]]"))
    }
}
