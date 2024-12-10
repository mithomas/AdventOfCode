package de.mthix.adventofcode.year2024

import de.mthix.adventofcode.textOfDay

const val EMPTY = "."

fun main() {
    val filesystem = textOfDay(2024, 9).mapIndexed { i,l -> List(l.digitToInt()) { if (i % 2 == 0) (i/2).toString() else EMPTY } }.filter { it.isNotEmpty() }


    val blockMoveFileSystem = filesystem.flatten().toMutableList()

    var nextFree = blockMoveFileSystem.indexOfFirst { isEmptyBlock(it) }
    var nextFile = blockMoveFileSystem.indexOfLast { !isEmptyBlock(it) }

    while(nextFree < nextFile) {
        blockMoveFileSystem[nextFree] = blockMoveFileSystem[nextFile]
        blockMoveFileSystem[nextFile] = EMPTY

        nextFree = blockMoveFileSystem.indexOfFirst { isEmptyBlock(it) }
        nextFile = blockMoveFileSystem.subList(0,nextFile).indexOfLast { !isEmptyBlock(it) }
    }

    println(calcId(blockMoveFileSystem))


    val fileMoveFileSystem = filesystem.toMutableList()

    var nextId = fileMoveFileSystem.filter { !isEmptySpace(it) }.maxOf { it[0].toInt() }

    while(nextId >= 0) {
        nextFile = indexOfFile(fileMoveFileSystem, nextId)
        val filesize = fileMoveFileSystem[nextFile].size

        for(i in 0..nextFile) {
            if(isEmptySpace(fileMoveFileSystem[i])) {
                val freespacesize = fileMoveFileSystem[i].size

                if (freespacesize >= filesize) {
                    fileMoveFileSystem[i] = fileMoveFileSystem[nextFile]
                    fileMoveFileSystem[nextFile] = List(filesize) { EMPTY }

                    if (freespacesize > filesize) {
                        fileMoveFileSystem.add(i + 1, List(freespacesize - filesize) { EMPTY })
                    }

                    break;
                }
            }
        }

        nextId--
    }

    println(calcId(fileMoveFileSystem.flatten()))
}

private fun indexOfFile(fileMoveFileSystem: MutableList<List<String>>, fileId: Int) = fileMoveFileSystem.indexOfFirst { it[0] == fileId.toString() }

private fun isEmptyBlock(block: String) = (block == EMPTY)
private fun isEmptySpace(space: List<String>) = isEmptyBlock(space[0])

private fun calcId(filesystem:List<String>) = filesystem
    .mapIndexed { i,id -> (if(id != EMPTY) id else "0").toBigInteger() * i.toBigInteger() }
    .reduce { acc, it -> acc + it }