package day18

import readInput

fun main() {
    fun part1(input: List<String>, gridSize: Int): Int {
        val grid = parseInput(input, gridSize)
        printGrid(grid)
        return bfs(grid, Pair(0, 0), Pair(gridSize - 1, gridSize - 1))
    }

    fun part2(input: List<String>): Int = 0

    val testInput = readInput("day18/Day18_test")
    val testGridSize = 7
    check(part1(testInput.take(12), testGridSize) == 22)
    check(part2(testInput) == 0)

    val input = readInput("day18/Day18")
    val normalGridSize = 71
    println(part1(input.take(1024), normalGridSize))
    println(part2(input))
}

private fun parseInput(input: List<String>, gridSize: Int): Array<CharArray> {
    val grid = Array(gridSize) { CharArray(gridSize) { '.' } }
    input.forEach { line ->
        val (x, y) = line.split(",").map { it.toInt() }
        if (x in grid.indices && y in grid.indices) {
            grid[y][x] = '#'
        }
    }
    return grid
}

private fun printGrid(grid: Array<CharArray>) {
    grid.forEach { row ->
        println(row.joinToString(""))
    }
}

private fun bfs(grid: Array<CharArray>, start: Pair<Int, Int>, end: Pair<Int, Int>): Int {
    val directions = listOf(Pair(0, 1), Pair(1, 0), Pair(0, -1), Pair(-1, 0))
    val queue = ArrayDeque<Triple<Int, Int, Int>>()
    val visited = mutableSetOf<Pair<Int, Int>>()

    queue.add(Triple(start.first, start.second, 0))
    visited.add(start)

    while (queue.isNotEmpty()) {
        val (x, y, steps) = queue.removeFirst()

        if (x == end.first && y == end.second) return steps

        for ((dx, dy) in directions) {
            val nx = x + dx
            val ny = y + dy

            if (
                nx in grid.indices && ny in grid.indices &&
                grid[ny][nx] == '.' &&
                Pair(nx, ny) !in visited
            ) {
                visited.add(Pair(nx, ny))
                queue.add(Triple(nx, ny, steps + 1))
            }
        }
    }

    return -1
}
