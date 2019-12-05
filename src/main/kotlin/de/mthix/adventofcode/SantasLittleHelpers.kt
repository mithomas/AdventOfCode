package de.mthix.adventofcode

import java.io.File

fun readInput(file: File):Array<Int> {
    return file.readText().split(',').map { it.toInt() }.toTypedArray()
}