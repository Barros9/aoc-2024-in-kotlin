package day03

import println
import readInput
import readText

fun main() {
    fun part1(input: String): Int {
        val regex = Regex("""mul\(\d+,\d+\)""")
        val matches = regex.findAll(input).map { it.value }.toList()
        val pairRegex = Regex("""mul\((\d+),(\d+)\)""")
        val pairs = matches.mapNotNull { match ->
            val result = pairRegex.matchEntire(match)
            if (result != null) {
                val first = result.groupValues[1].toInt()
                val second = result.groupValues[2].toInt()
                Pair(first, second)
            } else {
                null
            }
        }
        return pairs.sumOf { it.first * it.second }
    }

    fun part2(input: String): Int {
        return 0
    }

    val testInput = readText("day03/Day03_test")
    check(part1(testInput) == 161)
    check(part2(testInput) == 0)

    val input = readText("day03/Day03")
    part1(input).println()
    part2(input).println()
}
