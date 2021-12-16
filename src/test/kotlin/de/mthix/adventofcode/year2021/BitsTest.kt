package de.mthix.adventofcode.year2021

import org.junit.jupiter.api.*
import org.junit.jupiter.params.*
import org.assertj.core.api.Assertions.*

import de.mthix.adventofcode.year2021.BitsPacketType.*
import de.mthix.adventofcode.year2021.BitsPacketLengthTypeId.*
import org.junit.jupiter.params.provider.ValueSource


class OperatorTest {

    @Test
    fun literal() {
        assertThat(BitsPacketType.fromInt(4)).isEqualTo(LITERAL)
    }

    @ParameterizedTest
    @ValueSource(ints = [0,1,3,6])
    fun operator(typeId : Int) {
        assertThat(BitsPacketType.fromInt(typeId)).isNotEqualTo(LITERAL)
    }
}

class BitsPacketTest {

    @Test
    fun example1() {
      val bitsPacket = BitsPacket.buildFromHex("D2FE28")

      assertThat(bitsPacket.bitStream).isEqualTo("110100101111111000101000")
      assertThat(bitsPacket.version).isEqualTo(6)
      assertThat(bitsPacket.type).isEqualTo(LITERAL)
      assertThat(bitsPacket.bitLength).isEqualTo(21)
      assertThat(bitsPacket.value()).isEqualTo(2021)
    }

    @Test
    fun example2() {
        val bitsPacket = BitsPacket.buildFromHex("38006F45291200")

        assertThat(bitsPacket.bitStream).isEqualTo("00111000000000000110111101000101001010010001001000000000")
        assertThat(bitsPacket.version).isEqualTo(1)
        assertThat(bitsPacket.type).isNotEqualTo(LITERAL)
        assertThat(bitsPacket.lengthTypeId).isEqualTo(TOTAL_BIT_LENGTH)
        assertThat(bitsPacket.subpacketLength()).isEqualTo(27)
        assertThat(bitsPacket.subpacketCount()).isEqualTo(2)


        assertThat(bitsPacket.subpackets()[0]!!.bitStream).isEqualTo("110100010100101001000100100")
        assertThat(bitsPacket.subpackets()[1]!!.bitStream).isEqualTo("0101001000100100")
    }

    @Test
    fun example2Sub1() {
        val bitsPacket = BitsPacket("1101000101001010010001001000000000")

        assertThat(bitsPacket.bitStream).isEqualTo("1101000101001010010001001000000000")
        assertThat(bitsPacket.version).isEqualTo(6)
        assertThat(bitsPacket.type).isEqualTo(LITERAL)
        assertThat(bitsPacket.bitLength).isEqualTo(11)
        assertThat(bitsPacket.value()).isEqualTo(10)
    }

    @Test
    fun example2Sub2() {
        val bitsPacket = BitsPacket("01010010001001000000000")

        assertThat(bitsPacket.bitStream).isEqualTo("01010010001001000000000")
        assertThat(bitsPacket.version).isEqualTo(2)
        assertThat(bitsPacket.type).isEqualTo(LITERAL)
        assertThat(bitsPacket.bitLength).isEqualTo(16)
        assertThat(bitsPacket.value()).isEqualTo(20)
    }

    @Test
    fun example3() {
        val bitsPacket = BitsPacket.buildFromHex("EE00D40C823060")

        assertThat(bitsPacket.bitStream).isEqualTo("11101110000000001101010000001100100000100011000001100000")
        assertThat(bitsPacket.version).isEqualTo(7)
        assertThat(bitsPacket.type).isNotEqualTo(LITERAL)
        assertThat(bitsPacket.lengthTypeId).isEqualTo(SUBPACKET_NUMBER)
        //assertThat(bitsPacket.subpacketLength()).isEqualTo(27)
        assertThat(bitsPacket.subpacketCount()).isEqualTo(3)

        assertThat(bitsPacket.subpackets()[0]!!.bitStream).isEqualTo("01010000001100100000100011000001100000")
        assertThat(bitsPacket.subpackets()[1]!!.bitStream).isEqualTo("100100000100011000001100000")
        assertThat(bitsPacket.subpackets()[2]!!.bitStream).isEqualTo("0011000001100000")
    }

