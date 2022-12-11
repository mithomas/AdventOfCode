package de.mthix.adventofcode

import java.io.File


fun longArrayFromCsvInputForDay(year: Int, day: Int) = textOfDay(year, day).split(',').map { it.toLong() }.toTypedArray()

fun textOfDay(year: Int, day: Int, useExample: Int = 0) = fileOfDay(year, day, useExample).readText()

fun linesOfDay(year: Int, day: Int, useExample: Int = 0) = fileOfDay(year, day, useExample).readLines()

fun intStreamOfDay(year: Int, day: Int) = intsOf(textOfDay(year,day))

fun intsOf(string: String) = string.map { it.toString().toInt() }

fun fileOfDay(year: Int, day: Int, useExample : Int = 0) : File {
    val name = if(useExample == 0) "year$year/input.day$day.txt" else "year$year/example$useExample.day$day.txt"
    return File(object {}.javaClass.getResource(name).file)
}

fun intsOfDay(year: Int, day: Int) = textOfDay(year, day).split(",").map { it.toInt() }