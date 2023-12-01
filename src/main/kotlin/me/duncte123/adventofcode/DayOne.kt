val stripRegex = "[^0-9]".toRegex()

fun getFirstAndLastNumber(line: String) {
    val justNumbers = line.replace(stripRegex, "")
}

fun main() {
    println("Test")
}

