package de.mthix.adventofcode.year2022

import de.mthix.adventofcode.linesOfDay

fun main() {
    val lines = linesOfDay(2022, 7).map { it.split(" ") }.toMutableList()
    lines.removeFirst()
    println(lines)

    val root = AoCDir("/", null)
    val dirs = listOf(root).toMutableList()
    var current = root
    lines.forEach {
        if (it[0][0].isDigit()) {
            current.entries.add(AoCFile(it[0].toInt(), it[1], current))
        } else if(it[1] == "cd" && it[2] == "..")  {
            current = current.parent!!
        } else if(it[1] == "cd") {
            val child = AoCDir(it[2], current)
            current.entries.add(child)
            dirs.add(child)
            current = child
        }
    }

    val totalDiskSpace = 70000000
    val currentDiskSpace = totalDiskSpace-root.getSize()
    val requiredDiskSpace = 30000000

    println("Puzzle 1: " + dirs.map { it.getSize() }.filter { it <= 100000 }.sum())
    println("Puzzle 2: " + dirs.map { it.getSize() }.filter { currentDiskSpace+it >= requiredDiskSpace }.min())
}

abstract class FilesystemEntry(val name: String, val parent: AoCDir?) {
    abstract fun getSize(): Int
}

class AoCDir(name: String, parent: AoCDir?) : FilesystemEntry(name, parent) {

    val entries = emptyList<FilesystemEntry>().toMutableList()
    override fun getSize(): Int = entries.sumOf { it.getSize() }
}

class AoCFile(val s: Int, name: String, parent: AoCDir) : FilesystemEntry(name, parent) {
    override fun getSize(): Int = s
}