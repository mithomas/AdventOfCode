package de.mthix.adventofcode.year2021

import java.math.BigInteger

class DeterministicDice {

    val sides = 100
    var rolls = 0
    var nextValue = 1

    fun roll() : Int {
        val result = nextValue++
        if(nextValue > sides) { nextValue = 1 }
        rolls++

        return result
    }

    override fun toString(): String {
        return "$nextValue : $rolls"
    }
}

class Player(position : Int) {

    var zeroBasedPosition = position-1
    var score = BigInteger.ZERO

    fun move(dice : DeterministicDice) {
        var move = 0
        for(i in 1..3) { move += dice.roll() }

        //println("move by $move from ${zeroBasedPosition+1}")

        zeroBasedPosition += move
        zeroBasedPosition %= 10
        score += (zeroBasedPosition+1).toBigInteger()
    }

    override fun toString(): String {
        return "@${zeroBasedPosition+1} ($score pts)"
    }
}

fun main() {
    // example: listOf(Player(4), Player(8))
    // actual:  listOf(Player(7), Player(3))

    practiceGame(listOf(Player(7), Player(3)))
    realGame(listOf(Player(7), Player(3)))

    var j = BigInteger.ZERO
    for (i in 0 .. 444356092776315) {
        j = BigInteger.ONE + i.toBigInteger()
    }
    println(j)
}

fun practiceGame(players : List<Player>) {
    val dice = DeterministicDice()

    var currentPlayerIndex = 0
    while(players.map { it.score }.all { it < BigInteger.valueOf(1000) }) {
        val currentPlayer = players[currentPlayerIndex]
        currentPlayer.move(dice)
        println("Player ${currentPlayerIndex+1}: $currentPlayer")

        currentPlayerIndex = if(currentPlayerIndex == 0) 1 else 0
    }

    println(dice.rolls.toBigInteger() * players.map { it.score }.min()!!)
}

fun realGame(players: List<Player>) {
    println(players)
}