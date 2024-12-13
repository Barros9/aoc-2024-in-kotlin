package day11

import println
import readInput
import java.math.BigInteger

fun main() {
    fun part1(input: List<String>): Int {
        var stones = input[0].split(" ")

        repeat(25) {
            stones = transformStones(stones)
        }

        return stones.size
    }

    fun part2(input: List<String>): BigInteger {
        var stoneCounts = input[0].split(" ").groupingBy { it }.eachCount().mapValues { it.value.toBigInteger() }

        repeat(75) {
            stoneCounts = transformStonesCounts(stoneCounts)
        }

        return stoneCounts.values.reduce(BigInteger::add)
    }

    val testInput = readInput("day11/Day11_test")
    check(part1(testInput) == 55312)

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

private fun transformStonesCounts(stones: Map<String, BigInteger>): Map<String, BigInteger> {
    val newCounts = mutableMapOf<String, BigInteger>()

    for ((stone, count) in stones) {
        when {
            stone == "0" -> {
                newCounts["1"] = newCounts.getOrDefault("1", BigInteger.ZERO) + count
            }

            stone.length % 2 == 0 -> {
                val mid = stone.length / 2
                val left = stone.substring(0, mid).trimStart('0').ifEmpty { "0" }
                val right = stone.substring(mid).trimStart('0').ifEmpty { "0" }

                newCounts[left] = newCounts.getOrDefault(left, BigInteger.ZERO) + count
                newCounts[right] = newCounts.getOrDefault(right, BigInteger.ZERO) + count
            }

            else -> {
                val newStone = (stone.toBigInteger() * BigInteger.valueOf(2024)).toString()
                newCounts[newStone] = newCounts.getOrDefault(newStone, BigInteger.ZERO) + count
            }
        }
    }

    return newCounts
}
