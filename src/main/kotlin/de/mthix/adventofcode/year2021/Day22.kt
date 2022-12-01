package de.mthix.adventofcode.year2021

import de.mthix.adventofcode.linesOfDay

data class Cuboid(val x : Int, val y : Int, val z : Int, var state : Boolean = false)

class RebootStep(commandLine : String) {
    val x : IntRange
    val y : IntRange
    val z : IntRange
    val command : Boolean
    val cuboidCount : Int

    init {
        val parts = commandLine.split(" ")
        command = parts[0] == "on"

        val ranges = parts[1].split(",")

        val xRange = ranges[0].split("=")[1].split("..").map { it.toInt() }
        x = IntRange(xRange[0], xRange[1])

        val yRange = ranges[1].split("=")[1].split("..").map { it.toInt() }
        y = IntRange(yRange[0], yRange[1])

        val zRange = ranges[2].split("=")[1].split("..").map { it.toInt() }
        z = IntRange(zRange[0], zRange[1])

        cuboidCount = x.count() * y.count() * z.count()
    }

    override fun toString(): String {
        return "$command x=$x,y=$y,z=$z ($cuboidCount)"
    }
}

fun main() {
    val commands = linesOfDay(2021,22,2).map { RebootStep(it) }

    // find dimensions
    val xDim = IntRange(commands.map { it.x.first }.min()!!, commands.map { it.x.last }.max()!!)
    val yDim = IntRange(commands.map { it.y.first }.min()!!, commands.map { it.y.last }.max()!!)
    val zDim = IntRange(commands.map { it.z.first }.min()!!, commands.map { it.z.last }.max()!!)
    println("Dimensions: $xDim,$yDim,$zDim")
    println(xDim.count().toBigInteger()*yDim.count().toBigInteger()*zDim.count().toBigInteger())

    // naive approach - puzzle 1
    // init cuboids
    val size = -50..50
    val count = (size.last-size.first)*(size.last-size.first)*(size.last-size.first)
    val cuboids = ArrayList<Cuboid>(count)
    println("Having $count cuboids.")

    for(x in size) {
        for(y in size) {
            for(z in size) {
                cuboids.add(Cuboid(x,y,z,false))
            }
        }
    }

    // run commands
    println(commands)
    commands.forEach { cmd ->
        cuboids.filter { it.x in cmd.x && it.y in cmd.y && it.z in cmd.z }.forEach { it.state = cmd.command }
    }

    println(cuboids.filter { it.state }.size)


    // better approach - puzzle 2 > reduce
    commands.forEachIndexed { i,cmd ->

    }
}