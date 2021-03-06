package de.mthix.adventofcode

import java.io.File


fun longArrayFromCsvInputForDay(year: Int, day: Int) = textOfDay(year, day).split(',').map { it.toLong() }.toTypedArray()

fun textOfDay(year: Int, day: Int) = fileOfDay(year, day).readText()

fun linesOfDay(year: Int, day: Int) = fileOfDay(year, day).readLines()

fun intsOfDay(year: Int, day: Int) = intsOf(textOfDay(year,day))

fun intsOf(string: String) = string.map { it.toString().toInt() }

fun fileOfDay(year: Int, day: Int) = File(object {}.javaClass.getResource("year$year/input.day$day.txt").file)
