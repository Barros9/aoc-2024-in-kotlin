package day06

import println
import readInput


fun main() {
    fun part1(input: List<String>): Int {
        val directions = listOf(Pair(-1, 0), Pair(0, 1), Pair(1, 0), Pair(0, -1))
        val turns = listOf('^', '>', 'v', '<')

        var x = 0
        var y = 0
        var dirIndex = 0

        input.forEachIndexed { i, row ->
            row.forEachIndexed { j, cell ->
                if (cell in turns) {
                    x = i
                    y = j
                    dirIndex = turns.indexOf(cell)
                }
            }
        }

        val rows = input.size
        val columns = input[0].length
        val visited = mutableSetOf<Pair<Int, Int>>()

        visited.add(Pair(x, y))

        while (true) {
            val (dx, dy) = directions[dirIndex]
            val nx = x + dx
            val ny = y + dy

            if (nx !in 0 until rows || ny !in 0 until columns) {
                break
            }

            if (input[nx][ny] != '#') {
                x = nx
                y = ny
                visited.add(Pair(x, y))
            } else {
                dirIndex = (dirIndex + 1) % 4
            }
        }

        return visited.size
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    val testInput = readInput("day06/Day06_test")
    check(part1(testInput) == 41)
    check(part2(testInput) == 0)

    val input = readInput("day06/Day06")
    part1(input).println()
    part2(input).println()
}
