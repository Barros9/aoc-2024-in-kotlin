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
        val regex = Regex("""do\(\)|don't\(\)|mul\(\d+,\d+\)""")
        val matches = regex.findAll(input).map { it.value }.toList()
        val pairRegex = Regex("""mul\((\d+),(\d+)\)""")
        var isMulEnabled = true
        val pairs = mutableListOf<Pair<Int, Int>>()
        for (instruction in matches) {
            when (instruction) {
                "do()" -> isMulEnabled = true
                "don't()" -> isMulEnabled = false
                else -> if (isMulEnabled && pairRegex.matches(instruction)) {
                    val match = pairRegex.find(instruction)!!
                    val first = match.groupValues[1].toInt()
                    val second = match.groupValues[2].toInt()
                    pairs.add(Pair(first, second))
                }
            }
        }
        return pairs.sumOf { it.first * it.second }
    }

    val testInput1 = readText("day03/Day03_test_1")
    check(part1(testInput1) == 161)
    val testInput2 = readText("day03/Day03_test_2")
    check(part2(testInput2) == 48)

    val input = readText("day03/Day03")
    part1(input).println()
    part2(input).println()
}
