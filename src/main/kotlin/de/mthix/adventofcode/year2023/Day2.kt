package de.mthix.adventofcode.year2023

import de.mthix.adventofcode.linesOfDay


fun main() {
    val linesOfDay = linesOfDay(2023, 2)

    val cubes = mapOf("red" to 12, "green" to 13, "blue" to 14)

    val games = linesOfDay
        .map { line -> line.split(": ") }
        .associate { game -> game[0].substring(5).toInt() to toCubeMaps(game[1]) }

    println(games
        .filter { (id, game) -> game
            .all { draw -> draw
                .all{ (!draw.containsKey("red") || draw["red"]!! <= cubes["red"]!!)
                        && (!draw.containsKey("blue") || draw["blue"]!! <= cubes["blue"]!!)
                        && (!draw.containsKey("green") || draw["green"]!! <= cubes["green"]!!)}}}
        .keys
        .sum()
        )

    println(games.values.sumOf { game -> toSingleCubeList(game).reduce { acc, i -> acc * i } })
}

fun toCubeMaps(game: String) = game.split("; ").map { toCubeMap(it) }
fun toCubeMap(draw: String) = draw.split(", ").associate { it.split(" ")[1] to it.split(" ")[0].toInt() }.withDefault { 0 }

fun toSingleCubeList(game: List<Map<String,Int>>): List<Int> {
    val cubes = mutableMapOf("red" to 0, "green" to 0, "blue" to 0)

    game.forEach { draw ->
        if(draw.containsKey("red") && draw["red"]!! > cubes["red"]!!) cubes["red"] = draw["red"]!!
        if(draw.containsKey("blue") && draw["blue"]!! > cubes["blue"]!!) cubes["blue"] = draw["blue"]!!
        if(draw.containsKey("green") && draw["green"]!! > cubes["green"]!!) cubes["green"] = draw["green"]!!
    }

    return cubes.values.toList()
}