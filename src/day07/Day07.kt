package day07

import println
import readInput
import java.math.BigDecimal

fun main() {
    fun part1(input: List<String>): BigDecimal {
        var sum = BigDecimal.ZERO
        input.forEach { equation ->
            val (total, numbers) = parseEquation(equation)
            if (canMatchResult(total, numbers)) sum = sum.add(total)
        }
        return sum
    }

    fun part2(input: List<String>): BigDecimal {
        return BigDecimal.ZERO
    }

    val testInput = readInput("day07/Day07_test")
    check(part1(testInput) == BigDecimal("3749"))
    check(part2(testInput) == BigDecimal.ZERO)

    val input = readInput("day07/Day07")
    part1(input).println()
    part2(input).println()
}

private fun parseEquation(equation: String): Pair<BigDecimal, List<BigDecimal>> {
    val parts = equation.split(":")
    val total = parts[0].trim().toBigDecimal()
    val numbers = parts[1].trim().split(" ").map { it.toBigDecimal() }
    return total to numbers
}

private fun canMatchResult(total: BigDecimal, numbers: List<BigDecimal>): Boolean {
    val operatorCount = numbers.size - 1
    val operators = listOf('+', '*')

    val allOperatorCombinations = generateOperatorCombinations(operators, operatorCount)

    for (operatorCombination in allOperatorCombinations) {
        if (evaluateExpression(numbers, operatorCombination) == total) {
            return true
        }
    }
    return false
}

private fun generateOperatorCombinations(operators: List<Char>, count: Int): List<List<Char>> {
    if (count == 0) return listOf(emptyList())

    val smallerCombinations = generateOperatorCombinations(operators, count - 1)
    return smallerCombinations.flatMap { combination ->
        operators.map { operator -> combination + operator }
    }
}

private fun evaluateExpression(numbers: List<BigDecimal>, operators: List<Char>): BigDecimal {
    var result = numbers[0]
    for (i in operators.indices) {
        val nextNumber = numbers[i + 1]
        result = when (operators[i]) {
            '+' -> result.add(nextNumber)
            '*' -> result.multiply(nextNumber)
            else -> throw IllegalArgumentException("Unsupported operator: ${operators[i]}")
        }
    }
    return result
}