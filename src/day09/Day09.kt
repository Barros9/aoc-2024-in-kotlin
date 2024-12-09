package day09

import println
import readInput

fun main() {
    fun part1(input: List<String>): Long {
        val disk = mapDisk(input.first())
        val compactDisk = compactDisk(disk)
        return calculateSum(compactDisk)
    }

    fun part2(input: List<String>): Long {
        val disk = mapDisk(input.first())
        val compactDisk = compactDiskOptimized(disk)
        return calculateSum(compactDisk)
    }

    val testInput = readInput("day09/Day09_test")
    check(part1(testInput) == 1928L)
    check(part2(testInput) == 2858L)

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

private fun compactDiskOptimized(disk: String): String {
    val chunks = disk.chunked(4).reversed().fold(mutableListOf<MutableList<String>>()) { acc, element ->
        if (acc.isEmpty() || acc.last().last() != element) {
            acc.add(mutableListOf(element))
        } else {
            acc.last().add(element)
        }
        acc
    }

    var checked = Int.MAX_VALUE
    while (true) {
        val suitableFileIndex = chunks
            .asSequence()
            .withIndex()
            .indexOfFirst { (index, it) ->
                "...." !in it &&
                        it.any { it.toInt() < checked } &&
                        it.size <= (
                        chunks
                            .asSequence()
                            .drop(index + 1)
                            .filter { "...." in it }
                            .maxOfOrNull { it.size } ?: 0
                        )
            }

        if (suitableFileIndex == -1) {
            break
        }

        val suitableFileSize = chunks[suitableFileIndex].size
        val suitableFreeSpaceIndex = chunks.indexOfLast { "...." in it && it.size >= suitableFileSize }
        val suitableFreeSpaceSize = chunks[suitableFreeSpaceIndex].size

        checked = chunks[suitableFileIndex].first().toInt()

        chunks[suitableFreeSpaceIndex] = chunks[suitableFileIndex]
        chunks[suitableFileIndex] = chunks[suitableFileIndex].map { "...." }.toMutableList()
        if (suitableFileSize < suitableFreeSpaceSize) {
            chunks.add(
                suitableFreeSpaceIndex,
                buildList { repeat(suitableFreeSpaceSize - suitableFileSize) { add("....") } }.toMutableList(),
            )
        }
    }

    return chunks.reversed().flatten().joinToString("")
}

private fun calculateSum(disk: String): Long =
    disk.chunked(4)
        .map { it.toLongOrNull() ?: 0 }
        .mapIndexed { index, number -> index * number }
        .sum()