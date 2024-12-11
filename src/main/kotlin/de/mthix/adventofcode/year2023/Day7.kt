package de.mthix.adventofcode.year2023

import de.mthix.adventofcode.linesOfDay
import java.lang.IllegalStateException

open class Hand(val hand: String, val bet: Int) : Comparable<Hand> {

    val cards = hand.groupingBy { it }.eachCount()

    override fun compareTo(other: Hand): Int {
        var diff = other.getHandOrder() - getHandOrder()

        // hand
        if (diff != 0) return diff

        // high card
        for (i in 0..hand.length - 1) {
            diff = getCardOrder()[other.hand[i]]!! - getCardOrder()[hand[i]]!!
            if (diff != 0) return diff
        }

        return 0
    }

    open fun getCardOrder() = mapOf(
        'A' to 1,
        'K' to 2,
        'Q' to 3,
        'J' to 4,
        'T' to 5,
        '9' to 6,
        '8' to 7,
        '7' to 8,
        '6' to 9,
        '5' to 10,
        '4' to 11,
        '3' to 12,
        '2' to 13
    )

    fun getHandOrder(): Int {
        if (isFiveOfAKind()) return 1
        if (isFourOfAKind()) return 2
        if (isFullHouse()) return 3
        if (isThreeOfAKind()) return 4
        if (isTwoPair()) return 5
        if (isOnePair()) return 6

        return 7
    }

    open fun isFiveOfAKind() = cards.size == 1
    open fun isFourOfAKind() = cards.any { it.value == 4 }
    open fun isFullHouse() = cards.size == 2 && cards.any { it.value == 3 }
    open fun isThreeOfAKind() = cards.size == 3 && cards.any { it.value == 3 }
    open fun isTwoPair() = cards.filter { it.value == 2 }.size == 2
    open fun isOnePair() = cards.size == 4

    override fun toString() = hand
}

class JokerHand(hand: String, bet: Int) : Hand(hand, bet) {

    val nonJokerCards = cards.filter { it.key != 'J' }
    override fun getCardOrder() = mapOf(
        'A' to 1,
        'K' to 2,
        'Q' to 3,
        'T' to 5,
        '9' to 6,
        '8' to 7,
        '7' to 8,
        '6' to 9,
        '5' to 10,
        '4' to 11,
        '3' to 12,
        '2' to 13,
        'J' to 14,
    )

    private fun getJokerCount() = cards.getOrDefault('J', 0)

    override fun isFiveOfAKind(): Boolean {
        return try {
            nonJokerCards.maxOf { it.value }
        } catch (e: NoSuchElementException) {
            0
        } + getJokerCount() == 5
    }

    override fun isFourOfAKind(): Boolean {
        return nonJokerCards.maxOf { it.value } + getJokerCount() == 4
    }

    override fun isFullHouse(): Boolean {
        return nonJokerCards.size == 2 && nonJokerCards.map { it.value }.sum() + getJokerCount() == 5
    }

    override fun isThreeOfAKind(): Boolean {
        return nonJokerCards.maxOf { it.value } + getJokerCount() == 3
    }

    override fun isTwoPair(): Boolean {
        return nonJokerCards.filter { it.value == 2 }.size + getJokerCount() == 2
    }

    override fun isOnePair(): Boolean {
        return nonJokerCards.maxOf { it.value } + getJokerCount() == 2
    }
}

fun main() {
    val linesOfDay = linesOfDay(2023, 7).map { it.split(" ") }

    printSumBets(linesOfDay.map { Hand(it[0], it[1].toInt()) }.sorted())
    printSumBets(linesOfDay.map { JokerHand(it[0], it[1].toInt()) }.sorted())
}

fun printSumBets(hands: List<Hand>) {
    println(hands.mapIndexed { i, hand -> (i + 1) * hand.bet }.sum())
}
