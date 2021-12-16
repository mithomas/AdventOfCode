package de.mthix.adventofcode.year2021

import de.mthix.adventofcode.year2021.BitsPacketLengthTypeId.*
import de.mthix.adventofcode.year2021.BitsPacketType.*
import java.lang.Integer.*
import java.lang.IllegalStateException
import java.math.BigInteger
import java.math.BigInteger.*

enum class BitsPacketType {
    LITERAL,
    PRODUCT,
    SUM,
    MINIMUM,
    MAXIMUM,
    GREATER_THEN,
    LESS_THAN,
    EQUAL;

    companion object {
        fun fromInt(id : Int): BitsPacketType {
            return when(id) {
                0 -> SUM
                1 -> PRODUCT
                2 -> MINIMUM
                3 -> MAXIMUM
                4 -> LITERAL
                5 -> GREATER_THEN
                6 -> LESS_THAN
                7 -> EQUAL
                else -> throw IllegalArgumentException("Wrong operator: $id")
            }
        }
    }
}

enum class BitsPacketLengthTypeId(val id : Int) {
    NONE(-1),
    TOTAL_BIT_LENGTH(0),
    SUBPACKET_NUMBER(1);

    companion object {

        fun fromInt(id : Int): BitsPacketLengthTypeId {
            return when(id) {
                0 -> TOTAL_BIT_LENGTH
                1 -> SUBPACKET_NUMBER
                else -> throw IllegalArgumentException("unknown id: $id")
            }
        }
    }
}

class BitsPacket(val bitStream : String) {

    val version : Int
    val type : BitsPacketType
    val bitLength : Int

    val lengthTypeId : BitsPacketLengthTypeId

    private val value : BigInteger
    private val subpacketLength : Int
    private val subpacketCount : Int
    private val subpackets = mutableListOf<BitsPacket>()

    init {
        //print("Creating with bitstream: $bitStream")
        version = bitStream.substring(0,3).toInt(2)
        type = BitsPacketType.fromInt(bitStream.substring(3,6).toInt(2))
        //println(" with type $type")

        if(type == LITERAL) {
            lengthTypeId = NONE
            subpacketLength = -1
            subpacketCount = -1

            val valueChunks = bitStream
                .substring(6)
                .chunked(5)

            val chunkCount = valueChunks.indexOfFirst { it.startsWith("0") } + 1

            value = valueChunks
                .take(chunkCount)
                .map { it.substring(1) }
                .joinToString(separator = "")
                .toBigInteger(2)

            bitLength = 3 + 3 + chunkCount*5 // version + type + chunks
        } else {
            value = BigInteger.valueOf(-1)

            lengthTypeId = BitsPacketLengthTypeId.fromInt(bitStream[6].toString().toInt())

            when(lengthTypeId) {
                TOTAL_BIT_LENGTH -> {
                    subpacketLength = bitStream.substring(7, 7 + 15).toInt(2)
                    var subpacketBitStream = bitStream.substring(7 + 15, 7 + 15 + subpacketLength)

                    //println("Parsing $subpacketLength bits worth of subpackets...")
                    while (subpacketBitStream.replace("0", "").isNotEmpty()) {
                        //println("Next bitstream: $subpacketBitStream")
                        val nextSubPacket = BitsPacket(subpacketBitStream)
                        subpackets.add(nextSubPacket)
                        subpacketBitStream = subpacketBitStream.substring(nextSubPacket.bitLength)
                    }
                    //println("Done.")

                    subpacketCount = subpackets.size
                }
                SUBPACKET_NUMBER -> {
                    var subpacketBitStream = bitStream.substring(7 + 11)
                    subpacketCount = bitStream.substring(7, 7 + 11).toInt(2)

                    //println("Parsing $subpacketCount subpackets...")
                    for (i in 0 until subpacketCount) {
                        val nextSubPacket = BitsPacket(subpacketBitStream)
                        subpackets.add(nextSubPacket)
                        subpacketBitStream = subpacketBitStream.substring(nextSubPacket.bitLength)
                    }
                    //println("Done.")

                    subpacketLength = subpackets.map { it.bitLength }.sum()
                }
                NONE -> throw IllegalStateException("Not an operator!")
            }

            bitLength = 3 + 3 + 1 + (if (lengthTypeId==TOTAL_BIT_LENGTH) 15 else 11) + subpackets.map { it.bitLength }.sum()// version + type + lengthtypeid + length + subpackets
        }
    }

    fun calculate() : BigInteger {
        return when(type) {
            SUM -> subpackets.map { it.calculate() }.reduce { acc, bigInteger -> acc + bigInteger }
            PRODUCT -> subpackets.map { it.calculate() }.reduce { acc, bigInteger -> acc * bigInteger }
            MINIMUM -> subpackets.map { it.calculate() }.min()!!
            MAXIMUM -> subpackets.map { it.calculate() }.max()!!
            LITERAL -> value
            GREATER_THEN -> if (subpackets[0]!!.calculate() > subpackets[1]!!.calculate()) ONE else ZERO
            LESS_THAN -> if (subpackets[0]!!.calculate() < subpackets[1]!!.calculate()) ONE else ZERO
            EQUAL -> if (subpackets[0]!!.calculate() == subpackets[1]!!.calculate()) ONE else ZERO
        }
    }

    fun subpackets() : List<BitsPacket> {
        if (type == LITERAL) { throw IllegalStateException("Not an operator!") }
        return subpackets
    }

    fun subpacketCount() : Int {
        if (type == LITERAL) { throw IllegalStateException("Not an operator!") }
        return subpacketCount
    }

    fun subpacketLength() : Int {
        if (type == LITERAL) { throw IllegalStateException("Not an operator!") }
        return subpacketLength
    }

    fun value() : BigInteger {
        if (type != LITERAL) { throw IllegalStateException("Not a literal!") }
        return value
    }


    override fun toString(): String {
        val result = "v$version:$type"

        return when(type) {
            LITERAL -> "$result:value=$value"
            else -> result
        }
    }


    companion object {

        fun buildFromHex(hexInput : String) : BitsPacket {
            return BitsPacket(hexInput
                .map { toBinaryString(parseInt(it.toString(), 16)).padStart(4, '0') }
                .joinToString(separator = ""))
        }
    }
}
