package day13

import println
import readInput

fun main() {
    fun part1(input: List<String>): Int =
        parseMachines(input)
            .mapNotNull { findMinCost(it) }
            .sum()

    fun part2(input: List<String>): Int {
        return 0
    }

    val testInput = readInput("day13/Day13_test")
    check(part1(testInput) == 480)
    check(part2(testInput) == 0)

    val input = readInput("day13/Day13")
    part1(input).println()
    part2(input).println()
}

private data class Machine(val aDelta: Pair<Int, Int>, val bDelta: Pair<Int, Int>, val prize: Pair<Int, Int>)

private fun parseMachines(input: List<String>): List<Machine> {
    val filtered = input.filter { it.isNotBlank() }
    return (filtered.indices step 3).map { i ->
        val a = parseLine(filtered[i])
        val b = parseLine(filtered[i+1])
        val p = parsePrizeLine(filtered[i+2])
        Machine(a, b, p)
    }
}

private fun parseLine(line: String): Pair<Int, Int> {
    val parts = line.split(",")
    val xVal = parts[0].substringAfter("X").replace("+", "").replace("=", "").trim().toInt()
    val yVal = parts[1].substringAfter("Y").replace("+", "").replace("=", "").trim().toInt()
    return xVal to yVal
}

private fun parsePrizeLine(line: String): Pair<Int, Int> {
    val pParts = line.split(",")
    val pX = pParts[0].substringAfter("X=").trim().toInt()
    val pY = pParts[1].substringAfter("Y=").trim().toInt()
    return pX to pY
}

private fun findMinCost(machine: Machine): Int? {
    var minCost: Int? = null
    for (a in 0..100) {
        for (b in 0..100) {
            val x = a * machine.aDelta.first + b * machine.bDelta.first
            val y = a * machine.aDelta.second + b * machine.bDelta.second
            if (x == machine.prize.first && y == machine.prize.second) {
                val cost = 3*a + b
                if (minCost == null || cost < minCost) minCost = cost
            }
        }
    }
    return minCost
}