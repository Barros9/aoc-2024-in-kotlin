package day14

import println
import readInput

fun main() {
    fun part1(input: List<String>): Int = calculateSafetyFactor(input, 100, 101, 103)

    val input = readInput("day14/Day14")
    part1(input).println()
}

private fun parseInput(input: List<String>): List<Triple<Pair<Int, Int>, Int, Int>> = input.map { line ->
    val parts = line.split(" ", "p=", ",", "v=", ignoreCase = true)
        .filter { it.isNotEmpty() }
        .map { it.toInt() }
    Triple(parts[0] to parts[1], parts[2], parts[3])
}

private fun calculateFinalPosition(
    p: Pair<Int, Int>,
    v: Pair<Int, Int>,
    time: Int,
    width: Int,
    height: Int
): Pair<Int, Int> {
    val (pX, pY) = p
    val (vX, vY) = v
    val xFinal = ((pX + vX * time) % width + width) % width
    val yFinal = ((pY + vY * time) % height + height) % height
    return xFinal to yFinal
}

private fun classifyInQuadrant(pos: Pair<Int, Int>, width: Int, height: Int): Int? {
    val (x, y) = pos
    val middleX = width / 2
    val middleY = height / 2
    return when {
        x > middleX && y < middleY -> 0
        x < middleX && y < middleY -> 1
        x < middleX && y > middleY -> 2
        x > middleX && y > middleY -> 3
        else -> null
    }
}

private fun calculateSafetyFactor(input: List<String>, time: Int, width: Int, height: Int): Int {
    val robots = parseInput(input)
    val quadrants = IntArray(4)
    robots.forEach { (p, vX, vY) ->
        val finalPos = calculateFinalPosition(p, vX to vY, time, width, height)
        classifyInQuadrant(finalPos, width, height)?.let { quadrants[it]++ }
    }
    return quadrants.reduce { acc, q -> acc * q }
}