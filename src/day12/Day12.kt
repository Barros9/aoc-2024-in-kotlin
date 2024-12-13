package day12

import println
import readInput

fun main() {
    fun part1(input: List<String>): Int {
        var sum = 0
        findRegions(input) { region, perimeter ->
            sum += region.size * perimeter
        }
        return sum
    }

    fun part2(input: List<String>): Int {
        fun Coords.corners(char: Char): Int {
            var c = 0
            listOf(-1 to -1, 1 to -1, -1 to 1, 1 to 1).forEach { (dx, dy) ->
                if (input[x + dx, y] != char && input[x, y + dy] != char) c++
                if (input[x + dx, y] == char && input[x, y + dy] == char && input[x + dx, y + dy] != char) c++
            }
            return c
        }
        var sum = 0
        findRegions(input) { region, _ ->
            sum += region.size * region.sumOf { it.corners(input[it]!!) }
        }
        return sum
    }

    val testInput = readInput("day12/Day12_test")
    check(part1(testInput) == 1930)
    check(part2(testInput) == 1206)

    val input = readInput("day12/Day12")
    part1(input).println()
    part2(input).println()
}

private data class Coords(val x: Int, val y: Int)
private operator fun List<String>.get(x: Int, y: Int) = getOrNull(y)?.getOrNull(x)
private operator fun List<String>.get(coords: Coords) = get(coords.x, coords.y)

private fun findRegions(input: List<String>, onRegionFound: (plots: Set<Coords>, perimeter: Int) -> Unit) {
    val xRange = input[0].indices
    val yRange = input.indices
    val visited = mutableSetOf<Coords>()

    fun regionAndPerimeter(startX: Int, startY: Int): Pair<Set<Coords>, Int> {
        val region = mutableSetOf<Coords>()
        var perimeter = 0
        val stack = ArrayDeque<Pair<Int, Int>>()
        stack += startX to startY
        val char = input[startX, startY]!!
        val directions = listOf(0 to -1, 0 to 1, -1 to 0, 1 to 0)
        while (stack.isNotEmpty()) {
            val (x, y) = stack.removeLast()
            val current = Coords(x, y)
            if (current in region) continue
            if (input[current] != char) {
                perimeter++
                continue
            }
            region += current
            directions.forEach { (dx, dy) -> stack += x + dx to y + dy }
        }
        return region to perimeter
    }

    for (y in yRange) {
        for (x in xRange) {
            if (Coords(x, y) !in visited) {
                val (region, perimeter) = regionAndPerimeter(x, y)
                visited += region
                onRegionFound(region, perimeter)
            }
        }
    }
}
