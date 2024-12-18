package day15

import readInput

fun main() {
    fun part1(input: List<String>): Int {
        val (warehouse, robot, moves) = parseInput(input)
        return solve(warehouse, robot, moves)
    }

    fun part2(input: List<String>): Int = 0

    val testInput = readInput("day15/Day15_test")
    check(part1(testInput) == 2028)
    check(part2(testInput) == 0)

    val input = readInput("day15/Day15")
    println(part1(input))
}

private fun parseInput(input: List<String>): Triple<MutableMap<Index, Char>, Index, String> {
    val lines = input.iterator()
    val warehouse = mutableMapOf<Index, Char>()
    var robot = Index(0, 0)
    for ((y, line) in lines.withIndex()) {
        if (line.isEmpty()) break
        for (x in line.indices) when (val c = line[x]) {
            '#', 'O' -> warehouse[Index(x, y)] = c
            '@' -> robot = Index(x, y)
        }
    }
    val moves = buildString { lines.forEach(::append) }

    return Triple(warehouse, robot, moves)
}

private fun solve(wh: Map<Index, Char>, robot: Index, moves: String): Int {
    val warehouse = wh.toMutableMap()

    fun move(start: Index, direction: Index): Boolean {
        fun collectDeps(box: Index, map: MutableMap<Index, Char>) {
            val parts = buildList {
                add(box)
                if (warehouse[box] == '[') add(box + Directions.E)
                if (warehouse[box] == ']') add(box + Directions.W)
            }
            val deps = parts.mapNotNull { part -> (part + direction).takeIf { warehouse[it]?.isBox() == true } }
            for (part in parts) map[part] = warehouse.getValue(part)
            for (dep in deps) if (dep !in map) collectDeps(dep, map)
        }

        if (start !in warehouse) return true
        if (warehouse[start] == '#') return false
        val deps = buildMap { collectDeps(start, this) }
        for ((part) in deps) if (warehouse[part + direction] == '#') return false
        deps.entries.sortedBy { (k, _) -> k.y * -direction.y + k.x * -direction.x }.forEach { (part, value) ->
            warehouse[part + direction] = value
            warehouse.remove(part)
        }
        return true
    }

    moves.fold(robot) { current, move ->
        val direction = when (move) {
            '^' -> Directions.N
            '>' -> Directions.E
            'v' -> Directions.S
            else -> Directions.W
        }
        val next = current + direction
        if (move(next, direction)) next else current
    }

    var sum = 0
    for ((k, v) in warehouse) if (v.isBox(includeClose = false)) sum += 100 * k.y + k.x

    return sum
}

private fun Char.isBox(includeClose: Boolean = true) = this == 'O' || this == '[' || (includeClose && this == ']')

private data class Index(val x: Int, val y: Int) {
    operator fun plus(other: Index): Index {
        return Index(this.x + other.x, this.y + other.y)
    }
}

private object Directions {
    val N = Index(0, -1)
    val E = Index(1, 0)
    val S = Index(0, 1)
    val W = Index(-1, 0)
}

