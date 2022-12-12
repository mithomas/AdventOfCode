package de.mthix.adventofcode.year2021

import de.mthix.adventofcode.*


fun main() {
    val map = BaseGrid(BaseGrid.fromUnseparatedIntLines(linesOfDay(2021,15, 0)), { it })
    //println(map.toIntString())
    val target = Coordinates(map.width-1, map.height-1)
    println(map.findAllDistancesByDijkstra(Coordinates(0,0), target)[target]!!.totalDistance)

    val largeMap = BaseGrid(BaseGrid.emptyGrid(map.width*5, map.height*5), { it })
        for(x in 0 until map.width) {
            for(xOffset in 0..4) {
                for(y in 0 until map.height) {
                    for (yOffset in 0..4) {
                        val valueInc = xOffset+yOffset

                        var newValue =  map.get(x,y) + valueInc
                        if(newValue > 9) {
                            newValue -= 9
                        }

                        largeMap.set(x+map.width*xOffset, y+map.height*yOffset, newValue)
                        largeMap.setWeight(x+map.width*xOffset, y+map.height*yOffset, newValue)
                    }
                }
        }
    }
    //println(largeMap.toIntString())
    val largeTarget = Coordinates(largeMap.width-1, largeMap.height-1)
    println(largeMap.findAllDistancesByDijkstra(Coordinates(0,0), largeTarget)[largeTarget]!!.totalDistance)
}