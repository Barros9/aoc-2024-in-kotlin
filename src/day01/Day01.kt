package day01

import println
import readInput
import kotlin.math.abs

fun main() {
    fun part1(input: List<String>): Int {
        val (firstArray, secondArray) = input
            .map { line -> line.split("   ").map(String::toInt) }
            .let { pairs -> pairs.map { it[0] }.sorted() to pairs.map { it[1] }.sorted() }

        return firstArray
            .zip(secondArray)
            .sumOf { (valueFirst, valueSecond) -> abs(valueFirst - valueSecond) }
    }

    fun part2(input: List<String>): Int {
        val (firstArray, secondArray) = input
            .map { line -> line.split("   ").map(String::toInt) }
            .let { pairs -> pairs.map { it[0] } to pairs.map { it[1] } }

        val countMap = secondArray.groupingBy { it }.eachCount()

        return firstArray.sumOf { valueFirst ->
            valueFirst * (countMap[valueFirst] ?: 0)
        }
    }

    val testInput = readInput("day01/Day01_test")
    check(part1(testInput) == 11)
    check(part2(testInput) == 31)

    val input = readInput("day01/Day01")
    part1(input).println()
    part2(input).println()
}
