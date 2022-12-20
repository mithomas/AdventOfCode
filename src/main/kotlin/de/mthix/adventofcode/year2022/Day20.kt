package de.mthix.adventofcode.year2022

import de.mthix.adventofcode.linesOfDay
import java.math.BigInteger
import java.math.BigInteger.ZERO
import kotlin.math.abs

fun main() {
    val linesOfDay = linesOfDay(2022, 20, 0)
    val list = initList(linesOfDay)

    val mixedList = mix(list)
    val nullList = zeroList(mixedList)
    println(list)
    println(mixedList)
    println(nullList)
    println("Puzzle 1: " + (getValue(nullList, 1000) + getValue(nullList, 2000) + getValue(nullList, 3000)))


    val bigList = initList(linesOfDay).onEach { it.value = it.value * BigInteger("811589153") }
    var bigNullList = zeroList(bigList)
    println(bigList)
    (1..10).forEach {
        mix(bigList)
        bigNullList = zeroList(bigList)
        println(bigNullList)
    }
    println("Puzzle 2: " + (getValue(bigNullList, 1000) + getValue(bigNullList, 2000) + getValue(bigNullList, 3000)))
}

fun mix(list: List<NumberNode>) = list.onEach { it.move() }.first().toList()
fun zeroList(list: List<NumberNode>) = list.first { it.value == ZERO }.toList()
fun getValue(list: List<NumberNode>, index: Int) = list[index % list.size].value

fun initList(linesOfDay: List<String>): List<NumberNode> {
    val list = linesOfDay.map { NumberNode(it.toBigInteger()) }

    list.onEachIndexed { i, it ->
        it.next = list[if (i + 1 < list.size) (i + 1) else 0]
        it.prev = list[if (i - 1 >= 0) (i - 1) else list.size - 1]
        it.listSize = list.size
    }

    return list
}

data class NumberNode(var value: BigInteger, var prev: NumberNode? = null, var next: NumberNode? = null, var listSize: Int = 0) {

    fun move() {
        val value = this.value.mod((listSize-1).toBigInteger()).toInt()

        if(value != 0) {
            var newPrev = this

            if (value > 0) (1..value).forEach { newPrev = newPrev.next!! }
            else (1..abs(value)).forEach { newPrev = newPrev.prev!! }

            this.prev!!.next = this.next
            this.next!!.prev = this.prev

            this.prev = newPrev
            this.next = newPrev.next

            newPrev.next!!.prev = this
            newPrev.next = this
        }
    }

    fun toList() : List<NumberNode> {
        val list = mutableListOf<NumberNode>()
        var current = this

        do {
            list.add(current)
            current = current.next!!
        } while (current != this)

        return list
    }
    override fun toString() = "$value"
}
