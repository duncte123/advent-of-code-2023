package me.duncte123.adventofcode

import me.duncte123.adventofcode.partial.AbstractSolution
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors

class DayEight : AbstractSolution() {
    private val directionRegex = "(?<key>\\w{3}) = \\((?<left>\\w{3}), (?<right>\\w{3})\\)".toRegex()

    override fun getTestInput() =
        "LR\n" +
        "\n" +
        "11A = (11B, XXX)\n" +
        "11B = (XXX, 11Z)\n" +
        "11Z = (11B, XXX)\n" +
        "22A = (22B, XXX)\n" +
        "22B = (22C, 22C)\n" +
        "22C = (22Z, 22Z)\n" +
        "22Z = (22B, 22B)\n" +
        "XXX = (XXX, XXX)"

    private val executor = Executors.newVirtualThreadPerTaskExecutor()

    override fun run(input: String): String {
        val instructions = mutableMapOf<String, Instruction>()

        val (direction, maps) = input.split("\n\n").map { it.trim() }.filter { it.isNotBlank() }

        maps.split("\n").forEach { map ->
            val result = directionRegex.matchEntire(map)

            if (result != null) {
                val groups = result.groups
                val key = groups[1]!!.value
                val left = groups[2]!!.value
                val right = groups[3]!!.value

                instructions[key] = Instruction(left, right)
            }
        }

        println(direction)
        println(instructions)

        val endsInA = instructions.keys.filter { it.endsWith('A') }
        val theirInstructions = endsInA.toMutableList()
        val cycles = mutableListOf<Long>()
        val latch = CountDownLatch(endsInA.size)

        endsInA.forEachIndexed { index, _ ->
            var steps = 0L

            executor.execute {
                while (true) {
                    val stepToTake = direction[(steps % direction.length).toInt()]

                    steps++

                    val instruction = instructions[theirInstructions[index]]!!

                    println(instruction)

                    if (stepToTake == 'L') {
                        theirInstructions[index] = instruction.left
                    } else {
                        theirInstructions[index] = instruction.right
                    }

                    if (theirInstructions[index].endsWith('Z')) {
                        cycles.add(steps)
                        latch.countDown()
                        break
                    }
                }
            }
        }

        latch.await()

        println(cycles)
        println(lcm(cycles))

        val wrongAnswers = listOf(440117549617L, 12361L)

        val result = lcm(cycles)

        return "$result (is wrong: ${if (result in wrongAnswers) "Yes" else "No"})"
    }

    private fun gcd(a: Long, b: Long): Long {
        var tmpA = a
        var tmpB = b

        while (tmpB > 0) {
            val tmp = tmpB
            tmpB = tmpA % tmpB
            tmpA = tmp
        }

        return tmpA
    }

    private fun lcm(a: Long, b: Long): Long {
        return a * (b / gcd(a, b))
    }

    private fun lcm(items: List<Long>): Long {
        val itemCopy = items.toList()
        var result = itemCopy.removeFirst()

        itemCopy.forEach {
            result = lcm(result, it)
        }

        return result
    }
}

data class Instruction(val left: String, val right: String)