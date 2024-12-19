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

    fun part2(input: List<String>): Long {
        val (patterns, designs) = parseInput(input)

        var totalWays = 0L
        val memo = mutableMapOf<String, Long>()

        for (design in designs) {
            totalWays += countWaysToConstruct(design, patterns, memo)
        }

        return totalWays
    }

    val testInput = readInput("day19/Day19_test")
    check(part1(testInput) == 6)
    check(part2(testInput) == 16L)

    val input = readInput("day19/Day19")
    println(part1(input))
    println(part2(input))
}

private fun parseInput(input: List<String>): Pair<Set<String>, List<String>> {
    val patterns = input.first().split(", ").toSet()
    val designs = input.drop(2)
    return Pair(patterns, designs)
}

private fun canConstructDesign(design: String, patterns: Set<String>, memo: MutableMap<String, Boolean>): Boolean {
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

private fun countWaysToConstruct(design: String, patterns: Set<String>, memo: MutableMap<String, Long>): Long {
    if (design in memo) return memo[design]!!
    if (design.isEmpty()) return 1L

    var ways = 0L
    for (pattern in patterns) {
        if (design.startsWith(pattern)) {
            val remaining = design.removePrefix(pattern)
            ways += countWaysToConstruct(remaining, patterns, memo)
        }
    }

    memo[design] = ways
    return ways
}