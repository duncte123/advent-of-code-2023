class DayOne {
    val stripRegex = "[^0-9]".toRegex()

    init {
        val allBytes = javaClass.getResourceAsStream("dayOneInput.txt").readAllBytes()
        val inputString = String(allBytes).trim()
//        val inputString = "two1nine\n" +
//                "eightwothree\n" +
//                "abcone2threexyz\n" +
//                "xtwone3four\n" +
//                "4nineeightseven2\n" +
//                "zoneight234\n" +
//                "7pqrstsixteen"
        val inputLines = inputString.split("\n").filter { it.isNotBlank() }
        val theNumbers = inputLines
            .map { fixupCollissions(it) }
            .map { getFirstAndLastNumber(it) }

        println(theNumbers)

        val actualNumbers = theNumbers.map { it.toInt() }

        val summed = actualNumbers.sum()

        println(summed)
    }

    fun fixupCollissions(line: String) = line
        .replace("oneight", "oneeight")
        .replace("threeight", "threeeight")
        .replace("fiveight", "fiveeight")
        .replace("nineight", "nineeight")
        .replace("twone", "twoone")
        .replace("sevenine", "sevennine")
        .replace("eightwo", "eighttwo")

    fun replaceWordsWithNumbers(line: String) = line
        .replace("one", "1")
        .replace("two", "2")
        .replace("three", "3")
        .replace("four", "4")
        .replace("five", "5")
        .replace("six", "6")
        .replace("seven", "7")
        .replace("eight", "8")
        .replace("nine", "9")

    // this is probably not super efficent, but it works
    fun loopCharacters(line: String): String {
        var tmp = ""

        line.toCharArray().forEach {
            tmp = replaceWordsWithNumbers("$tmp$it")
        }

        return tmp
    }

    fun getFirstAndLastNumber(line: String): String {
        val wordsReplaced = loopCharacters(line)
        val justNumbers = wordsReplaced.replace(stripRegex, "").trim()
        val numbers = justNumbers.split("").filter { it.isNotBlank() }
        val firstNumber = numbers[0]

        if (numbers.size == 1) {

            return "$firstNumber$firstNumber"
        }

        val lastNumber = numbers[numbers.size - 1]

        return "$firstNumber$lastNumber"
    }
}

fun main() {
    DayOne()
}

