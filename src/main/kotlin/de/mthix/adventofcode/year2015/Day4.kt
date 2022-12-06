package de.mthix.adventofcode.year2015

import java.math.BigInteger
import java.security.MessageDigest

fun main() {
    val key = "bgvyzdsv"

    println("Puzzle 1: " + findMD5WithStartString(key, "00000"))
    println("Puzzle 2: " + findMD5WithStartString(key, "000000"))
}

fun findMD5WithStartString(key: String, start: String): Int {
    var i = 0
    var md5: String
    do {
        md5 = md5("$key$i")
        i += 1
    } while (!md5.startsWith(start))

    return i-1
}

fun md5(input: String): String {
    val md = MessageDigest.getInstance("MD5")
    return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
}