    @Test
    fun example3Sub1() {
        val bitsPacket = BitsPacket("01010000001100100000100011000001100000")

        assertThat(bitsPacket.bitStream).isEqualTo("01010000001100100000100011000001100000")
        //assertThat(bitsPacket.version).isEqualTo(6)
        assertThat(bitsPacket.type).isEqualTo(LITERAL)
        assertThat(bitsPacket.bitLength).isEqualTo(11)
        assertThat(bitsPacket.value()).isEqualTo(1)
    }

    @Test
    fun example3Sub2() {
        val bitsPacket = BitsPacket("100100000100011000001100000")

        assertThat(bitsPacket.bitStream).isEqualTo("100100000100011000001100000")
        //assertThat(bitsPacket.version).isEqualTo(6)
        assertThat(bitsPacket.type).isEqualTo(LITERAL)
        assertThat(bitsPacket.bitLength).isEqualTo(11)
        assertThat(bitsPacket.value()).isEqualTo(2)
    }

    @Test
    fun example3Sub3() {
        val bitsPacket = BitsPacket("0011000001100000")

        assertThat(bitsPacket.bitStream).isEqualTo("0011000001100000")
        //assertThat(bitsPacket.version).isEqualTo(6)
        assertThat(bitsPacket.type).isEqualTo(LITERAL)
        assertThat(bitsPacket.bitLength).isEqualTo(11)
        assertThat(bitsPacket.value()).isEqualTo(3)
    }

    @Test
    fun example4() {
        var bitsPacket = BitsPacket.buildFromHex("8A004A801A8002F478")

        assertThat(bitsPacket.version).isEqualTo(4)
        assertThat(bitsPacket.type).isNotEqualTo(LITERAL)
        assertThat(bitsPacket.subpacketCount()).isEqualTo(1)

        bitsPacket = bitsPacket.subpackets()[0]
        assertThat(bitsPacket.version).isEqualTo(1)
        assertThat(bitsPacket.type).isNotEqualTo(LITERAL)
        assertThat(bitsPacket.subpacketCount()).isEqualTo(1)

        bitsPacket = bitsPacket.subpackets()[0]
        assertThat(bitsPacket.version).isEqualTo(5)
        assertThat(bitsPacket.type).isNotEqualTo(LITERAL)
        assertThat(bitsPacket.subpacketCount()).isEqualTo(1)

        bitsPacket = bitsPacket.subpackets()[0]
        assertThat(bitsPacket.version).isEqualTo(6)
        assertThat(bitsPacket.type).isEqualTo(LITERAL)
    }

    @Test
    fun example5() {
        var bitsPacket = BitsPacket.buildFromHex("620080001611562C8802118E34")

        assertThat(bitsPacket.version).isEqualTo(3)
        assertThat(bitsPacket.type).isNotEqualTo(LITERAL)
        assertThat(bitsPacket.subpacketCount()).isEqualTo(2)

       /* bitsPacket = bitsPacket.subpackets()[0]
        assertThat(bitsPacket.version).isEqualTo(1)
        assertThat(bitsPacket.type).isNotEqualTo(LITERAL)
        assertThat(bitsPacket.subpacketCount()).isEqualTo(1)

        bitsPacket = bitsPacket.subpackets()[0]
        assertThat(bitsPacket.version).isEqualTo(5)
        assertThat(bitsPacket.type).isNotEqualTo(LITERAL)
        assertThat(bitsPacket.subpacketCount()).isEqualTo(1)

        bitsPacket = bitsPacket.subpackets()[0]
        assertThat(bitsPacket.version).isEqualTo(6)
        assertThat(bitsPacket.type).isEqualTo(LITERAL)*/
    }
}