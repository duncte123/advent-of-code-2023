package me.duncte123.adventofcode

import me.duncte123.adventofcode.partial.AbstractSolution

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
        val theirInstructions = endsInA.toMutableList()
        var steps = 0L

        while (true) {
            val stepToTake = direction[(steps % direction.length).toInt()]

            steps++

            println(stepToTake)

            endsInA.forEachIndexed { keyId, _ ->
                val instruction = instructions[theirInstructions[keyId]]!!

                if (stepToTake == 'L') {
                    theirInstructions[keyId] = instruction.left
                } else {
                    theirInstructions[keyId] = instruction.right
                }
            }

            if (theirInstructions.all { it.endsWith('Z') }) {
                // LCM time :/
                break
            }
        }

        return "$steps (12361)"
    }
}

data class Instruction(val left: String, val right: String)