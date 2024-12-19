package day19

import readInput

fun main() {
    fun part1(input: List<String>): Int {
        val (patterns, designs) = parseInput(input)

        var validCount = 0
        val memo = mutableMapOf<String, Boolean>()

        for (design in designs) {
            if (canConstructDesign(design, patterns, memo)) {
                validCount++
            }
        }

        return validCount
    }

    fun part2(input: List<String>): Int = 0

    val testInput = readInput("day19/Day19_test")
    check(part1(testInput) == 6)

    val input = readInput("day19/Day19")
    println(part1(input))
    println(part2(input))
}

private fun parseInput(input: List<String>): Pair<Set<String>, List<String>> {
    val patterns = input.first().split(", ").toSet()
    val designs = input.drop(2)
    return Pair(patterns, designs)
}

fun canConstructDesign(design: String, patterns: Set<String>, memo: MutableMap<String, Boolean>): Boolean {
    if (design in memo) return memo[design]!!
    if (design.isEmpty()) return true

    for (pattern in patterns) {
        if (design.startsWith(pattern)) {
            val remaining = design.removePrefix(pattern)
            if (canConstructDesign(remaining, patterns, memo)) {
                memo[design] = true
                return true
            }
        }
    }
    memo[design] = false
    return false
}