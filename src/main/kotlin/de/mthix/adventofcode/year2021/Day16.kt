package de.mthix.adventofcode.year2021

import de.mthix.adventofcode.textOfDay

fun main() {

    output("8A004A801A8002F478")
    output("620080001611562C8802118E34")
    output("C0015000016115A2E0802F182340")
    output("A0016C880162017C3686B18A3D4780")

    output("C200B40A82")
    output("04005AC33890")
    output("880086C3E88112")
    output("CE00C43D881120")

    println(output(textOfDay(2021,16)))
}

fun output(hexString : String) {
    val p = BitsPacket.buildFromHex(hexString)
    println("Version sum: ${sumVersions(p)}, resolved: ${p.calculate()}")
}

fun sumVersions(packet : BitsPacket) : Int {
    return packet.version + (if (packet.type != BitsPacketType.LITERAL) (packet.subpackets().map { sumVersions(it) }.sum()) else 0)
}