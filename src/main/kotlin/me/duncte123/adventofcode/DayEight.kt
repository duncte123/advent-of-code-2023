package me.duncte123.adventofcode

import me.duncte123.adventofcode.partial.AbstractSolution

class DayEight : AbstractSolution() {
    private val directionRegex = "(?<key>[A-Z]{3}) = \\((?<left>[A-Z]{3}), (?<right>[A-Z]{3})\\)".toRegex()

    override fun getTestInput() =
        "RL\n" +
        "\n" +
        "AAA = (BBB, CCC)\n" +
        "BBB = (DDD, EEE)\n" +
        "CCC = (ZZZ, GGG)\n" +
        "DDD = (DDD, DDD)\n" +
        "EEE = (EEE, EEE)\n" +
        "GGG = (GGG, GGG)\n" +
        "ZZZ = (ZZZ, ZZZ)"

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

        var steps = 0
        var curInstruction = "AAA"

        while (true) {
            val instruction = instructions[curInstruction]!!
            val stepToTake = direction[steps % direction.length]

            println(stepToTake)

            steps++

            if (stepToTake == 'L') {
                if (instruction.left == "ZZZ") {
                    break
                }

                curInstruction = instruction.left
            } else {
                if (instruction.right == "ZZZ") {
                    break
                }

                curInstruction = instruction.right
            }
        }

        return "$steps"
    }
}

data class Instruction(val left: String, val right: String)