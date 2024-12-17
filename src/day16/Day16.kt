package day16

import println
import readInput
import java.util.*

fun main() {
    fun part1(input: List<String>): Int {
        val (start, end, maze) = parseMaze(input)

        val queue = PriorityQueue<State> { a, b -> a.score - b.score }
        val visited = mutableMapOf<Triple<Int, Int, Int>, Int>()

        queue.add(State(start.first, start.second, 1, 0))
        visited[Triple(start.first, start.second, 1)] = 0

        return bfs(maze, queue, visited, end)
    }

    fun part2(input: List<String>): Int {
        val (start, end, maze) = parseMaze(input)

        val queue = PriorityQueue<State> { a, b -> a.score - b.score }
        val visited = mutableMapOf<Triple<Int, Int, Int>, Int>()
        val bestPaths = mutableSetOf<Pair<Int, Int>>()

        queue.add(State(start.first, start.second, 1, 0))
        visited[Triple(start.first, start.second, 1)] = 0

        val minScore = bfs(maze, queue, visited, end)

        for (entry in visited) {
            val (key, score) = entry
            if (score == minScore) {
                val (x, y, _) = key
                bestPaths.add(Pair(x, y))
            }
        }

        return bestPaths.size
    }

    val testInput = readInput("day16/Day16_test")
    check(part1(testInput) == 7036)

    val input = readInput("day16/Day16")
    part1(input).println()
    part2(input).println()
}

private data class State(val x: Int, val y: Int, val direction: Int, val score: Int)

private fun parseMaze(input: List<String>): Triple<Pair<Int, Int>, Pair<Int, Int>, MutableList<MutableList<Char>>> {
    var start = Pair(0, 0)
    var end = Pair(0, 0)

    val maze = mutableListOf<MutableList<Char>>()
    for (i in input.indices) {
        val row = input[i].toCharArray().toMutableList()
        for (j in row.indices) {
            if (row[j] == 'S') start = Pair(i, j)
            if (row[j] == 'E') end = Pair(i, j)
        }
        maze.add(row)
    }
    return Triple(start, end, maze)
}

private fun bfs(
    maze: MutableList<MutableList<Char>>,
    queue: PriorityQueue<State>,
    visited: MutableMap<Triple<Int, Int, Int>, Int>,
    end: Pair<Int, Int>
): Int {
    while (queue.isNotEmpty()) {
        val (x, y, direction, score) = queue.poll()

        if (Pair(x, y) == end) return score

        for (i in directions.indices) {
            val (dx, dy) = directions[i]
            val nx = x + dx
            val ny = y + dy

            if (nx in maze.indices && ny in maze[0].indices && maze[nx][ny] != '#') {
                val newScore = if (i == direction) score + 1 else score + 1000 + 1
                val newState = State(nx, ny, i, newScore)

                val currentBestScore = visited[Triple(nx, ny, i)]
                if (currentBestScore == null || newScore < currentBestScore) {
                    visited[Triple(nx, ny, i)] = newScore
                    queue.add(newState)
                }
            }
        }
    }
    return -1
}

private val directions = listOf(
    -1 to 0,
    0 to 1,
    1 to 0,
    0 to -1
)