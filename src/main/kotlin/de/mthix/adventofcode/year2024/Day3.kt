package de.mthix.adventofcode.year2024

import com.marcinmoskala.math.product
import de.mthix.adventofcode.textOfDay

fun main() {
    val mulRe = "mul\\((\\d+),(\\d+)\\)|do\\(\\)|don't\\(\\)".toRegex()
    val instructions = mulRe.findAll(textOfDay(2024, 3))

    var multiplicationSum = 0L
    var enabledMultiplicationSum = 0L
    var enabled = true

    for (instruction in instructions) {
        when(instruction.value) {
            "do()" -> enabled = true
            "don't()" -> enabled = false
            else -> {
                val product = instruction.groupValues.subList(1,3).map { it.toInt() }.product()

                multiplicationSum += product
                if(enabled) { enabledMultiplicationSum += product }
            }
        }
    }

    println("multiplications: $multiplicationSum")
    println("enabled multiplications: $enabledMultiplicationSum")
}
