package de.mthix.adventofcode.year2021

import de.mthix.adventofcode.linesOfDay
import java.math.BigInteger

val pairs = mapOf(
    "(" to ")",
    "[" to "]",
    "{" to "}",
    "<" to ">")

enum class State(private val illegalChar : String, val points : Int) {
    ROUND(")", 3),
    SQUARE("]", 57),
    CURLY("}", 1197),
    ANGLE(">",25137),
    LEGAL("", 0),
    INCOMPLETE("", 0),
    UNKNOWN("", 0);


    fun isLegal() = illegalChar.isEmpty()

    companion object {
        private val map = State.values().associateBy(State::illegalChar)
        fun fromString(string: String) = map.getOrDefault(string, UNKNOWN)
    }
}

data class ParseResult(val state : State, val chunkStartElements : List<String>)


fun main() {
    val lines = linesOfDay(2021,10,0)

    val parsedLines = lines.map { parse(it, listOf()) }
    println("\n\n $parsedLines")

    println("\n\n ${parsedLines.map { it.state.points }.sum()}")

    val closingSeq = lines
        .map { parse(it, listOf()) }
        .filter { it.state.isLegal() }
        .map { calculateCompletionScore(it.chunkStartElements) }
        .sorted()
    println("$closingSeq")

    val middleScore =  closingSeq[closingSeq.size / 2]
    println(middleScore)
}

fun parse(line : String, chunkStartElements : List<String>) : ParseResult {
    //print("'$chunkStartElements' : $line")

    if(line.isEmpty()) {
        //println(" ==> empty")
        return if (chunkStartElements.isEmpty()) ParseResult(State.LEGAL, chunkStartElements) else ParseResult(State.INCOMPLETE, chunkStartElements)
    }

    val currentChar = line[0].toString()
    val remainderLine = line.substring(1)
    //print(" : $currentChar")

    if(pairs.containsKey(currentChar)) { // new chunk starts
        //println(" ==> new chunk")
        return parse(remainderLine, chunkStartElements+currentChar)
    } else { // chunk closes
        if(currentChar == pairs[chunkStartElements.last()]) { // legal close
            //println(" ==> legal close")
            return parse(remainderLine, chunkStartElements.dropLast(1))
        } else { // illegal close
            //println(" ==> illegal close")
            return ParseResult(State.fromString(currentChar), chunkStartElements)
        }
    }
}

fun calculateCompletionScore(chunkStartElements : List<String>) : BigInteger {
    val closingSeq = chunkStartElements.map { pairs[it] }.reversed()
    println("$closingSeq")

    var score = BigInteger.ZERO
    closingSeq.forEach {
        score *= BigInteger.valueOf(5)
        when(it) {
            ")" -> score += BigInteger.ONE
            "]" -> score += BigInteger.TWO
            "}" -> score += BigInteger.valueOf(3)
            ">" -> score += BigInteger.valueOf(4)
        }
    }

    return score
}