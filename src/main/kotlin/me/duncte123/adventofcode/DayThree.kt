package me.duncte123.adventofcode

import me.duncte123.adventofcode.partial.AbstractSolution

class DayThree : AbstractSolution() {
    private val numberLocations = mutableMapOf<NumberLoc, Int>()
    override fun getTestInput() =
            "467..114..\n" +
            "...*......\n" +
            "..35..633.\n" +
            "......#...\n" +
            "617*......\n" +
            ".....+.58.\n" +
            "..592.....\n" +
            "......755.\n" +
           "...\$.*....\n" +
            ".664.598.."

    override fun run(input: String): String {
        val rows = input.trim().split("\n")
            // Padding out the strings on both sides, don't feel like doing bounds checks
            .map { "..$it.." }
        var total = 0
        val gearCoords = mutableMapOf<Coord, Char>()

        rows.forEachIndexed { yCoord, row ->
            var strNum = ""
            var startIndex = 0

            row.forEachIndexed rowLoop@{ xCoord, char ->
                if (char == '*') {
                    gearCoords[Coord(xCoord, yCoord)] = char
                }

                if (char.isDigit()) {
                    if (strNum.isBlank()) {
                        startIndex = xCoord
                    }

                    strNum += char
                } else {
                    val (toAdd, success) = checkRow(yCoord, rows, row, strNum, startIndex)

                    if (success) {
                        total += toAdd
                    }

                    strNum = ""
                }

                // If we have a num on the end of the line (eg 132)
                if (xCoord == row.length - 1) {
                    val (toAdd, success) = checkRow(yCoord, rows, row, strNum, startIndex)

                    if (success) {
                        total += toAdd
                    }
                }
            }
        }

        val gearRatio = calculateGearRatio(gearCoords, rows)

        // Correct possible answer is 517021, but not testing that until it gives me the result.
        val badNums = listOf(589770, 524618, 516134, 514096, 509814, 471817, 15758)
        val yellMsg = if (total in badNums) " (you've had this answer before, it was wrong)" else ""

        return "$gearRatio - $total$yellMsg"
    }

    private fun checkRow(yCoord: Int, rows: List<String>, row: String, strNum: String, startIndex: Int = 0): Pair<Int, Boolean> {
        if (strNum.isBlank()) {
            return 0 to false
        }

        val firstNumIndex = row.indexOf(strNum, startIndex)

        if (firstNumIndex == -1) {
            println("Could not find $strNum in $row")
            return 0 to false
        }

        var isPart = false

        // normalize the index to be 0 based
        val lastNumIndex = firstNumIndex + strNum.length - 1

        if (yCoord > 0) {
            val rowAbove = rows[yCoord - 1]

            // Right above
            isPart = checkSurroundingPartPosibility(rowAbove, firstNumIndex, lastNumIndex)
        }

        numberLocations[NumberLoc(
            start = Coord(firstNumIndex, yCoord),
            end = Coord(lastNumIndex, yCoord)
        )]

        println("${yCoord + 1}#$firstNumIndex> $strNum")

        isPart = isPart || canBePart(row[firstNumIndex - 1]) || canBePart(row[lastNumIndex + 1])
//        isPart = isPart || checkSurrounding(row, firstNumIndex, lastNumIndex)

        if (yCoord < rows.size - 1) {
            val rowBelow = rows[yCoord + 1]

            isPart = isPart || checkSurroundingPartPosibility(rowBelow, firstNumIndex, lastNumIndex)
        }

        if (isPart) {
            println("(found part) ${yCoord + 1}#$firstNumIndex> $strNum")
            return strNum.toInt() to true
        }

        return 0 to true
    }

    private fun checkSurroundingPartPosibility(row: String, firstIndex: Int, lastIndex: Int): Boolean {
        println("${
            row[firstIndex - 1]
        }${
            row[firstIndex]
        }${
            row[firstIndex + 1]
        }${
            row[lastIndex - 1]
        }${
            row[lastIndex]
        }${
            row[lastIndex + 1]
        }")
        return canBePart(row[firstIndex - 1]) ||
                canBePart(row[firstIndex]) ||
                canBePart(row[firstIndex + 1]) ||
                canBePart(row[lastIndex - 1]) ||
                canBePart(row[lastIndex]) ||
                canBePart(row[lastIndex + 1])
    }

    private fun calculateGearRatio(gearCoords: Map<Coord, Char>, rows: List<String>): Int {
        val result = 0

        gearCoords.forEach { (coord, ) ->
            val isConsideredForRatio = checkSurroundingForDigit(coord, rows)

            if (isConsideredForRatio) {
                val numTopLeft = numberLocations[NumberLoc.fromNormalCoord(
                    coord = coord,
                    addStartX = 1,
                    addStartY = 1,
                )]

                println(numTopLeft)
            }
        }

        return result
    }

    // TODO: check left and right of gear
    private fun checkSurroundingForDigit(gearLoc: Coord, rows: List<String>): Boolean {
        val topLeft = rows[gearLoc.y - 1][gearLoc.x - 1]
        val topCenter = rows[gearLoc.y - 1][gearLoc.x]
        val topRight = rows[gearLoc.y - 1][gearLoc.x + 1]

        val bottomLeft = rows[gearLoc.y + 1][gearLoc.x - 1]
        val bottomCenter = rows[gearLoc.y + 1][gearLoc.x]
        val bottomRight = rows[gearLoc.y + 1][gearLoc.x + 1]

        return (
                    topLeft.isDigit() ||
                    topCenter.isDigit() ||
                    topRight.isDigit()
                )
                &&
                (
                    bottomLeft.isDigit() ||
                    bottomCenter.isDigit() ||
                    bottomRight.isDigit()
                )
    }

    private fun canBePart(item: Char): Boolean {
        return item != '.'
    }
}

data class Coord(val x: Int, val y: Int)
data class NumberLoc(val start: Coord, val end: Coord) {
    companion object {
        fun fromNormalCoord(coord: Coord, addStartX: Int = 0, addStartY: Int = 0, addEndX: Int = 0, addEndY: Int = 0): NumberLoc {
            return NumberLoc(
                Coord(
                    coord.x + addStartX,
                    coord.y + addStartY,
                ),
                Coord(
                    coord.x + addEndX,
                    coord.y + addEndY,
                )
            )
        }
    }
}
