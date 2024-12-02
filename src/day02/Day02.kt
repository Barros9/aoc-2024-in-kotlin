package day02

import println
import readInput
import kotlin.math.abs

fun main() {
    fun part1(input: List<String>): Int {
        val reports = input.map { line -> line.split(" ").map(String::toInt) }
        return reports.count { levels -> isOrdered(levels, true) || isOrdered(levels, false) }
    }

    fun part2(input: List<String>): Int {
        val reports = input.map { line -> line.split(" ").map(String::toInt) }
        return reports.count { levels -> isSafe(levels) }
    }

    val testInput = readInput("day02/Day02_test")
    check(part1(testInput) == 2)
    check(part2(testInput) == 4)

    val input = readInput("day02/Day02")
    part1(input).println()
    part2(input).println()
}

private fun isOrdered(levels: List<Int>, increasing: Boolean): Boolean =
    levels
        .zip(levels.drop(1))
        .all { (currentLevel, nextLevel) ->
            val condition = if (increasing) currentLevel < nextLevel else currentLevel > nextLevel
            condition && abs(currentLevel - nextLevel) in 1..3
        }

private fun isSafe(level: List<Int>): Boolean {
    if (isOrdered(level, true) || isOrdered(level, false)) return true
    return level.indices.any { index ->
        val modifiedLevel = level.toMutableList().apply { removeAt(index) }
        isOrdered(modifiedLevel, true) || isOrdered(modifiedLevel, false)
    }
}