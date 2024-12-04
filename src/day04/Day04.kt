package day04

import println
import readInput

fun main() {
    fun part1(input: List<String>): Int = countOccurrences(input, "XMAS")

    fun part2(input: List<String>): Int {
        return 0
    }

    val testInput1 = readInput("day04/Day04_test_1")
    check(part1(testInput1) == 18)
    val testInput2 = readInput("day04/Day04_test_2")
    check(part2(testInput2) == 0)

    val input = readInput("day04/Day04")
    part1(input).println()
    part2(input).println()
}

private fun countOccurrences(grid: List<String>, word: String): Int {
    val directions = listOf(
        Pair(0, 1),
        Pair(1, 0),
        Pair(1, 1),
        Pair(-1, 1),
        Pair(0, -1),
        Pair(-1, 0),
        Pair(-1, -1),
        Pair(1, -1)
    )
    val rows = grid.size
    val columns = grid[0].length
    var count = 0

    fun isValid(x: Int, y: Int): Boolean = x in 0 until rows && y in 0 until columns

    fun searchFrom(x: Int, y: Int, direction: Pair<Int, Int>): Boolean {
        for (i in word.indices) {
            val nx = x + i * direction.first
            val ny = y + i * direction.second
            if (!isValid(nx, ny) || grid[nx][ny] != word[i]) {
                return false
            }
        }
        return true
    }

    for (x in 0 until rows) {
        for (y in 0 until columns) {
            if (grid[x][y] == word[0]) {
                for (direction in directions) {
                    if (searchFrom(x, y, direction)) {
                        count++
                    }
                }
            }
        }
    }
    return count
}