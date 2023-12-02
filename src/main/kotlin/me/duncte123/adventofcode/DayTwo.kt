package me.duncte123.adventofcode

import me.duncte123.adventofcode.partial.AbstractSolution

class DayTwo : AbstractSolution() {
    val totalRedCubes = 12
    val totalGreenCubes = 13
    val totalBlueCubes = 14

    override fun getTestInput() = "Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green\n" +
            "Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue\n" +
            "Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red\n" +
            "Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red\n" +
            "Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green"

    override fun run(input: String): String {
        val lines = input.trim().split("\n")
        val games = lines.map { makeGame(it) }

//        println(games)

        val possibleGames = games.filter {
            it.isGamePossibleWith(totalRedCubes, totalGreenCubes, totalBlueCubes)
        }

//        println(possibleGames)

        val gameIdsSummed = possibleGames.sumOf { it.id }

        val powerGames = games.sumOf { it.getPowerOfGame() }

        return "$gameIdsSummed -> $powerGames"
    }

    private fun makeGame(input: String): GameData {
        val (gameId, cubeInfo) = input.split(":".toRegex(), 2)

        return GameData(
            getIntGameId(gameId),
            parseCubeInfoToList(cubeInfo.trim())
        )
    }

    private fun getIntGameId(rawGameId: String): Int = rawGameId.substring(5).toInt()

    private fun parseCubeInfoToList(cubeInfo: String): List<CubeData> {
        return cubeInfo.split(";")
            .map { it.trim() }
            .map { CubeData.fromHand(it) }
    }
}

data class GameData(val id: Int, val cubeData: List<CubeData>) {
    fun isGamePossibleWith(red: Int, green: Int, blue: Int): Boolean {
        val (maxRed, maxGreen, maxBlue) = getMaxCubeCountInGame()

        return maxRed <= red && maxGreen <= green && maxBlue <= blue
    }

    fun getPowerOfGame(): Int {
        return getMaxCubeCountInGame().getPower()
    }

    private fun getMaxCubeCountInGame(): CubeData {
        val maxRed = cubeData.maxOf { it.red }
        val maxGreen = cubeData.maxOf { it.green }
        val maxBlue = cubeData.maxOf { it.blue }

        return CubeData(maxRed, maxGreen, maxBlue)
    }
}
data class CubeData(val red: Int, val green: Int, val blue: Int) {
    fun getPower(): Int {
        return red * green * blue
    }

    companion object {
        fun fromHand(input: String): CubeData {
            var red = 0
            var blue = 0
            var green = 0

            val parts = input.split(",").map { it.trim() }

            parts.forEach { part ->
                val (amount, colour) = part.split(" ")

                when (colour) {
                    "red" -> {
                        red = amount.toInt()
                    }
                    "green" -> {
                        green = amount.toInt()
                    }
                    "blue" -> {
                        blue = amount.toInt()
                    }
                    else -> throw IllegalArgumentException("Colour $colour not implemented")
                }
            }

            return CubeData(red, green, blue)
        }
    }
}
