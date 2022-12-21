package de.mthix.adventofcode.year2022

import de.mthix.adventofcode.linesOfDay
import java.math.BigInteger
import java.math.BigInteger.ONE
import java.math.BigInteger.ZERO

fun main() {
    val lines = linesOfDay(2022, 21)

    val monkeys = mutableMapOf<String, CalcMonkey>()
    lines.forEach {
        val input = it.split(":")
        monkeys[input[0]] = CalcMonkey(input[1], monkeys)
    }

    println("Puzzle 1: " + monkeys["root"]!!.calc())

    val root = monkeys["root"]!!
    // human is only part of one branch of root's calculuation
    // calculate the other one and try to find the human value
    // (as human value goes up, the result goes down, so binary search can be used
    //
    // default values and which is the human-less monkey chosen from observation
    println(
        "Puzzle 2: " + searchHumanInput(
            ZERO,
            BigInteger("10000000000000"),
            monkeys[root.m1]!!,
            monkeys["humn"]!!,
            monkeys[root.m2]!!.calc()
        )
    )
}

fun searchHumanInput(
    lower: BigInteger,
    upper: BigInteger,
    monkey: CalcMonkey,
    human: CalcMonkey,
    wanted: BigInteger
): BigInteger {
    human.value = (lower+upper)/2.toBigInteger()
    val value = monkey.calc()

    if ((value - wanted).abs() < 10000.toBigInteger()) {
        do { // check output -> use sensible starting value - actually, better use a binary search here...
            human.value += ONE
        } while (monkey.calc() != wanted)

        return human.value
    }

    return if (value > wanted) {
        searchHumanInput(lower + upper / 2.toBigInteger(), upper, monkey, human, wanted)
    } else {
        searchHumanInput(lower, upper / 2.toBigInteger(), monkey, human, wanted)
    }
}

class CalcMonkey(init: String, private val monkeys: Map<String, CalcMonkey>) {

    var value: BigInteger
    val m1: String
    val m2: String
    var op: MonkeyOp

    init {
        val inputs = init.trim().split(" ")

        if (inputs.size == 3) {
            value = -1.toBigInteger()

            m1 = inputs[0].trim()
            m2 = inputs[2].trim()
            op = MonkeyOp.parse(inputs[1])
        } else {
            value = inputs[0].trim().toBigInteger()

            m1 = ""
            m2 = ""
            op = MonkeyOp.NONE
        }
    }

    fun calc(): BigInteger =
        when (op) {
            MonkeyOp.NONE -> value
            MonkeyOp.ADD -> monkeys[m1]!!.calc() + monkeys[m2]!!.calc()
            MonkeyOp.SUB -> monkeys[m1]!!.calc() - monkeys[m2]!!.calc()
            MonkeyOp.MULT -> monkeys[m1]!!.calc() * monkeys[m2]!!.calc()
            MonkeyOp.DIV -> monkeys[m1]!!.calc() / monkeys[m2]!!.calc()
        }
}

enum class MonkeyOp {
    ADD,
    SUB,
    MULT,
    DIV,
    NONE;

    companion object {
        fun parse(str: String) = when (str.trim()) {
            "+" -> ADD
            "-" -> SUB
            "*" -> MULT
            "/" -> DIV
            else -> throw IllegalArgumentException("Unknown op '$str'.")
        }
    }
}