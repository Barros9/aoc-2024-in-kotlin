package day17

import println
import readInput

fun main() {
    fun part1(input: List<String>): String {
        val (registers, program) = parseInput(input)
        val output = runProgram(registers, program)
        return output.joinToString(",")
    }

    fun part2(input: List<String>): Int {
        val (_, program) = parseInput(input)

        var a = 1
        while (true) {
            val registers = mutableListOf(a, 0, 0)
            val output = runProgram(registers, program)

            if (output == program) {
                return a
            }
            a++
        }
    }

    val testInput1 = readInput("day17/Day17_test_1")
    check(part1(testInput1) == "4,6,3,5,6,3,5,2,1,0")

    val testInput2 = readInput("day17/Day17_test_2")
    check(part2(testInput2) == 117440)

    val input = readInput("day17/Day17")
    part1(input).println()
    part2(input).println()
}

private fun parseInput(input: List<String>): Pair<MutableList<Int>, List<Int>> {
    val registers = mutableListOf(0, 0, 0)
    var programLine = ""

    for (line in input) {
        when {
            line.startsWith("Register A:") -> registers[0] = line.split(": ")[1].toInt()
            line.startsWith("Register B:") -> registers[1] = line.split(": ")[1].toInt()
            line.startsWith("Register C:") -> registers[2] = line.split(": ")[1].toInt()
            line.startsWith("Program:") -> programLine = line.split(": ")[1]
        }
    }

    val program = programLine.split(",").map { it.trim().toInt() }
    return Pair(registers, program)
}

private fun runProgram(registers: MutableList<Int>, program: List<Int>): List<Int> {
    var (a, b, c) = registers
    var ip = 0
    val output = mutableListOf<Int>()

    while (ip < program.size) {
        val opcode = program[ip]
        val operand = program[ip + 1]

        when (opcode) {
            0 -> a /= 1 shl comboValue(operand, a, b, c)  // adv
            1 -> b = b xor operand  // bxl
            2 -> b = comboValue(operand, a, b, c) % 8  // bst
            3 -> {  // jnz
                if (a != 0) {
                    ip = operand
                    continue
                }
            }
            4 -> b = b xor c  // bxc
            5 -> output.add(comboValue(operand, a, b, c) % 8)  // out
            6 -> b = a / (1 shl comboValue(operand, a, b, c))  // bdv
            7 -> c = a / (1 shl comboValue(operand, a, b, c))  // cdv
            else -> throw IllegalArgumentException("Unknown command: $opcode")
        }
        ip += 2
    }
    return output
}

private fun comboValue(operand: Int, a: Int, b: Int, c: Int): Int =
    when (operand) {
        in 0..3 -> operand
        4 -> a
        5 -> b
        6 -> c
        else -> throw IllegalArgumentException("Not valid: $operand")
    }