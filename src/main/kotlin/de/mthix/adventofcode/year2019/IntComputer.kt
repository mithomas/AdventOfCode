package de.mthix.adventofcode.year2019

import org.apache.commons.lang3.builder.ToStringBuilder
import java.util.*

class IntComputer(val program: Array<Int>) {

    var index = 0
    var completed = false

    fun process(input:Int):List<Int> {
        return process(mutableListOf(input))
    }

    fun process(input: MutableList<Int>):List<Int> {
        println("input:       $input")
        println("program size:${program.size}\n")
        val output = LinkedList<Int>()

        var instruction = Instruction(program, index)

        while (!completed && !(instruction.opcode == OpCode.INPUT && input.isEmpty())) {
            //println("instruction: $instruction")

            var jumped = false

            when (instruction.opcode) {
                OpCode.ADD -> program[instruction.targetIndex] = instruction.operands.sum()
                OpCode.MULTIPLY -> program[instruction.targetIndex] = instruction.operands.reduce { sum, operand -> sum * operand }
                OpCode.INPUT -> program[instruction.targetIndex] = input.removeAt(0)
                OpCode.OUTPUT -> output += instruction.operands.sum()
                OpCode.JUMP_IF_TRUE -> if (instruction.operands[0] > 0) {
                    index = instruction.operands[1]
                    jumped = true
                }
                OpCode.JUMP_IF_FALSE -> if (instruction.operands[0] == 0) {
                    index = instruction.operands[1]
                    jumped = true
                }
                OpCode.LESS_THEN -> program[instruction.targetIndex] = if (instruction.operands[0] < instruction.operands[1]) 1 else 0
                OpCode.EQUALS -> program[instruction.targetIndex] = if (instruction.operands[0] == instruction.operands[1]) 1 else 0
                OpCode.END -> completed = true
            }

            if (!jumped) {
                index += instruction.pointerIncrement
            }
            //println("new index:   $index")
            //println("new opcode:  ${program[index]}")
            //println("program:     ${program.asList().mapIndexed { i, e -> "$i:$e" }}")
            //println("output:      $output\n")

            if(!completed) instruction = Instruction(program, index)
        }

        return output
    }

    companion object {
        const val OPCODE_LENGTH = 2
    }


    class Instruction(val program: Array<Int>, index: Int) {
        val opcode: OpCode
        val pointerIncrement: Int
        val targetIndex: Int
        val operands: MutableList<Int> = LinkedList()
        private val positionModes: MutableList<ParameterMode> = LinkedList()

        init {
            val opcodeMode = program[index].toString()
            opcode = OpCode.of(if (opcodeMode.length > 2) opcodeMode.substring(opcodeMode.length - OPCODE_LENGTH).toInt() else opcodeMode.toInt())

            (1..opcode.operandCount).forEach { i ->
                positionModes += ParameterMode.of(opcodeMode, i)
                operands += getOperand(index + i, positionModes[i - 1])
            }
            targetIndex = if (opcode.hasTarget) program[index + opcode.operandCount + 1] else -1
            pointerIncrement = 1 + opcode.operandCount + (if (opcode.hasTarget) 1 else 0)
        }

        private fun getOperand(value: Int, mode: ParameterMode): Int {
            return if (mode == ParameterMode.POSITION) program[program[value]] else program[value]
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
        END(99, 0, false);

        companion object {
            fun of(opcode: Int): OpCode {
                //println("parsing code:$opcode")
                return values().first { it.opcode == opcode }
            }
        }
    }


    enum class ParameterMode(val mode: Int) {
        POSITION(0),
        IMMEDIATE(1),
        ;

        companion object {

            fun of(opcodeMode: String, operandIndex: Int): ParameterMode {
                if (opcodeMode.length >= operandIndex + OPCODE_LENGTH) {
                    return values().first { it.mode == opcodeMode[opcodeMode.length - operandIndex - OPCODE_LENGTH].toString().toInt() }
                }

                //println("param mode:  default")
                return POSITION
            }
        }
    }
}
