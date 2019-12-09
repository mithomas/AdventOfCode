package de.mthix.adventofcode.year2019

class Circuit(private val program:Array<Long>, private val phaseSettingSequence:List<Long>) {

    private val amplifiers = (1..phaseSettingSequence.size).map { IntComputer(program.copyOf()) }

    fun calculate():Long {
        var output = 0L

        phaseSettingSequence.forEachIndexed() { i, phaseSetting ->
            println("Running amp $i")
            output = amplifiers[i].process(mutableListOf(phaseSetting,output))[0]
        }

        while(!amplifiers.last().completed) {
            phaseSettingSequence.forEachIndexed() { i, phaseSetting ->
                println("Running amp $i")
                output = amplifiers[i].process(mutableListOf(output))[0]
            }
        }

        return output
    }
}
