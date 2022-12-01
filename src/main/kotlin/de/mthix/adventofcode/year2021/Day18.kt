package de.mthix.adventofcode.year2021

import de.mthix.adventofcode.linesOfDay
import java.lang.IllegalStateException
import kotlin.math.ceil
import kotlin.math.floor

abstract class SnailfishNumberElement

data class SnailfishValue(val value : Int) : SnailfishNumberElement() {
    override fun toString(): String {
        return value.toString()
    }
}

data class SnailfishRecNumber(val x : SnailfishNumberElement,
                           val y : SnailfishNumberElement,
                           val parent: SnailfishRecNumber,
                           val depth : Int) : SnailfishNumberElement() {

    override fun toString(): String {
        return "[$x,$y]"
    }
}

data class SnailfishNumber(var value : Int, var depth : Int) {

    override fun toString(): String {
        return "$value@$depth"
    }
}

fun main() {
    var sum = listOf<SnailfishNumber>()
    linesOfDay(2021,18,1).map { toSFN(it) }.forEach { sum = add(sum, it) }

    println(sum)
    println(toSFN("[[[[8,7],[7,7]],[[8,6],[7,7]]],[[[0,7],[6,6]],[8,7]]]"))
}

fun add(s1 : List<SnailfishNumber>, s2 : List<SnailfishNumber>) : List<SnailfishNumber> {
    val result = (s1 + s2)
    result.forEach { it.depth++ }
    return reduce(result)
}

fun reduce(sn : List<SnailfishNumber>) : List<SnailfishNumber> {
    var i = 0
    var steps = 0
    var predecessorDepth = -1
    var result = sn

    println("reduce $result")
    while(i < result.size) {
        val curSN = result[i]
        print("$i : $curSN -> ")

        if(predecessorDepth == 5 && curSN.depth == 5) { // explode
            result = explode(result, i-1, i)
            println("explode at ${i-1},$i > $result")
            predecessorDepth = -1
            i = 0
        } else if(curSN.value > 9) { // reduce
            result = split(result, i)
            println("split at $i > $result")
            predecessorDepth = -1
            i = 0
        } else {
            println("nothing to do")
            predecessorDepth = curSN.depth
            i++
        }

        steps++
    }

    return result
}

fun explode(sn : List<SnailfishNumber>, iLeft : Int, iRight : Int) : List<SnailfishNumber> {
    val left = sn.subList(0, iLeft)
    val right = sn.subList(iRight+1, sn.size)

    if(left.isNotEmpty()) { left.last().value += sn[iLeft].value }
    if(right.isNotEmpty()) { right.first().value += sn[iRight].value }

    val result =  left.toMutableList()
    result.add(SnailfishNumber(0,4))
    result.addAll(right)

    return result
}

fun split(sn : List<SnailfishNumber>, i : Int) : List<SnailfishNumber> {
    val left = sn.subList(0, i)
    val right = sn.subList(i+1, sn.size)

    val cur = sn[i]

    val result =  left.toMutableList()
    result.add(SnailfishNumber(floor(cur.value.toDouble()/2).toInt(),cur.depth+1))
    result.add(SnailfishNumber(ceil(cur.value.toDouble()/2).toInt(),cur.depth+1))
    result.addAll(right)

    return result
}

fun toSFN(line : String) : List<SnailfishNumber> {
    val result = mutableListOf<SnailfishNumber>()
    var depth = 0
    var number = mutableListOf<Char>()

    for(char in line) {
        when(char) {
            '[' -> depth++
            ']' -> depth--
            ',' -> { /* do nothing */ }
            else -> result.add(SnailfishNumber(char.toString().toInt(16), depth))
        }
    }

    if(depth != 0) { throw IllegalStateException("Depth should be 0 but was $depth.") }

    return result
}