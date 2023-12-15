package me.duncte123.adventofcode

import me.duncte123.adventofcode.partial.AbstractSolution

class DayNine : AbstractSolution() {
    override fun getTestInput() =
        "" +
                "0 3 6 9 12 15\n" +
                "1 3 6 10 15 21\n" +
                "10 13 16 21 30 45"

    override fun run(input: String): String {
        val histories = input.split("\n")
            .map { it.split(" ") }
            .map { it.map { num -> num.toLong() } }

        val historyValues = mutableListOf<Long>()


        histories.forEach { start ->
            val foundSeqs = mutableListOf(start)
            var currSeq = start
            var nextSeq = mutableListOf<Long>()

            while (true) {
                currSeq.zipWithNext().forEach { (a, b) ->
                    nextSeq.add(b - a)
                }

                println(nextSeq)

                if (nextSeq.all { it == 0L }) {
                    // we've never added the zeroes to the found histories so no need to remove anything.
                    val rev = foundSeqs.reversed()

                    println(rev)

                    var result = 0L

                    rev.forEach {
                        val lastItem = it.first()

                        result = lastItem - result

                        println("$lastItem - $result")
                    }

                    historyValues.add(result)
                    println(result)

                    break
                } else {
                    foundSeqs.add(nextSeq)
                    currSeq = nextSeq
                    nextSeq = mutableListOf()
                }
            }
        }


        return "${historyValues.sum()}"
    }
}