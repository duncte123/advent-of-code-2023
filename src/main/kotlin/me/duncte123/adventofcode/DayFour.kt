package me.duncte123.adventofcode

import me.duncte123.adventofcode.partial.AbstractSolution
import kotlin.math.min

class DayFour : AbstractSolution() {
    override fun getTestInput() =
        "Card   1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53\n" +
        "Card   2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19\n" +
        "Card   3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1\n" +
        "Card   4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83\n" +
        "Card   5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36\n" +
        "Card   6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11"

    override fun run(input: String): String {
        val cards = input.trim().split("\n").toMutableList()
        var totalPoints = 0
        var cardIndex = 0

        // Could I do this more efficient? Probably
        // Do I care? Not really
        while (true) {
            if (cardIndex >= cards.size) {
                break;
            }

            val card = cards[cardIndex]

            val (_, numbers) = card.split(":".toRegex(), 2)
            val cardId = getCardIdFromRow(card)
            val (winningNumbersRaw, myNumbersRaw) = numbers.split("\\|".toRegex(), 2).map { it.trim() }
            val winningNumbers = winningNumbersRaw.split(" ").filter { it.isNotBlank() }.map { it.toInt() }
            val myNumbers = myNumbersRaw.split(" ").filter { it.isNotBlank() }.map { it.toInt() }
            val myWinningNumbers = myNumbers.filter { winningNumbers.contains(it) }

            if (myWinningNumbers.isNotEmpty()) {
                var totalWon = 1
                val startRange = cardId + 1
                val copyNum = min((cardId + myWinningNumbers.size), cards.size)

                myWinningNumbers.removeFirst()

                myWinningNumbers.forEach {
                    totalWon *= 2
                }

                totalPoints += totalWon

                (startRange..copyNum).forEach {
                    addCardCopy(it, cards)
                }
            }

            cardIndex++
        }

        val part2 = cards.size

        return "$totalPoints - $part2"
    }

    private fun addCardCopy(cardId: Int, cards: MutableList<String>) {
        val cardToCopyIndex = cards.binarySearch {
            getCardIdFromRow(it).compareTo(cardId)
        }

        val clone = cards[cardToCopyIndex]

        cards.add(cardToCopyIndex + 1, clone)
    }

    private fun getCardIdFromRow(row: String): Int {
        val (cardJunk, _) = row.split(":".toRegex(), 2)
        val (_, cardIdStr) = cardJunk.split(" ").filter { it.isNotBlank() }

        return cardIdStr.toInt()
    }
}