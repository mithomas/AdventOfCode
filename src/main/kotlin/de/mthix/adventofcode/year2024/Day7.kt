package de.mthix.adventofcode.year2024

import de.mthix.adventofcode.linesOfDay
import java.math.BigInteger

fun main() {
    val equations = linesOfDay(2024, 7)
        .map { it.split(":") }
        .map { Pair(it[0].toBigInteger(), it[1].trim().split(" ").map { it.toInt() }) }

    val twoOpEquations = equations.filter { equate(it) }
    println(sum(twoOpEquations))

    val threeOpEquations = equations.filter { equate(it,true)}
    println(sum(threeOpEquations))
}

fun sum(equations:List<Pair<BigInteger,List<Int>>>) = equations.map { it.first }.sumOf { it }

fun equate(it:Pair<BigInteger,List<Int>>,threeOp:Boolean=false) = equate(it.first, it.second.first().toBigInteger(), it.second.subList(1, it.second.size),threeOp)

fun equate(result:BigInteger, current:BigInteger, numbers:List<Int>, threeOp:Boolean):Boolean {
    return if(current > result) {
        false
    } else if(numbers.isEmpty()) {
        return result == current
    } else {
        (equate(result,current*numbers.first().toBigInteger(), numbers.subList(1,numbers.size),threeOp)
                || equate(result,current+numbers.first().toBigInteger(), numbers.subList(1,numbers.size),threeOp))
                || (threeOp && equate(result,(current.toString()+numbers.first().toString()).toBigInteger(), numbers.subList(1,numbers.size),threeOp))
    }
}
