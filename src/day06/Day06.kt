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
        val directions = listOf('^', '>', 'v', '<')
        var startX = 0
        var startY = 0
        var startDir = 0

        input.forEachIndexed { i, row ->
            row.forEachIndexed { j, cell ->
                if (cell in directions) {
                    startX = i
                    startY = j
                    startDir = directions.indexOf(cell)
                }
            }
        }

        val rows = input.size
        val columns = input[0].length
        var validObstructions = 0

        for (i in 0 until rows) {
            for (j in 0 until columns) {
                if (input[i][j] == '.' && (i != startX || j != startY)) {
                    if (isLoop(input, Pair(i, j), startX, startY, startDir)) {
                        validObstructions++
                    }
                }
            }
        }

        return validObstructions
    }

    val testInput = readInput("day06/Day06_test")
    check(part1(testInput) == 41)
    check(part2(testInput) == 6)

    val input = readInput("day06/Day06")
    part1(input).println()
    part2(input).println()
}

private fun isLoop(grid: List<String>, obstruction: Pair<Int, Int>, startX: Int, startY: Int, startDir: Int): Boolean {
    val directions = listOf(Pair(-1, 0), Pair(0, 1), Pair(1, 0), Pair(0, -1))
    val visitedStates = mutableSetOf<Triple<Int, Int, Int>>()
    val rows = grid.size
    val columns = grid[0].length

    var x = startX
    var y = startY
    var dirIndex = startDir

    while (true) {
        if (Triple(x, y, dirIndex) in visitedStates) {
            return true
        }
        visitedStates.add(Triple(x, y, dirIndex))

        val (dx, dy) = directions[dirIndex]
        val nx = x + dx
        val ny = y + dy

        if (nx !in 0 until rows || ny !in 0 until columns) {
            return false
        }

        if (nx == obstruction.first && ny == obstruction.second || grid[nx][ny] == '#') {
            dirIndex = (dirIndex + 1) % 4
        } else {
            x = nx
            y = ny
        }
    }
}
