package de.mthix.adventofcode.year2023

import de.mthix.adventofcode.linesOfDay


val wordNumbers = mapOf(
    "one" to "1",
    "two" to "2",
    "three" to "3",
    "four" to "4",
    "five" to "5",
    "six" to "6",
    "seven" to "7",
    "eight" to "8",
    "nine" to "9")

fun main() {
    val linesOfDay = linesOfDay(2023, 1)

    println(sumCalibrationValues(linesOfDay))
    println("")
    linesOfDay.forEach {
        println(it)
        println(substituteWordNumbers(it))
        println(substituteWordNumbers(it).filter { it.isDigit() })
        println("") }
    println(sumCalibrationValues(linesOfDay.map { substituteWordNumbers(it) }))
}

fun sumCalibrationValues(input: List<String>): Int {
    return input
        .map{ it.filter { it.isDigit() } }
        .filter { it.isNotEmpty() }
        .map { it.first().toString() + it.last().toString() }
        .sumOf { it.toInt() }
}

fun substituteWordNumbers(s: String): String {
    if(s.isEmpty()) return ""

    var numberWord = ""
    for(k in wordNumbers.keys) {
        if(s.startsWith(k)) numberWord = k
    }

    return if(numberWord.isNotEmpty()) {
        wordNumbers[numberWord] + substituteWordNumbers(s.substring(1))
    } else {
        s.substring(0,1) + substituteWordNumbers(s.substring(1))
    }
}