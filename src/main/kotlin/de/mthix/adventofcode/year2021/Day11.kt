package de.mthix.adventofcode.year2021

import de.mthix.adventofcode.BaseGrid
import de.mthix.adventofcode.GridNode
import de.mthix.adventofcode.linesOfDay

class Octopus(var energy : Int) {

    var hasFlashedInStep = false

    fun incEnergy() {
        energy++
    }

    fun flashes() : Boolean {
        val doFlash = !hasFlashedInStep && energy > 9
        hasFlashedInStep = energy > 9
        return doFlash
    }

    fun completeStep() {
        if(hasFlashedInStep) {
            energy = 0
        }

        hasFlashedInStep = false
    }

    override fun toString() : String {
        return "$energy${if (hasFlashedInStep) '!' else ' '}"
    }
}

class OctopusGrid(octopusEnergyLevels : List<List<Int>>) : BaseGrid<Octopus>(octopusEnergyLevels, { Octopus(it) }) {

    var allFlashes = 0

    fun step() : Boolean {
        var allHaveFlashed = false

        nodeList.forEach { it.value.incEnergy() }

        var flashingNodes = listOf<GridNode<Octopus>>()
        do {
            flashingNodes = nodeList.filter { it.value.flashes() }
            allFlashes += flashingNodes.size

            flashingNodes.map { it.getNeighbors(true) }.flatten().forEach { it.value.incEnergy() }
        } while (flashingNodes.isNotEmpty())

        allHaveFlashed = nodeList.all { it.value.hasFlashedInStep }
        nodeList.forEach { it.value.completeStep() }

        return allHaveFlashed
    }
}

fun main() {
    val octupi = OctopusGrid(BaseGrid.fromUnseparatedIntLines(linesOfDay(2021, 11, 0)))

    var step = 1

    while (!octupi.step()) {
        if(step++ == 100) println("after step 100 => ${octupi.allFlashes}\n\n")
    }

    println("simultaneously: $step")
}