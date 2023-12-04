package me.duncte123.adventofcode

import me.duncte123.adventofcode.partial.AbstractSolution

class DayThree : AbstractSolution() {
    override fun getTestInput() = "467..114..\n" +
            "...*......\n" +
            "..35..633.\n" +
            "......#...\n" +
            "617*......\n" +
            ".....+.58.\n" +
            "..592.....\n" +
            "......755.\n" +
            "...\$.*....\n" +
            ".664.598..\n" +
            "..........\n" +
            "...2......\n" + // found a bug, but what else could it fucking be
            "....*...*.\n" +
            ".........1"
//    override fun getTestInput() = "...12\n" +
//        "12*.."

    override fun run(input: String): String {
        val rows = input.trim().split("\n").map { "..$it.." }
        var total = 0

        rows.forEachIndexed { index, row ->
            var strNum = ""

             row.forEachIndexed rowLoop@{ lineIndex, char ->
                 if (char.isDigit()) {
                     strNum += char
                 } else {
                     val (toAdd, success) = checkRow(index, rows, row, strNum)

                     if (success) {
                         total += toAdd
                     }

                     strNum = ""
                 }

                 // If we have a num on the end of the line (eg 132)
                 if (lineIndex == row.length - 1) {
                     val (toAdd, success) = checkRow(index, rows, row, strNum)

                     if (success) {
                         total += toAdd

                         strNum = ""
                     }
                 }
            }
        }

        return "$total (bad nums 516134, 514096, 509814, 524618, 15758)"
    }

    private fun checkRow(index: Int, rows: List<String>, row: String, strNum: String): Pair<Int, Boolean> {
        if (strNum.isBlank()) {
            return 0 to false
        }

        val firstNumIndex = row.indexOf(strNum)

        if (firstNumIndex == -1) {
            println("Could not find $strNum in $row")
            return 0 to false
        }

        var canBeAdded = false

        // normalize the index to be 0 based
        val lastNumIndex = firstNumIndex + strNum.length

        if (index > 0) {
            val rowAbove = rows[index - 1]

            // Right above
            canBeAdded = rowAbove[firstNumIndex + 1] != '.' ||
                    rowAbove[firstNumIndex] != '.' ||
                    rowAbove[firstNumIndex - 1] != '.' ||
                    rowAbove[lastNumIndex + 1] != '.' ||
                    rowAbove[lastNumIndex] != '.' ||
                    rowAbove[lastNumIndex - 1] != '.'
        }

        canBeAdded = canBeAdded || row[firstNumIndex - 1] != '.' || row[lastNumIndex + 1] != '.'

        if (index < rows.size - 1) {
            val rowBelow = rows[index + 1]

            canBeAdded = canBeAdded ||
                    rowBelow[firstNumIndex + 1] != '.' ||
                    rowBelow[firstNumIndex] != '.' ||
                    rowBelow[firstNumIndex - 1] != '.' ||
                    rowBelow[lastNumIndex + 1] != '.' ||
                    rowBelow[lastNumIndex] != '.' ||
                    rowBelow[lastNumIndex - 1] != '.'
        }

        if (canBeAdded) {
            println("${index + 1}> $strNum")

            return strNum.toInt() to true
        }

        return 0 to true
    }
}
