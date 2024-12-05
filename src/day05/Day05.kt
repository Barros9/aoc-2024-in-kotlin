package day05

import println
import readInput

fun main() {
    fun part1(input: List<String>): Int {
        val (rules, groups) = getRulesAndGroups(input)
        val correctlyOrderedGroups = groups.filter { group ->
            isCorrectlyOrdered(group, rules)
        }
        return correctlyOrderedGroups.sumOf { findMiddlePage(it) }
    }

    fun part2(input: List<String>): Int {
        val (rules, groups) = getRulesAndGroups(input)
        val incorrectlyOrderedGroups = groups.filter { group ->
            !isCorrectlyOrdered(group, rules)
        }
        val reorderedGroups = incorrectlyOrderedGroups.map { reorderGroup(it, rules) }
        return reorderedGroups.sumOf { findMiddlePage(it) }
    }

    val testInput = readInput("day05/Day05_test")
    check(part1(testInput) == 143)
    check(part2(testInput) == 123)

    val input = readInput("day05/Day05")
    part1(input).println()
    part2(input).println()
}

private fun getRulesAndGroups(input: List<String>): Pair<List<Pair<Int, Int>>, List<List<Int>>> {
    val splitIndex = input.indexOf("")
    val rulesPart = input.subList(0, splitIndex)
    val groupsPart = input.subList(splitIndex + 1, input.size)

    val rules = rulesPart.map { line ->
        val (first, second) = line.split("|").map(String::toInt)
        first to second
    }

    val groups = groupsPart.map { line ->
        line.split(",").map(String::toInt)
    }

    return rules to groups
}

private fun isCorrectlyOrdered(group: List<Int>, rules: List<Pair<Int, Int>>): Boolean {
    val indexMap = group.withIndex().associate { it.value to it.index }
    return rules
        .filter { (first, second) -> first in indexMap && second in indexMap }
        .all { (first, second) -> indexMap[first]!! < indexMap[second]!! }
}

private fun findMiddlePage(group: List<Int>): Int = group[group.size / 2]

private fun reorderGroup(group: List<Int>, rules: List<Pair<Int, Int>>): List<Int> {
    val dependencies = rules.filter { (first, second) ->
        first in group && second in group
    }

    val dependencyMap = mutableMapOf<Int, MutableList<Int>>()
    group.forEach { page -> dependencyMap[page] = mutableListOf() }
    dependencies.forEach { (first, second) -> dependencyMap[second]?.add(first) }

    val result = mutableListOf<Int>()
    val visited = mutableSetOf<Int>()

    fun visit(page: Int) {
        if (page !in visited) {
            visited.add(page)
            dependencyMap[page]?.forEach { visit(it) }
            result.add(page)
        }
    }

    group.forEach { visit(it) }
    return result.reversed()
}