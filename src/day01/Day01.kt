package day01

import println
import readInput
import kotlin.math.abs

fun main() {
    fun part1(input: List<String>): Int {
        val (firstArray, secondArray) = input
            .map { it.split("   ").map(String::toInt) }
            .let { pairs -> pairs.map { it[0] } to pairs.map { it[1] } }

        val sortedFirst = firstArray.sorted()
        val sortedSecond = secondArray.sorted()

        return sortedFirst
            .zip(sortedSecond)
            .sumOf { (valueFirst, valueSecond) -> abs(valueFirst - valueSecond) }
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    val testInput = readInput("day01/Day01_test")
    check(part1(testInput) == 11)
//    check(part2(testInput) == 1)

    val input = readInput("day01/Day01")
    part1(input).println()
    part2(input).println()
}
