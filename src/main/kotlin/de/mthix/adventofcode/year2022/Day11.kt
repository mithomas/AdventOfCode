package de.mthix.adventofcode.year2022

import com.marcinmoskala.math.product
import de.mthix.adventofcode.*
import java.lang.IllegalArgumentException
import java.math.BigInteger

fun main() {
    val example = 0
    val lines = linesOfDay(2022, 11, example).onEach { it.trim() }.chunked(7)
    println(lines)


    val reliefMonkeys = lines.mapIndexed { i, str -> Monkey(i, example, true, str) }
    (1..20).forEach {
        reliefMonkeys.forEach { it.inspectAndThrow(reliefMonkeys) }
        println("$it: ${reliefMonkeys.map { it.items }}")
    }
    println(reliefMonkeys.map { it.itemsInspected })
    println("Puzzle 1: " + reliefMonkeys.map { it.itemsInspected }.sortedDescending().subList(0, 2).product())


    val worryMonkeys = lines.mapIndexed { i, str -> Monkey(i, example, false, str) }
    (1..10000).forEach {
        worryMonkeys.forEach { it.inspectAndThrow(worryMonkeys) }
        //println("$it: ${worryMonkeys.map { it.items }}")
    }
    println(worryMonkeys.map { it.itemsInspected })
    println("Puzzle 2: " + worryMonkeys.map { it.itemsInspected }.sortedDescending().subList(0, 2).product())
}

class Monkey(private val index: Int, val example: Int, private val cutInThree: Boolean, initString: List<String>) {

    val items = ArrayDeque<Int>()
    var itemsInspected = 0

    private val dividableBy: Int

    private val trueMonkey: Int
    private val falseMonkey: Int

    private var monkeys = emptyList<Monkey>()


    init {
        initString[1].split(":")[1].split(",").forEach { items.add(it.trim().toInt()) }

        dividableBy = initString[3].split("by")[1].trim().toInt()

        trueMonkey = initString[4].split("monkey")[1].trim().toInt()
        falseMonkey = initString[5].split("monkey")[1].trim().toInt()
    }

    fun inspectAndThrow(monkeys: List<Monkey>) {
        this.monkeys = monkeys

        while (items.isNotEmpty()) {
            val value = manageWorry(operation(items.removeFirst()))
            itemsInspected++

            if (value % dividableBy == 0) {
                monkeys[trueMonkey].items.add(value)
            } else {
                monkeys[falseMonkey].items.add(value)
            }
        }
    }

    private fun manageWorry(value: BigInteger): Int {
        return (if (cutInThree) {
            value / 3.toBigInteger()
        } else {
            value % monkeys.map { it.dividableBy }.product().toBigInteger()
        }).toInt()
    }

    private fun operation(value: Int): BigInteger {
        val bigValue = value.toBigInteger()

        return when (example) {
            0 -> when (index) {
                0 -> bigValue * 13.toBigInteger()
                1 -> bigValue + 2.toBigInteger()
                2 -> bigValue + 1.toBigInteger()
                3 -> bigValue + 8.toBigInteger()
                4 -> bigValue * bigValue
                5 -> bigValue + 4.toBigInteger()
                6 -> bigValue * 17.toBigInteger()
                7 -> bigValue + 5.toBigInteger()
                else -> throw IllegalArgumentException("Wrong index value: $index")
            }

            1 -> {
                when (index) {
                    0 -> bigValue * 19.toBigInteger()
                    1 -> bigValue + 6.toBigInteger()
                    2 -> bigValue * bigValue
                    3 -> bigValue + 3.toBigInteger()
                    else -> throw IllegalArgumentException("Wrong index value: $index")
                }
            }

            else -> throw IllegalArgumentException("Wrong example value: $example")
        }
    }

    override fun toString(): String {
        return "Monkey[i=$index,ex=$example,true=$trueMonkey,fls=$falseMonkey,divBy=$dividableBy,inspected=$itemsInspected,items=$items]"
    }
}