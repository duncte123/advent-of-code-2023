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

    // source -> destination
    private val seedToSoilMap = hashMapOf<Long, Long>()
    private val soilToFertilizerMap = hashMapOf<Long, Long>()
    private val fertilizerToWaterMap = hashMapOf<Long, Long>()
    private val waterToLightMap = hashMapOf<Long, Long>()
    private val lightToTemperatureMap = hashMapOf<Long, Long>()
    private val temperatureToHumidityMap = hashMapOf<Long, Long>()
    private val humidityToLocationMap = hashMapOf<Long, Long>()

    override fun run(input: String): String {
        val (
            seedsRaw,
            seedToSoil,
            soilToFertilizer,
            fertilizerToWater,
            waterToLight,
            lightToTemperature,
            temperatureToHumidity,
            humidityToLocation
        ) = input.split("\n\n").map { part ->
            part.split(":".toRegex(), 2)[1]
                .split("\n")
                .map { it.trim() }
                .filter { it.isNotBlank() }
        }

        val tempMap = hashMapOf<Long, Long>()

        makeMapping(seedToSoil, tempMap)

        val stage2 = seedsRaw.first().split(" ")
            .map { it.toLong() }
            .map { tempMap.getMappedSource(it) }

        makeMapping(soilToFertilizer, tempMap)

        val stage3 = stage2.map { tempMap.getMappedSource(it) }

        makeMapping(fertilizerToWater, tempMap)

        val stage4 = stage3.map { tempMap.getMappedSource(it) }

        makeMapping(waterToLight, tempMap)

        val stage5 = stage4.map { tempMap.getMappedSource(it) }

        makeMapping(lightToTemperature, tempMap)

        val stage6 = stage5.map { tempMap.getMappedSource(it) }

        makeMapping(temperatureToHumidity, tempMap)

        val stage7 = stage6.map { tempMap.getMappedSource(it) }

        makeMapping(humidityToLocation, tempMap)

        val stage8 = stage7.map { tempMap.getMappedSource(it) }


        val resultingLocations = mutableListOf<Long>()
        /*val seeds = seedsRaw.first().split(" ").map { it.toLong() }

        seeds.forEach { seed ->
            val soil = seedToSoilMap.getMappedSource(seed)
            val fertilizer = soilToFertilizerMap.getMappedSource(soil)
            val water = fertilizerToWaterMap.getMappedSource(fertilizer)
            val light = waterToLightMap.getMappedSource(water)
            val temp = lightToTemperatureMap.getMappedSource(light)
            val humidity = temperatureToHumidityMap.getMappedSource(temp)
            val location = humidityToLocationMap.getMappedSource(humidity)

            resultingLocations.add(location)
        }*/

        return "${stage8.minOf { it }}"
    }

    private fun makeMapping(input: List<String>, targetMap: MutableMap<Long, Long>) {
        targetMap.clear()
        input.forEach {
            val (destStart, sourceStart, length) = it.split(" ").map { item -> item.toLong() }

            for (i in 0 .. length) {
                targetMap[sourceStart + i] = destStart + i
            }
        }
    }

    private operator fun <E> List<E>.component6(): E = this[5]
    private operator fun <E> List<E>.component7(): E = this[6]
    private operator fun <E> List<E>.component8(): E = this[7]

    private fun Map<Long, Long>.getMappedSource(part: Long) = this[part] ?: part
}
