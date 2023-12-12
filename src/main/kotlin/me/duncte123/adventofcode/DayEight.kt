package me.duncte123.adventofcode

import me.duncte123.adventofcode.partial.AbstractSolution
import java.util.concurrent.CountDownLatch

class DayEight : AbstractSolution() {
    private val directionRegex = "(?<key>[A-Z0-9]{3}) = \\((?<left>[A-Z0-9]{3}), (?<right>[A-Z0-9]{3})\\)".toRegex()

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
        val latch = CountDownLatch(endsInA.size)
        val steps = 0

        endsInA.forEach { startKey ->
            followPath(startKey, direction, instructions) {
                println("Steps: $it")
                latch.countDown()
            }
        }

        latch.await()

        return "$steps"
    }

    private fun followPath(startKey: String, direction: String, instructions: Map<String, Instruction>, stepsCallback: (Int) -> Unit) = runOnVirtual(startKey) {
        var steps = 0
        var curInstruction = startKey

        while (true) {
            val instruction = instructions[curInstruction]!!
            val stepToTake = direction[steps % direction.length]

            println(stepToTake)

            steps++

            if (stepToTake == 'L') {
                if (instruction.left.endsWith('Z')) {
                    stepsCallback(steps)
                    break
                }

                curInstruction = instruction.left
            } else {
                if (instruction.right.endsWith('Z')) {
                    stepsCallback(steps)
                    break
                }

                curInstruction = instruction.right
            }
        }
    }

    private fun runOnVirtual(start: String, task: () -> Unit) = Thread.ofVirtual().name("SearchTask[$start]").start(task)
}

data class Instruction(val left: String, val right: String)