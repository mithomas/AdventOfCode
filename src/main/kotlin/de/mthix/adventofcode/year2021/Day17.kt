package de.mthix.adventofcode.year2021

import de.mthix.adventofcode.Coordinates
import kotlin.math.sign

fun main() {
    val initialPosition = Coordinates(0,0)

    // examples: target area: x=20..30, y=-10..-5 ==> 45 maxY
    //val targetX = 20..30
    //val targetY = -10..-5

    //println(shoot(initialPosition, Coordinates(7,2), targetX, targetY))
    //println(shoot(initialPosition, Coordinates(6,3), targetX, targetY))
    //println(shoot(initialPosition, Coordinates(9,0), targetX, targetY))
    //println(shoot(initialPosition, Coordinates(17,4), targetX, targetY))

    // actual input: target area: x=144..178, y=-100..-76
    val targetX = 144..178
    val targetY = -100..-76

    val successfulPaths = mutableListOf<List<Coordinates>>()
    for(velX in 0..targetX.last) {
        for(velY in -100..100) {
            val velocity = Coordinates(velX, velY)
            val path = shoot(initialPosition, velocity, targetX, targetY)
            if(path.second) {
                successfulPaths.add(path.first)
                //println(velocity)
            }
        }
    }

    println("highest position: " + successfulPaths
        .map { path -> path.map { position -> position.y }.max()!! }
        .max()
    )
    println("number of successful paths: " + successfulPaths.size)
}

/**
 * @return <path to final position, hit or miss>
 */
fun shoot(initialPosition : Coordinates, initialVelocity : Coordinates, targetX : IntRange, targetY : IntRange) : Pair<List<Coordinates>, Boolean> {
    //print("Searching path from $initialPosition with $initialVelocity to [$targetX:$targetY]")

    val positions = mutableListOf(initialPosition)
    var hitTargetArea = false

    var position = initialPosition
    var velocity = initialVelocity

    while (position.x <= targetX.last && position.y >= targetY.first) {
        position = Coordinates(position.x+velocity.x, position.y+velocity.y)
        velocity = Coordinates(velocity.x-sign((velocity.x).toDouble()).toInt(), velocity.y-1)

        positions.add(position)
        hitTargetArea = hitTargetArea || targetX.contains(position.x) && targetY.contains(position.y)
    }

    //println(": $hitTargetArea")
    return Pair(positions, hitTargetArea)
}