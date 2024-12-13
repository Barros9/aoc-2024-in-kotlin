package day13

import println
import readInput

fun main() {
    fun part1(input: List<String>): Int =
        parseMachines(input)
            .mapNotNull { findMinCost(it) }
            .sum()

    fun part2(input: List<String>): Long =
        parseMachines(input, 10_000_000_000_000L)
            .mapNotNull { findMinCostLarge(it) }
            .sum()

    val testInput = readInput("day13/Day13_test")
    check(part1(testInput) == 480)

    val input = readInput("day13/Day13")
    part1(input).println()
    part2(input).println()
}

private data class Machine(val aDelta: Pair<Long, Long>, val bDelta: Pair<Long, Long>, val prize: Pair<Long, Long>)

private fun parseMachines(input: List<String>, offset: Long = 0L): List<Machine> {
    val filtered = input.filter { it.isNotBlank() }
    return (filtered.indices step 3).map { i ->
        val a = parseLine(filtered[i])
        val b = parseLine(filtered[i+1])
        val p = parsePrizeLine(filtered[i+2])
        Machine(a, b, (p.first + offset to p.second + offset))
    }
}

private fun parseLine(line: String): Pair<Long, Long> {
    val parts = line.split(",")
    val xVal = parts[0].substringAfter("X").replace("+", "").replace("=", "").trim().toLong()
    val yVal = parts[1].substringAfter("Y").replace("+", "").replace("=", "").trim().toLong()
    return xVal to yVal
}

private fun parsePrizeLine(line: String): Pair<Long, Long> {
    val pParts = line.split(",")
    val pX = pParts[0].substringAfter("X=").trim().toLong()
    val pY = pParts[1].substringAfter("Y=").trim().toLong()
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

private fun findMinCostLarge(machine: Machine): Long? {
    val (ax, ay) = machine.aDelta
    val (bx, by) = machine.bDelta
    val (px, py) = machine.prize

    val det = ax * by - ay * bx
    if (det == 0L) return null

    val aNum = (px * by - py * bx)
    val bNum = (ax * py - ay * px)

    if (aNum % det != 0L || bNum % det != 0L) return null

    val a = aNum / det
    val b = bNum / det

    if (a < 0 || b < 0) return null

    return 3*a + b
}