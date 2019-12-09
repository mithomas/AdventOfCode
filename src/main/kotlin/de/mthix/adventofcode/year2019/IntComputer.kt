package de.mthix.adventofcode.year2019

import de.mthix.adventofcode.year2019.IntComputer.ParamMode.*
import org.apache.commons.lang3.builder.ToStringBuilder
import java.util.*

class IntComputer(program: Array<Long>, memorySize: Int = program.size) {

    val program: Array<Long> = Array(memorySize) { 0L }

    private var relativeBase = 0
    var index = 0
    var completed = false

    init {
        program.copyInto(this.program)
    }

    fun process(): List<Long> {
        return process(mutableListOf())
    }

    fun process(input: Long): List<Long> {
        return process(mutableListOf(input))
    }

    fun process(input: MutableList<Long>): List<Long> {
        println("input:       $input")
        println("program size:${program.size}\n")
        val output = LinkedList<Long>()

        var instruction = Instruction(program, index, relativeBase)

        while (!completed && !(instruction.opcode == OpCode.INPUT && input.isEmpty())) {
            //println("instruction: $instruction")

            var jumped = false

            when (instruction.opcode) {
                OpCode.ADD -> program[instruction.targetIndex] = instruction.operands.sum()
                OpCode.MULTIPLY -> program[instruction.targetIndex] = (instruction.operands.reduce { sum, operand -> sum * operand }).toLong()
                OpCode.INPUT -> program[instruction.targetIndex] = input.removeAt(0)
                OpCode.OUTPUT -> output += instruction.operands.sum()
                OpCode.JUMP_IF_TRUE -> if (instruction.operands[0] > 0) {
                    index = instruction.operands[1].toInt()
                    jumped = true
                }
                OpCode.JUMP_IF_FALSE -> if (instruction.operands[0] == 0L) {
                    index = instruction.operands[1].toInt()
                    jumped = true
                }
                OpCode.LESS_THEN -> program[instruction.targetIndex] = if (instruction.operands[0] < instruction.operands[1]) 1 else 0
                OpCode.EQUALS -> program[instruction.targetIndex] = if (instruction.operands[0] == instruction.operands[1]) 1 else 0
                OpCode.SHIFT_RELATIVE_BASE -> relativeBase += instruction.operands[0].toInt()
                OpCode.END -> completed = true
            }

            if (!jumped) {
                index += instruction.pointerIncrement
            }
            //println("new index:   $index")
            //println("new base:    $relativeBase")
            //println("new opcode:  ${program[index]}")
            //println("program:     ${program.asList().mapIndexed { i, e -> "$i:$e" }}")
            //println("output:      $output\n")

            if (!completed) instruction = Instruction(program, index, relativeBase)
        }

        return output
    }

    companion object {
        const val OPCODE_LENGTH = 2
    }


    class Instruction(private val program: Array<Long>, index: Int, private val relativeBase: Int) {
        val opcode: OpCode
        val pointerIncrement: Int
        val targetIndex: Int
        val operands: MutableList<Long> = LinkedList()
        private val positionModes: MutableList<ParamMode> = LinkedList()

        init {
            val opcodeDefinition = program[index].toString()
            opcode = OpCode.of(opcodeDefinition)

            (1..(opcode.operandCount + (if (opcode.hasTarget) 1 else 0))).forEach { positionModes += ParamMode.of(opcodeDefinition, it) }
            (1..opcode.operandCount).forEach { operands += getOperand(index + it, positionModes[it - 1]) }

            targetIndex = getTarget(index + opcode.operandCount + 1)
            pointerIncrement = 1 + opcode.operandCount + (if (opcode.hasTarget) 1 else 0)
        }

        private fun getTarget(targetParamIndex: Int): Int {
            if(!opcode.hasTarget) return -1

            val targetIndex = program[targetParamIndex].toInt()
            return if (positionModes.last() == RELATIVE) relativeBase + targetIndex else targetIndex
        }

        private fun getOperand(operandParamIndex: Int, mode: ParamMode): Long {
            return when (mode) {
                POSITION -> program[program[operandParamIndex].toInt()]
                IMMEDIATE -> program[operandParamIndex]
                RELATIVE -> program[(relativeBase + program[operandParamIndex]).toInt()]
            }
        }

        override fun toString(): String {
            return ToStringBuilder.reflectionToString(this)
        }
    }


    enum class OpCode(val opcode: Int, val operandCount: Int, val hasTarget: Boolean) {
        ADD(1, 2, true),
        MULTIPLY(2, 2, true),
        INPUT(3, 0, true),
        OUTPUT(4, 1, false),
        JUMP_IF_TRUE(5, 2, false),
        JUMP_IF_FALSE(6, 2, false),
        LESS_THEN(7, 2, true),
        EQUALS(8, 2, true),
        SHIFT_RELATIVE_BASE(9, 1, false),
        END(99, 0, false);

        companion object {
            fun of(opcodeDefinition: String): OpCode {
                val opcode = if (opcodeDefinition.length > 2) opcodeDefinition.substring(opcodeDefinition.length - OPCODE_LENGTH).toInt() else opcodeDefinition.toInt()
                //println("parsing code:$opcode")
                return values().first { it.opcode == opcode }
            }
        }
    }


    enum class ParamMode(val mode: Int) {
        POSITION(0),
        IMMEDIATE(1),
        RELATIVE(2),
        ;

        companion object {

            fun of(opcodeMode: String, operandIndex: Int): ParamMode {
                if (opcodeMode.length >= operandIndex + OPCODE_LENGTH) {
                    return values().first { it.mode == opcodeMode[opcodeMode.length - operandIndex - OPCODE_LENGTH].toString().toInt() }
                }

                //println("param mode:  default")
                return POSITION
            }
        }
    }
}
