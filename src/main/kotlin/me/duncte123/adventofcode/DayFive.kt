package me.duncte123.adventofcode

import me.duncte123.adventofcode.partial.AbstractSolution

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

        var seedPairs = seedsRaw.first()
            .split(" ")
            .map { it.toLong() }
            .chunked(2)
            .map { (source, length) ->
                source to length
            }
            .toMutableList()

        // My original solution took more than 48 hours to run, I never let it finish because the power supply for my laptop was unable to keep it alive.
        // So I'm taking inspiration from https://github.com/EdwardvanLieshout/AdventOfCode2023/blob/master/challenge5b.ts instead
        stages.forEach { stage ->
            val tempSeedRanges = mutableListOf<Pair<Long, Long>>()
            stage.map { it.toRangeInput() }
                .forEach { (destStart, sourceStart, rLen) ->
                    // Not using forEach here to get around my good friend ConcurrentModificationException
                    for (index in 0 ..< seedPairs.size) {
                        val (seed, sLen) = seedPairs[index]

                        if (seed >= sourceStart && seed + sLen <= sourceStart + rLen) {
                            tempSeedRanges.add(destStart + seed - sourceStart to sLen)
                            seedPairs[index] = -1L to -1L
                        }

                        if (
                            seed >= sourceStart &&
                            seed < sourceStart + rLen &&
                            seed + sLen > sourceStart + rLen &&
                            seed != -1L
                        ) {
                            val start = seed
                            val end = sourceStart + rLen
                            val finalSeed = seed + sLen

                            tempSeedRanges.add(destStart + start - sourceStart to end - start)
                            seedPairs.add(end to finalSeed - end)
                            seedPairs[index] = -1L to -1L
                        }

                        if (
                            seed < sourceStart &&
                            seed + sLen > sourceStart &&
                            seed + sLen <= sourceStart + rLen &&
                            seed != -1L
                        ) {
                            val start = sourceStart
                            val end = seed + sLen
                            val begin = seed

                            // You know, this first equation is not making a whole lot of sense to me
                            tempSeedRanges.add(destStart + start - sourceStart to end - start)
                            seedPairs.add(begin to sourceStart - begin)
                            seedPairs[index] = -1L to -1L
                        }

                        if (seed < sourceStart && seed + sLen >= sourceStart + rLen && seed != -1L) {
                            val start = sourceStart
                            val end = sourceStart + rLen
                            val begin = seed
                            val finalSeed = seed + sLen

                            tempSeedRanges.add(destStart + start - sourceStart to end - start)
                            seedPairs.add(end to finalSeed - end)
                            seedPairs.add(begin to sourceStart - begin)
                            seedPairs[index] = -1L to -1L
                        }
                    }
                }

            seedPairs = seedPairs.filter { it.first > 0 }.toMutableList()
            seedPairs.addAll(tempSeedRanges)

            println(seedPairs)
        }

        val result = seedPairs.minBy { it.first }.first

        println(seedPairs)

        val incorrect = listOf(1549152L, 0L) // Highly doubt 0 is correct LMAO

        return "$result (Incorrect: ${incorrect.contains(result)})"
    }

    private fun String.toRangeInput(): Range {
        val (dest, source, len) = this.split(" ").map { item -> item.toLong() }

        return Range(dest, source, len)
    }

    private operator fun <E> List<E>.component6(): E = this[5]
    private operator fun <E> List<E>.component7(): E = this[6]
    private operator fun <E> List<E>.component8(): E = this[7]
}

data class Range(val destStart: Long, val sourceStart: Long, val length: Long) {}
