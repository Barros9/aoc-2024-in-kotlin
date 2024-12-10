package day10

import println
import readInput

fun main() {
    fun part1(input: List<String>) = findTrailheadScores(parseMap(input))

    fun part2(input: List<String>) = findTrailheadRatings(parseMap(input))

    val testInput = readInput("day10/Day10_test")
    check(part1(testInput) == 36)
    check(part2(testInput) == 81)

    val input = readInput("day10/Day10")
    part1(input).println()
    part2(input).println()
}

private fun parseMap(input: List<String>) = input.map { line -> line.map { it.digitToInt() } }

private fun findTrailheadScores(map: List<List<Int>>) = calculateForAllTrailheads(map) {
    calculateScoreFromTrailhead(map, it.first, it.second)
}

private fun findTrailheadRatings(map: List<List<Int>>) = calculateForAllTrailheads(map) {
    countDistinctTrails(map, it.first, it.second)
}

private fun calculateForAllTrailheads(
    map: List<List<Int>>,
    operation: (Pair<Int, Int>) -> Int
) = map.indices.sumOf { x ->
    map[x].indices.sumOf { y ->
        if (map[x][y] == 0) operation(x to y) else 0
    }
}

private fun calculateScoreFromTrailhead(map: List<List<Int>>, startX: Int, startY: Int): Int {
    val directions = getDirections()
    val queue = ArrayDeque<Pair<Int, Int>>().apply { add(startX to startY) }
    val visited = mutableSetOf(startX to startY)
    var score = 0

    while (queue.isNotEmpty()) {
        val (x, y) = queue.removeFirst()
        if (map[x][y] == 9) score++

        directions.forEach { (dx, dy) ->
            val neighbor = x + dx to y + dy
            if (isValidNeighbor(map, neighbor, map[x][y], visited)) {
                visited.add(neighbor)
                queue.add(neighbor)
            }
        }
    }

    return score
}

private fun countDistinctTrails(map: List<List<Int>>, startX: Int, startY: Int): Int {
    val directions = getDirections()
    val stack = ArrayDeque<Pair<Pair<Int, Int>, Set<Pair<Int, Int>>>>()
    stack.add(startX to startY to emptySet())
    var trailCount = 0

    while (stack.isNotEmpty()) {
        val (position, visited) = stack.removeLast()
        val (x, y) = position

        if (map[x][y] == 9) {
            trailCount++
            continue
        }

        directions.forEach { (dx, dy) ->
            val neighbor = x + dx to y + dy
            if (isValidNeighbor(map, neighbor, map[x][y], visited)) {
                stack.add(neighbor to visited + position)
            }
        }
    }

    return trailCount
}

private fun isValidNeighbor(
    map: List<List<Int>>,
    neighbor: Pair<Int, Int>,
    currentHeight: Int,
    visited: Set<Pair<Int, Int>>
) = neighbor.first in map.indices &&
        neighbor.second in map[0].indices &&
        map[neighbor.first][neighbor.second] == currentHeight + 1 &&
        neighbor !in visited

private fun getDirections() = listOf(0 to 1, 1 to 0, 0 to -1, -1 to 0)
