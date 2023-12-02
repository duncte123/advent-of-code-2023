package me.duncte123.adventofcode.partial

abstract class AbstractSolution {
    fun getInput(): String {
        val clsName = javaClass.simpleName
        val camelCase = clsName[0].lowercase() + clsName.subSequence(1, clsName.length)

        return String(
            javaClass.getResourceAsStream("/${camelCase}Input.txt")?.readAllBytes() ?: byteArrayOf()
        ).trim()
    }

    abstract fun getTestInput(): String

    abstract fun run(input: String): String
}
