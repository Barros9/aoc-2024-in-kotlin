package day10

import println
import readInput

fun main() {
    fun part1(input: List<String>): Int {
        val map = parseMap(input)
        return findTrailheadScores(map)
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    val testInput = readInput("day10/Day10_test")
    check(part1(testInput) == 36)

    val input = readInput("day10/Day10")
    part1(input).println()
    part2(input).println()
}

private fun parseMap(input: List<String>): List<List<Int>> =
    input.map { line -> line.map { it.digitToInt() } }

private fun findTrailheadScores(map: List<List<Int>>): Int {
    val rows = map.size
    val cols = map[0].size

    return (0 until rows).sumOf { x ->
        (0 until cols).sumOf { y ->
            if (map[x][y] == 0) calculateScoreFromTrailhead(map, x, y) else 0
        }
    }
}

private fun isValidNeighbor(map: List<List<Int>>, x: Int, y: Int, currentHeight: Int, visited: Set<Pair<Int, Int>>): Boolean {
    val rows = map.size
    val cols = map[0].size
    return x in 0 until rows &&
            y in 0 until cols &&
            map[x][y] == currentHeight + 1 &&
            (x to y) !in visited
}

private fun calculateScoreFromTrailhead(map: List<List<Int>>, startX: Int, startY: Int): Int {
    val directions = listOf(0 to 1, 1 to 0, 0 to -1, -1 to 0)
    val queue = ArrayDeque<Pair<Int, Int>>().apply { add(startX to startY) }
    val visited = mutableSetOf(startX to startY)
    var score = 0

    while (queue.isNotEmpty()) {
        val (x, y) = queue.removeFirst()
        if (map[x][y] == 9) score++

        directions.forEach { (dx, dy) ->
            val nx = x + dx
            val ny = y + dy
            if (isValidNeighbor(map, nx, ny, map[x][y], visited)) {
                visited.add(nx to ny)
                queue.add(nx to ny)
            }
        }
    }

    return score
}
