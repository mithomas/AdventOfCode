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
        //println("$it: ${worryMonkeys.map { it.items }}")
        worryMonkeys.forEach { it.inspectAndThrow(worryMonkeys) }
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

    init {
        initString[1].split(":")[1].split(",").forEach { items.add(it.trim().toInt()) }

        dividableBy = initString[3].split("by")[1].trim().toInt()

        trueMonkey = initString[4].split("monkey")[1].trim().toInt()
        falseMonkey = initString[5].split("monkey")[1].trim().toInt()
    }

    fun inspectAndThrow(monkeys: List<Monkey>) {
        while (items.isNotEmpty()) {
            val value = manageWorry(operation(items.removeFirst().toBigInteger()), monkeys.map { it.dividableBy }.product())
            itemsInspected++

            if (value % dividableBy == 0) {
                monkeys[trueMonkey].items.add(value)
            } else {
                monkeys[falseMonkey].items.add(value)
            }
        }
    }

    private fun manageWorry(value : BigInteger, dividableProduct : Long) : Int {
        return (if(cutInThree) {
            value / 3.toBigInteger()
        } else {
            value % dividableProduct.toBigInteger()
        }).toInt()
    }

    private fun operation(value: BigInteger): BigInteger {
        return when (example) {
            0 -> when (index) {
                0 -> value * 13.toBigInteger()
                1 -> value + 2.toBigInteger()
                2 -> value + 1.toBigInteger()
                3 -> value + 8.toBigInteger()
                4 -> value * value
                5 -> value + 4.toBigInteger()
                6 -> value * 17.toBigInteger()
                7 -> value + 5.toBigInteger()
                else -> throw IllegalArgumentException("Wrong index value: $index")
            }

            1 -> {
                when (index) {
                    0 -> value * 19.toBigInteger()
                    1 -> value + 6.toBigInteger()
                    2 -> value * value
                    3 -> value + 3.toBigInteger()
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