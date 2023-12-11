package me.duncte123.adventofcode

import me.duncte123.adventofcode.partial.AbstractSolution

class DaySix : AbstractSolution() {
    override fun getTestInput() =
        "Time:      7  15   30\n" +
        "Distance:  9  40  200"

    override fun run(input: String): String {
        val (raceDuration, recordDistance) = input.split("\n")
                .map {
                    val a = it.split("\\s+".toRegex()).toMutableList()
                    a.removeFirst()
                    a.joinToString("").toLong()
                }

        val winsPerRace = mutableListOf<Int>()

        val maxHoldToWin = mutableListOf<Long>()

        (0 .. raceDuration).forEach { heltDownTime ->
            val remainingTime = raceDuration - heltDownTime
            val boatDistance = heltDownTime * remainingTime

            if (boatDistance > recordDistance) {
                maxHoldToWin.add(heltDownTime)
            }
        }

        winsPerRace.add(maxHoldToWin.size)

        println(winsPerRace)

        // (race time - helt down time) * remaining time = boat travel distance?

        return "${winsPerRace.reduce { a, b -> a * b }}"
    }
}