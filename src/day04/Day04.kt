package day04

import println
import readInput

fun main() {
    fun part1(input: List<String>): Int = countXMAS(input)

    fun part2(input: List<String>): Int = countMASInShapeOfX(input)

    val testInput1 = readInput("day04/Day04_test_1")
    check(part1(testInput1) == 18)
    val testInput2 = readInput("day04/Day04_test_2")
    check(part2(testInput2) == 9)

    val input = readInput("day04/Day04")
    part1(input).println()
    part2(input).println()
}

private fun isValid(x: Int, y: Int, rows: Int, columns: Int): Boolean =
    x in 0 until rows && y in 0 until columns

private fun searchPattern(grid: List<String>, pattern: List<Char>, x: Int, y: Int, dx: Int, dy: Int): Boolean {
    for (i in pattern.indices) {
        val nx = x + i * dx
        val ny = y + i * dy
        if (!isValid(nx, ny, grid.size, grid[0].length) || grid[nx][ny] != pattern[i]) {
            return false
        }
    }
    return true
}

private fun countXMAS(grid: List<String>): Int {
    val xmasPattern = listOf('X', 'M', 'A', 'S')
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
    var count = 0
    for (x in grid.indices) {
        for (y in grid[x].indices) {
            for ((dx, dy) in directions) {
                if (searchPattern(grid, xmasPattern, x, y, dx, dy)) {
                    count++
                }
            }
        }
    }
    return count
}

private fun countMASInShapeOfX(grid: List<String>): Int {
    val masPatterns = listOf(listOf('M', 'A', 'S'), listOf('S', 'A', 'M'))
    var count = 0

    for (x in 1 until grid.size - 1) {
        for (y in 1 until grid[0].length - 1) {
            if (grid[x][y] == 'A') {
                val diagonal1 = masPatterns.any { searchPattern(grid, it, x - 1, y - 1, 1, 1) }
                val diagonal2 = masPatterns.any { searchPattern(grid, it, x - 1, y + 1, 1, -1) }
                if (diagonal1 && diagonal2) {
                    count++
                }
            }
        }
    }
    return count
}