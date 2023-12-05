package me.duncte123.adventofcode

import me.duncte123.adventofcode.partial.AbstractSolution

class DayThree : AbstractSolution() {
    override fun getTestInput() = "467..114..\n" +
            "...*......\n" +
            "..35..633.\n" +
            "......#...\n" +
            "617*......\n" +
            "..=....58.\n" +
            "..592.....\n" +
            "......755.\n" +
           "...\$.*....\n" +
            ".664.598..\n"
//            "..........\n" +
//            "...2......\n" + // found a bug, but what else could it fucking be
//            "....*...*.\n" +
//            ".........1"
//    override fun getTestInput() = "...12\n" +
//        "12*.."

    override fun run(input: String): String {
        val rows = input.trim().split("\n")
            // Padding out the strings on both sides, don't feel like doing bounds checks
            .map { "..$it.." }
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
                    }
                }
            }
        }

        // Correct possible answer is 517021, but not testing that until it gives me the result.
        val badNums = listOf(589770, 524618, 516134, 514096, 509814, 471817, 15758)
        val yellMsg = if (total in badNums) " (you've had this answer before, it was wrong)" else ""

        return "$total$yellMsg"
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

        var isPart = false

        // normalize the index to be 0 based
        val lastNumIndex = firstNumIndex + strNum.length - 1

        if (index > 0) {
            val rowAbove = rows[index - 1]

            // Right above
            isPart = checkSurrounding(rowAbove, firstNumIndex, lastNumIndex)
        }

        println("${index + 1}#$firstNumIndex> $strNum")

        isPart = isPart || canBePart(row[firstNumIndex - 1]) || canBePart(row[lastNumIndex + 1])
//        isPart = isPart || checkSurrounding(row, firstNumIndex, lastNumIndex)

        if (index < rows.size - 1) {
            val rowBelow = rows[index + 1]

            isPart = isPart || checkSurrounding(rowBelow, firstNumIndex, lastNumIndex)
        }

        if (isPart) {
            println("(found part) ${index + 1}#$firstNumIndex> $strNum")
            return strNum.toInt() to true
        }

        return 0 to true
    }

    private fun checkSurrounding(row: String, firstIndex: Int, lastIndex: Int): Boolean {
        println("${
            row[firstIndex + 1]
        }${
            row[firstIndex]
        }${
            row[firstIndex - 1]
        }${
            row[lastIndex + 1]
        }${
            row[lastIndex]
        }${
            row[lastIndex - 1]
        }")
        return canBePart(row[firstIndex + 1]) ||
                canBePart(row[firstIndex]) ||
                canBePart(row[firstIndex - 1]) ||
                canBePart(row[lastIndex + 1]) ||
                canBePart(row[lastIndex]) ||
                canBePart(row[lastIndex - 1])
    }

    private fun canBePart(item: Char): Boolean {
        return item != '.'
    }
}
