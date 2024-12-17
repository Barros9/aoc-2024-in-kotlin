package day17

import println
import readInput

fun main() {
    fun part1(input: List<String>): String {
        val (registers, program) = parseInput(input)
        val output = runProgram(registers, program)
        return output.joinToString(",")
    }

    fun part2(input: List<String>): Long {
        val (_, program) = parseInput(input)
        return decipher(0L, program, program.size - 1)
    }

    val testInput1 = readInput("day17/Day17_test_1")
    check(part1(testInput1) == "4,6,3,5,6,3,5,2,1,0")

    val testInput2 = readInput("day17/Day17_test_2")
    check(part2(testInput2) == 117440L)

    val input = readInput("day17/Day17")
    part1(input).println()
    part2(input).println()
}

private fun parseInput(input: List<String>): Pair<MutableList<Long>, List<Int>> {
    val registers = mutableListOf(0L, 0L, 0L)
    var programLine = ""

    for (line in input) {
        when {
            line.startsWith("Register A:") -> registers[0] = line.split(": ")[1].toLong()
            line.startsWith("Register B:") -> registers[1] = line.split(": ")[1].toLong()
            line.startsWith("Register C:") -> registers[2] = line.split(": ")[1].toLong()
            line.startsWith("Program:") -> programLine = line.split(": ")[1]
        }
    }

    val program = programLine.split(",").map { it.trim().toInt() }
    return Pair(registers, program)
}

private fun runProgram(registers: MutableList<Long>, program: List<Int>): List<Int> {
    var (a, b, c) = registers
    var ip = 0
    val output = mutableListOf<Int>()

    fun getOperandValue(operand: Int): Long {
        return when (operand) {
            in 0..3 -> operand.toLong()
            4 -> a
            5 -> b
            6 -> c
            else -> -1
        }
    }

    while (ip < program.size) {
        val op = program[ip]
        val operand = program[ip + 1]
        val co = getOperandValue(operand)
        ip += 2

        when (op) {
            0 -> a /= 1 shl co.toInt() // adv
            1 -> b = b xor operand.toLong() // bxl
            2 -> b = co % 8 // bst
            3 -> if (a != 0L) ip = operand // jnz
            4 -> b = b xor c // bxc
            5 -> output.add((co % 8).toInt()) // out
            6 -> b = a / (1 shl co.toInt()) // bdv
            7 -> c = a / (1 shl co.toInt()) // cdv
            else -> throw IllegalArgumentException("Unknown command: $op")
        }
    }
    return output
}

private fun decipher(subA: Long, program: List<Int>, left: Int): Long {
    if (left < 0) return subA
    for (i in 0..7) {
        val a = subA * 8 + i
        val output = runProgram(mutableListOf(a, 0L, 0L), program)
        if (output == program.subList(left, program.size)) {
            val result = decipher(a, program, left - 1)
            if (result != 0L) return result
        }
    }
    return 0L
}