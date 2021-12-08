package de.mthix.adventofcode.year2021

import de.mthix.adventofcode.linesOfDay

class Display(signals : String) {

    /**
     *   0000
     *  1    2
     *  1    2
     *   3333
     *  4    5
     *  4    5
     *   666
     */
    val decoder = mutableListOf(mutableSetOf<Char>().toSortedSet())
    val allSignals = signals.filter { it != '|' }.split(" ").filter { it.isNotEmpty() }.map { it.toCharArray().sorted() }
    val outputSignals = signals.split("|")[1].split(" ").filter { it.isNotEmpty() }.map { it.toCharArray().sorted().joinToString("") }

    init {
        // init decoder with empty signal char set
        for(value in 0..8) {
            decoder.add(sortedSetOf())
        }

        // register unique sizes
        allSignals.forEach { registerUnique(it) }

        // 8 is all
        decoder[8].addAll("abcdefg".toCharArray().asList())

        // register common values for non-unique sizes
        val commonSignal5 = allSignals.filter { it.size == 5  }.reduce { common, cur -> common.toMutableList().apply { retainAll(cur) }  }
        decoder[2].addAll(commonSignal5)
        decoder[3].addAll(commonSignal5)
        decoder[5].addAll(commonSignal5)

        val commonSignal6 = allSignals.filter { it.size == 6  }.reduce { common, cur -> common.toMutableList().apply { retainAll(cur) }  }
        decoder[0].addAll(commonSignal6)
        decoder[6].addAll(commonSignal6)
        decoder[9].addAll(commonSignal6)

        // find segment 0
        val seg0 = decoder[7].minus(decoder[1])
        if(seg0.size != 1) throw IllegalArgumentException()
        for(value in listOf(0, 2, 3, 5, 6, 7, 8, 9)) {
            decoder[value].addAll(seg0)
        }

        // find segments 1,3
        val seg13 = decoder[4].minus(decoder[1])
        if(seg13.size != 2) throw IllegalArgumentException()
        for(value in listOf(5, 6, 8, 9)) {
            decoder[value].addAll(seg13)
        }

        // find segments 4,6
        val seg46 = decoder[8].minus(decoder[4]).minus(seg0)
        if(seg46.size != 2) throw IllegalArgumentException()
        for(value in listOf(0, 2, 6, 8)) {
            decoder[value].addAll(seg46)
        }

        // find segment 6
        if(decoder[3].size != 5) throw IllegalArgumentException()
        val seg6 = decoder[3].minus(decoder[4]).minus(decoder[7])
        if(seg6.size != 1) throw IllegalArgumentException()
        for(value in listOf(2,5)) {
            decoder[value].addAll(seg6)
        }

        // find segment 5
        if(decoder[6].size != 6) throw IllegalArgumentException()
        val seg5 = decoder[6].intersect(decoder[1])
        if(seg5.size != 1) throw IllegalArgumentException()
        for(value in listOf(5)) {
            decoder[value].addAll(seg5)
        }

        // find segments 2,4
        if(decoder[5].size != 5) throw IllegalArgumentException()
        val seg24 = decoder[8].minus(decoder[5])
        if(seg24.size != 2) throw IllegalArgumentException()
        for(value in listOf(2)) {
            decoder[value].addAll(seg24)
        }

        //println(decoder.mapIndexed { i,v -> i to v }.toMap())
        println(decoder.mapIndexed { i,v -> i to v.size }.toMap())
        println(decoder.mapIndexed { i,v -> v.joinToString("") to i }.toMap())
    }

    private fun registerUnique(signal : List<Char>) {
        when(signal.size) {
            2 -> { // 1
                decoder[0].addAll(signal)
                decoder[1].addAll(signal)
                decoder[3].addAll(signal)
                decoder[4].addAll(signal)
                decoder[7].addAll(signal)
                decoder[9].addAll(signal)
            }
            3 -> { // 7
                decoder[0].addAll(signal)
                decoder[3].addAll(signal)
                decoder[7].addAll(signal)
                decoder[9].addAll(signal)
            }
            4 -> { // 4
                decoder[4].addAll(signal)
                decoder[9].addAll(signal)
            }
        }
    }

    fun output() : Int {
        val decoderRev = decoder.mapIndexed { i,v -> v.joinToString("") to i }.toMap()
        return outputSignals.map { decoderRev[it] }.joinToString("").toInt()
    }
}

fun main() {
    // 0: input / 1: output
    val signals = linesOfDay(2021, 8)

    val simpleOutputDigits = signals
        .map { it.split("|") }
        .map { it[1].trim().split(" ") }
        .flatten()
        .filter{ it.length in listOf(2, 4, 3, 7) } // numbers 1, 4, 7, 8

    println("Simple output value: ${simpleOutputDigits.size}")

    println(signals.map { Display(it).output() }.sum())
}