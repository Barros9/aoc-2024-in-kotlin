package day14

import println
import readInput
import kotlin.math.pow

fun main() {
    fun part1(input: List<String>): Int = calculateSafetyFactor(input, 100, 101, 103)

    // https://www.reddit.com/r/adventofcode/comments/1hdvhvu/comment/m1zws1g/
    // Amazing solution with Chinese Remainder Theorem
    fun part2(input: List<String>): Int = findEasterEgg(input, 101, 103)

    val input = readInput("day14/Day14")
    part1(input).println()
    part2(input).println()
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

private fun variance(values: List<Int>): Double {
    val mean = values.average()
    return values.map { (it - mean).pow(2) }.average()
}

private fun simulate(
    robots: List<Triple<Pair<Int, Int>, Int, Int>>,
    time: Int,
    width: Int,
    height: Int
): List<Pair<Int, Int>> {
    return robots.map { (p, vX, vY) ->
        calculateFinalPosition(p, vX to vY, time, width, height)
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

private fun modInverse(a: Int, m: Int): Int {
    var y = 0
    var x = 1
    var a1 = a
    var m1 = m

    while (a1 > 1) {
        val q = a1 / m1
        var t = m1
        m1 = a1 % m1
        a1 = t
        t = y
        y = x - q * y
        x = t
    }

    if (x < 0) x += m
    return x
}

private fun findEasterEgg(input: List<String>, width: Int, height: Int): Int {
    val robots = parseInput(input)

    var bx = 0
    var bxVar = Double.MAX_VALUE
    var by = 0
    var byVar = Double.MAX_VALUE

    for (t in 0 until maxOf(width, height)) {
        val positions = simulate(robots, t, width, height)

        val xs = positions.map { it.first }
        val ys = positions.map { it.second }

        val xVar = variance(xs)
        val yVar = variance(ys)

        if (xVar < bxVar) {
            bx = t
            bxVar = xVar
        }

        if (yVar < byVar) {
            by = t
            byVar = yVar
        }
    }

    val inverseW = modInverse(width, height)
    val k = (by - bx) * inverseW % height
    val finalTime = bx + k * width

    return finalTime
}
