package de.mthix.adventofcode.year2018

import java.io.File

/**
```
--- Day 3: No Matter How You Slice It ---

The Elves managed to locate the chimney-squeeze prototype fabric for Santa's suit (thanks to someone who helpfully wrote its box IDs on the wall of the warehouse in the middle of the night). Unfortunately, anomalies are still affecting them - nobody can even agree on how to cut the fabric.

The whole piece of fabric they're working on is a very large square - at least 1000 inches on each side.

Each Elf has made a claim about which area of fabric would be ideal for Santa's suit. All claims have an ID and consist of a single rectangle with edges parallel to the edges of the fabric. Each claim's rectangle is defined as follows:

The number of inches between the left edge of the fabric and the left edge of the rectangle.
The number of inches between the top edge of the fabric and the top edge of the rectangle.
The width of the rectangle in inches.
The height of the rectangle in inches.

A claim like #123 @ 3,2: 5x4 means that claim ID 123 specifies a rectangle 3 inches from the left edge, 2 inches from the top edge, 5 inches wide, and 4 inches tall. Visually, it claims the square inches of fabric represented by # (and ignores the square inches of fabric represented by .) in the diagram below:

...........
...........
...#####...
...#####...
...#####...
...#####...
...........
...........
...........

The problem is that many of the claims overlap, causing two or more claims to cover part of the same areas. For example, consider the following claims:

#1 @ 1,3: 4x4
#2 @ 3,1: 4x4
#3 @ 5,5: 2x2

Visually, these claim the following areas:

........
...2222.
...2222.
.11XX22.
.11XX22.
.111133.
.111133.
........

The four square inches marked with X are claimed by both 1 and 2. (Claim 3, while adjacent to the others, does not overlap either of them.)

If the Elves all proceed with their own plans, none of them will have enough fabric. How many square inches of fabric are within two or more claims?

--- Part Two ---

Amidst the chaos, you notice that exactly one claim doesn't overlap by even a single square inch of fabric with any other claim. If you can somehow draw attention to it, maybe the Elves will be able to make Santa's suit after all!

For example, in the claims above, only claim 3 is intact after all claims are made.

What is the ID of the only claim that doesn't overlap?
```
 *
 * See also [https://adventofcode.com/2018/day/3].
 */

fun main(args: Array<String>) {
    val file = File(object {}.javaClass.getResource("input.day3.txt").file)
    val calculator = FabricClaimCalculator(file.readLines().map { Claim.fromInputLine(it) })


    println("Solution for step 1: ${calculator.calculateClaimOverlay()}")
    println("Solution for step 2: ${calculator.findNonOverlappingClaimId()}")
}

data class Claim(val id:Int, val x:Int, val y:Int, val width:Int, val height:Int) {

    val endX = x + width-1
    val endY = y + height-1
    val size = width * height

    var overlayed = false

    companion object {

        /** #1 @ 82,901: 26x12 */
        val PATTERN = "#([0-9]+) @ ([0-9]+),([0-9]+): ([0-9]+)x([0-9]+)".toRegex()

        fun fromInputLine(line: String): Claim {
            val (id, x, y, width, height) = PATTERN.find(line)!!.destructured
            return Claim(id.toInt(), x.toInt(), y.toInt(), width.toInt(), height.toInt())
        }
    }
}

class FabricClaimCalculator(val claims: List<Claim>) {

    /** two-dimensional grad with all claim ids */
    var fabric = HashMap<Pair<Int, Int>, HashSet<Claim>>()

    init {
        // build overlays
        claims.forEach { claim ->
            for(i in claim.x..claim.endX) {
                for(j in claim.y..claim.endY) {
                    fabric.getOrPut(Pair(i,j), { HashSet<Claim>() }).add(claim)
                }
            }
        }

        // mark all claims as overlayed
        fabric.filter { it.value.size > 1 }.values.flatten().distinct().forEach { it.overlayed = true }
    }

    fun calculateClaimOverlay(): Int {
        return fabric.filter { it.value.size > 1 }.size
    }

    fun findNonOverlappingClaimId(): Int {
        val nonOverlappingClaims = claims.filter { !it.overlayed }.distinct()
        assert(nonOverlappingClaims.size == 1)
        return nonOverlappingClaims.get(0).id
    }
}