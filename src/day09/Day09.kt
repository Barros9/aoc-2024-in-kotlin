package day09

import println
import readInput

fun main() {
    fun part1(input: List<String>): Long {
        val disk = mapDisk(input.first())
        val compactDisk = compactDisk(disk)
        return calculateSum(compactDisk)
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    val testInput = readInput("day09/Day09_test")
    check(part1(testInput) == 1928L)
    check(part2(testInput) == 0)

    val input = readInput("day09/Day09")
    part1(input).println()
    part2(input).println()
}

private fun mapDisk(input: String): String = buildString {
    input.forEachIndexed { index, char ->
        val value = if (index % 2 == 0) {
            (index / 2).toString().padStart(4, '0')
        } else {
            ".".repeat(4)
        }
        repeat(char.digitToInt()) { append(value) }
    }
}

private fun compactDisk(disk: String): String {
    val chunks = disk.chunked(4).toMutableList()

    while (true) {
        val freeIndex = chunks.indexOfFirst { it == "...." }
        val lastFileIndex = chunks.indexOfLast { it != "...." }

        if (freeIndex > lastFileIndex) break

        chunks[freeIndex] = chunks[lastFileIndex]
        chunks[lastFileIndex] = "...."
    }

    return chunks.joinToString("")
}

private fun calculateSum(disk: String): Long =
    disk.chunked(4)
        .map { it.toLongOrNull() ?: 0 }
        .mapIndexed { index, number -> index * number }
        .sum()