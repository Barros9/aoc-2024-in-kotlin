package day11

import println
import readInput

fun main() {
    fun part1(input: List<String>): Int {
        var stones = input[0].split(" ")

        repeat(25) {
            stones = transformStones(stones)
        }

        return stones.size
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    val testInput = readInput("day11/Day11_test")
    check(part1(testInput) == 55312)
    check(part2(testInput) == 0)

    val input = readInput("day11/Day11")
    part1(input).println()
    part2(input).println()
}

private fun transformStones(stones: List<String>): List<String> {
    val newStones = mutableListOf<String>()

    for (stone in stones) {
        when {
            stone == "0" -> newStones.add("1")

            stone.length % 2 == 0 -> {
                val mid = stone.length / 2
                val left = stone.substring(0, mid).trimStart('0')
                val right = stone.substring(mid).trimStart('0')
                if (left.isNotEmpty()) newStones.add(left) else newStones.add("0")
                if (right.isNotEmpty()) newStones.add(right) else newStones.add("0")
            }

            else -> newStones.add((stone.toLong() * 2024).toString())
        }
    }

    return newStones
}
