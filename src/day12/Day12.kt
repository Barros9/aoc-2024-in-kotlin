package day12

import println
import readInput

fun main() {
    fun part1(input: List<String>): Int = calculateFenceCost(input)

    fun part2(input: List<String>): Int {
        return 0
    }

    val testInput = readInput("day12/Day12_test")
    check(part1(testInput) == 1930)

    val input = readInput("day12/Day12")
    part1(input).println()
    part2(input).println()
}

private fun calculateFenceCost(map: List<String>): Int {
    val rows = map.size
    val cols = map[0].length
    val visited = Array(rows) { BooleanArray(cols) }

    fun dfs(row: Int, col: Int, plantType: Char): Pair<Int, Int> {
        if (row !in 0 until rows || col !in 0 until cols) {
            return 0 to 1
        }
        if (visited[row][col]) {
            return 0 to 0
        }
        if (map[row][col] != plantType) {
            return 0 to 1
        }

        visited[row][col] = true
        var area = 1
        var perimeter = 0

        val directions = listOf(-1 to 0, 1 to 0, 0 to -1, 0 to 1)
        for ((dr, dc) in directions) {
            val newRow = row + dr
            val newCol = col + dc
            if (newRow !in 0 until rows || newCol !in 0 until cols) {
                perimeter += 1
            } else if (map[newRow][newCol] != plantType) {
                perimeter += 1
            } else if (!visited[newRow][newCol]) {
                val (subArea, subPerimeter) = dfs(newRow, newCol, plantType)
                area += subArea
                perimeter += subPerimeter
            }
        }

        return area to perimeter
    }

    var totalCost = 0

    for (r in 0 until rows) {
        for (c in 0 until cols) {
            if (!visited[r][c]) {
                val plantType = map[r][c]
                val (area, perimeter) = dfs(r, c, plantType)
                totalCost += area * perimeter
            }
        }
    }

    return totalCost
}
