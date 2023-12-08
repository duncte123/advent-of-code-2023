package me.duncte123.adventofcode

import me.duncte123.adventofcode.partial.AbstractSolution
import java.util.concurrent.CountDownLatch

class DayFive : AbstractSolution() {
    override fun getTestInput() =
        "seeds: 79 14 55 13\n" +
        "\n" +
        "seed-to-soil map:\n" +
        "50 98 2\n" +
        "52 50 48\n" +
        "\n" +
        "soil-to-fertilizer map:\n" +
        "0 15 37\n" +
        "37 52 2\n" +
        "39 0 15\n" +
        "\n" +
        "fertilizer-to-water map:\n" +
        "49 53 8\n" +
        "0 11 42\n" +
        "42 0 7\n" +
        "57 7 4\n" +
        "\n" +
        "water-to-light map:\n" +
        "88 18 7\n" +
        "18 25 70\n" +
        "\n" +
        "light-to-temperature map:\n" +
        "45 77 23\n" +
        "81 45 19\n" +
        "68 64 13\n" +
        "\n" +
        "temperature-to-humidity map:\n" +
        "0 69 1\n" +
        "1 0 69\n" +
        "\n" +
        "humidity-to-location map:\n" +
        "60 56 37\n" +
        "56 93 4"

    override fun run(input: String): String {
        /*val (
            seedsRaw,
            seedToSoil,
            soilToFertilizer,
            fertilizerToWater,
            waterToLight,
            lightToTemperature,
            temperatureToHumidity,
            humidityToLocation
        )*/
        val stages = input.split("\n\n").map { part ->
            part.split(":".toRegex(), 2)[1]
                .split("\n")
                .map { it.trim() }
                .filter { it.isNotBlank() }
        }

        val seedsRaw = stages.removeFirst()

        val seedPairs = seedsRaw.first()
            .split(" ")
            .map { it.toLong() }
            // Comment out the two lines below to switch to part one
            .chunked(2)
            .map { (source, length) ->
                source .. (source + length)
            }

        val latch = CountDownLatch(seedPairs.size)

        println(seedPairs)

        val results = mutableListOf<Long>()
//        val seedToSoil = stages.removeFirst()

        seedPairs.map { stage1 ->
            runOnVThread("RangeThread[$stage1]") {
                // Initial conversion so that we have a list
//                val seedToSoilRange = makeMappingRanges(seedToSoil)

                val stageData = stage1.map {
                    var res = it
                    stages.map { stage ->
                        runOnVThread("StageThread") {
                            val stageMapping = makeMappingRanges(stage)

                            res = getMappedInRange(res, stageMapping)
                            // println("[${Thread.currentThread().name}] Mapped $it to $res")
                        }
                    }.forEach { t -> t.join() }
                    res
                }

                println(stageData)

                results.add(stageData.minOf { it })
                latch.countDown()
            }
        }

        latch.await()

        return "${results.minOf { it }}"
    }

    private fun makeMappingRanges(input: List<String>): List<Pair<LongRange, LongRange>> {
        val ranges = mutableListOf<Pair<LongRange, LongRange>>()

        input.forEach {
            val (destStart, sourceStart, length) = it.split(" ").map { item -> item.toLong() }

            ranges.add(
                (sourceStart .. (sourceStart + length)) to (destStart .. (destStart + length))
            )
        }

        return ranges
    }

    private fun getMappedInRange(input: Long, ranges: List<Pair<LongRange, LongRange>>, result: (Long) -> Unit) = runOnVThread("MappedThread[$input]") {
        val found = getMappedInRange(input, ranges)

        result(found)
    }

    private fun getMappedInRange(input: Long, ranges: List<Pair<LongRange, LongRange>>): Long {
        val foundRanges = ranges.filter { it.first.contains(input) }

        if (foundRanges.isEmpty()) {
            return input
        }

        foundRanges.forEach { (source, dest) ->
            val sourceIndex = source.indexOf(input)

            if (sourceIndex > -1) {
                val found = dest.elementAtOrNull(sourceIndex)

                if (found != null) {
                    return found
                }
            }

        }

        return input
    }

    private fun runOnVThread(name: String, task: () -> Unit) = Thread.ofVirtual().name(name).start(task)

    private operator fun <E> List<E>.component6(): E = this[5]
    private operator fun <E> List<E>.component7(): E = this[6]
    private operator fun <E> List<E>.component8(): E = this[7]
}
