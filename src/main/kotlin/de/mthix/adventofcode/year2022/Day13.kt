package de.mthix.adventofcode.year2022

import com.marcinmoskala.math.product
import de.mthix.adventofcode.linesOfDay
import java.lang.IllegalStateException
import java.lang.Integer.min
import kotlin.math.sign

fun main() {
    val linePairs = linesOfDay(2022, 13)
        .chunked(3)
        .map { pair -> pair.filter { it.isNotEmpty() } }
        .map { pair -> pair.map { convertToElement(it) } }

    println("Puzzle 1: " + linePairs.map { it[0].compareTo(it[1]) }.mapIndexed { i, v -> if (v == -1) (i + 1) else 0 }
        .sum())

    val dividerPackets = listOf(
        ListElement(mutableListOf(ListElement(mutableListOf(IntElement(2))))),
        ListElement(mutableListOf(ListElement(mutableListOf(IntElement(6)))))
    )
    val allLines = linePairs.flatten() + dividerPackets
    println("Puzzle 2: " + allLines.sorted().mapIndexed { i,e -> if(dividerPackets.contains(e)) (i+1) else 0 }.filter { it > 0 }.product() )
}

fun convertToElement(input: String): Element {
    val elements = ArrayDeque<ListElement>()

    var i = 0
    while (i < input.length) {
        val c = input[i]

        if (c == '[') {
            elements.add(ListElement())
        } else if (c == ']') {
            val complete = elements.removeLast()

            if (elements.isEmpty() && i == input.length - 1) {
                return complete
            } else {
                elements.last().elements.add(complete)
            }
        } else if (c.isDigit()) {
            val digits = mutableListOf(c)

            val nextChar = input[i + 1]
            if (nextChar.isDigit()) {
                digits.add(nextChar)
                i++
            }

            elements.last().elements.add(IntElement(digits.joinToString("").toInt()))
        }

        i++
    }

    throw IllegalStateException()
}

interface Element : Comparable<Element> {

    override fun compareTo(other: Element): Int
}

class IntElement(val value: Int) : Element {

    override fun compareTo(other: Element) = if (other is IntElement) {
        (this.value - other.value).sign
    } else {
        ListElement(mutableListOf(this)).compareTo(other)
    }

    override fun toString(): String {
        return "E:$value"
    }
}

class ListElement(val elements: MutableList<Element> = mutableListOf()) : Element {

    override fun compareTo(other: Element): Int {
        val comp = if (other is ListElement) other else ListElement(mutableListOf(other))

        for (i in (0 until min(this.elements.size, comp.elements.size))) {
            val c = this.elements[i].compareTo(comp.elements[i])
            if (c != 0) {
                return c
            }
        }

        return (this.elements.size - comp.elements.size).sign
    }

    override fun toString(): String {
        return "E:$elements"
    }
}