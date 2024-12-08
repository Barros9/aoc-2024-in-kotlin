package day08

import println
import readInput

fun main() {
    fun part1(input: List<String>): Int {
        val nodes = parseGrid(input)
        return countAntiNodes(nodes, input, ::antiNodesForPart1)
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    val testInput = readInput("day08/Day08_test")
    check(part1(testInput) == 14)
    check(part2(testInput) == 0)

    val input = readInput("day08/Day08")
    part1(input).println()
    part2(input).println()
}

private fun countAntiNodes(
    points: Map<Char, List<Point2D>>,
    grid: List<String>,
    worker: (Point2D, Point2D, Point2D) -> Set<Point2D>
): Int = points.values
    .flatMap { nodeList ->
        nodeList.flatMapIndexed { i, a ->
            nodeList.drop(i + 1).flatMap { b ->
                worker(a, b, a - b)
            }
        }
    }
    .filter { it.isOnGrid(grid) }
    .toSet()
    .size

private fun antiNodesForPart1(a: Point2D, b: Point2D, diff: Point2D): Set<Point2D> =
    setOf(a + diff, b - diff)

private fun parseGrid(input: List<String>): Map<Char, List<Point2D>> =
    input.flatMapIndexed { y, row ->
        row.mapIndexedNotNull { x, c ->
            if (c != '.') c to Point2D(x, y) else null
        }
    }.groupBy({ it.first }, { it.second })

private data class Point2D(val x: Int, val y: Int) {
    operator fun minus(other: Point2D) = Point2D(x - other.x, y - other.y)
    operator fun plus(other: Point2D) = Point2D(x + other.x, y + other.y)
    fun isOnGrid(grid: List<String>) = y in grid.indices && x in grid[y].indices
}