package de.mthix.adventofcode.year2024

import de.mthix.adventofcode.Coordinates
import de.mthix.adventofcode.linesOfDay

fun main() {
    val map = linesOfDay(2024, 8)


    println(calcAntinodes(map).size)
    println(calcAntinodes(map,true).size)
}

fun calcAntinodes(map:List<String>, harmonic:Boolean = false): Set<Coordinates> {
    val antennas = HashMap<Char,MutableList<Coordinates>>()

    for(y in map.indices)  {
        for(x in map[0].indices) {
            val position = map[y][x]
            if(position != '.') antennas.getOrPut(position) { ArrayList() }.add(Coordinates(x,y))
        }
    }
    println(antennas)

    return antennas
        .mapValues { it.value.map { e1 -> it.value.map { e2 -> e1 to e2 } }.flatten().map { it.toList().toSet() }.toSet().filter { it.size == 2 } }
        .mapValues { it.value.map { pair ->
            val xDiff = pair.last().x - pair.first().x
            val yDiff = pair.last().y - pair.first().y

            val antinodes = HashSet<Coordinates>()

            if(harmonic) {
                var curUp = pair.first()
                do {
                    antinodes.add(curUp)
                    curUp = up(curUp, xDiff, yDiff)
                } while (isInMap(curUp, map))

                var curDown = pair.last()
                do {
                    antinodes.add(curDown)
                    curDown = down(curDown, xDiff, yDiff)
                } while (isInMap(curDown, map))
            } else {
                antinodes.addAll(setOf(
                    Coordinates(pair.first().x-xDiff, pair.first().y-yDiff),
                    Coordinates(pair.last().x+xDiff, pair.last().y+yDiff)
                ) )
            }

            antinodes.filter { isInMap(it,map) }
        } }
        .map { it.value.flatten() }
        .flatten().toSet()
}

fun up(c:Coordinates, xDiff:Int, yDiff:Int) = Coordinates(c.x-xDiff, c.y-yDiff)
fun down(c:Coordinates, xDiff:Int, yDiff:Int) = Coordinates(c.x+xDiff, c.y+yDiff)

fun isInMap(c:Coordinates, map:List<String>) = c.x >= 0 && c.x < map[0].length && c.y >= 0 && c.y < map.size
