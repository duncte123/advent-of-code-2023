package me.duncte123.adventofcode

import me.duncte123.adventofcode.partial.AbstractSolution

class DaySeven : AbstractSolution() {
    // cards -> bid
    override fun getTestInput() =
        "32T3K 765\n" +
        "T55J5 684\n" +
        "KK677 28\n" +
        "KTJJT 220\n" +
        "QQQJA 483"

    // Note to self: you just add the jokers to the cards you have the most of
    override fun run(input: String): String {
        println("TTTAJ".isFullHouse())

        val hands = input.split("\n").map {
            val (cards, bid) = it.split(" ")

            cards to bid.toInt()
        }

        println(hands)

        val sortedHands = hands.sortedWith(this::compareHands)

        println(sortedHands)

        var result = 0L

        sortedHands.forEachIndexed { i, (_, points) ->
            result += points * (i + 1)
        }

        val wrong = listOf(250497043L, 250761535L, 251269448L, 251345551L, 251818476L, 251742373L)

        return "$result ${if (result in wrong) "(wrong answer)" else ""}"
    }

    // TODO: refactor do to this smarter, call compareHands as little as possible
    private fun compareHands(a: Pair<String, Int>, b: Pair<String, Int>): Int {
        val (hand1, _) = a
        val (hand2, _) = b

        val hand1Five = hand1.isFiveOfAKind()
        val hand2Five = hand2.isFiveOfAKind()

        if (hand1Five.isValid) {
            if (hand2Five.isValid) {
                return compareHands(hand1, hand2)
            }

            return 1
        }

        // if we get here, we already know that hand 2 is not 5 of a kind
        if (hand2Five.isValid) {
            return -1
        }

        val hand1Four = hand1.isFourOfAKind()
        val hand2Four = hand2.isFourOfAKind()

        if (hand1Four.isValid) {
            if (hand2Four.isValid) {
                return compareHands(hand1, hand2)
            }

            return 1
        }

        if (hand2Four.isValid) {
            return -1
        }

        val hand1Full = hand1.isFullHouse()
        val hand2Full = hand2.isFullHouse()

        if (hand1Full.isValid) {
            if (hand2Full.isValid) {
                return compareHands(hand1, hand2)
            }

            return 1
        }

        if (hand2Full.isValid) {
            return -1
        }

        val hand1Three = hand1.isThreeOfAKind()
        val hand2Three = hand2.isThreeOfAKind()

        if (hand1Three.isValid) {
            if (hand2Three.isValid) {
                return compareHands(hand1, hand2)
            }

            return 1
        }

        if (hand2Three.isValid) {
            return -1
        }

        val hand1TPair = hand1.isTwoPair()
        val hand2TPair = hand2.isTwoPair()

        if (hand1TPair.isValid) {
            if (hand2TPair.isValid) {
                return compareHands(hand1, hand2)
            }

            return 1
        }

        if (hand2TPair.isValid) {
            return -1
        }

        val hand1Pair = hand1.isOnePair()
        val hand2Pair = hand2.isOnePair()

        if (hand1Pair.isValid) {
            if (hand2Pair.isValid) {
                return compareHands(hand1, hand2)
            }

            return 1
        }

        if (hand2Pair.isValid) {
            return -1
        }

        val hand1High = hand1.isHighCard()
        val hand2High = hand2.isHighCard()

        if (hand1High.isValid) {
            if (hand2High.isValid) {
                return compareHands(hand1, hand2)
            }

            return 1
        }

        if (hand2High.isValid) {
            return -1
        }

        return compareHands(hand1, hand2)
    }

    private fun String.isFiveOfAKind(): BoolWithHighCard {
        val firstChar = this[0]
        val strLen = this.length
        val testStr = firstChar.toString().repeat(strLen)
        val highestCard = this.toCharArray()
            .sortedWith { a, b -> b compareCard a }[0]

        if (this == testStr) {
            return BoolWithHighCard.ofTrue(highestCard)
        }

        if (this.contains('J')) {
            return this.replaceJokers(highestCard).isFiveOfAKind()
        }

        return BoolWithHighCard.ofFalse()
    }

    private fun String.isFourOfAKind(): BoolWithHighCard {
        val chars = this.toCharArray().groupBy { it }

        if (chars.size != 2) {
            if (this.contains('J')) {
                val highestNotJoker = chars
                    .filter { it.key != 'J' }
                    .maxBy { it.value.size }

                return this.replaceJokers(highestNotJoker.key).isFourOfAKind()
            }

            return BoolWithHighCard.ofFalse()
        }

        var bigOne: Char? = null

        chars.forEach {
            if (it.value.size == 4) {
                bigOne = it.key
            }
        }

        if (bigOne == null) {
            return BoolWithHighCard.ofFalse()
        }

        return BoolWithHighCard.ofTrue(bigOne!!)
    }

    private fun String.isFullHouse(): BoolWithHighCard {
        val chars = this.toCharArray().groupBy { it }

        if (chars.size != 2) {
            if (this.contains('J')) {
                val (maxItemChar) = chars.filter { it.key != 'J' }
                    .maxBy { it.value.size }

                return this.replaceJokers(maxItemChar).isFullHouse()
            }

            return BoolWithHighCard.ofFalse()
        }

        var card1: Char? = null
        var card2: Char? = null

        chars.forEach {
            if (it.value.size == 3) {
                card1 = it.key
            } else if (it.value.size == 2) {
                card2 = it.key
            }
        }

        if (card1 == null || card2 == null) {
            return BoolWithHighCard.ofFalse()
        }

        return BoolWithHighCard.ofTrue(card1!!)
    }

    private fun String.isThreeOfAKind(): BoolWithHighCard {
        val chars = this.toCharArray().groupBy { it }

        if (!chars.any { it.value.size == 3 }) {
            if (this.contains('J')){
                val highest = chars.filter { it.key != 'J' }
                    .maxBy { it.value.size }

                return this.replaceJokers(highest.key).isThreeOfAKind()
            }

            return BoolWithHighCard.ofFalse()
        }

        // TODO: this may be wrong, double check the rules
        val highestOtherCard = chars.filter { it.value.size != 3 }
            .map { it.key }
            .sortedWith { a, b -> b compareCard a }[0]

        return BoolWithHighCard.ofTrue(highestOtherCard)
    }

    private fun String.isTwoPair(): BoolWithHighCard {
        val chars = this.toCharArray().groupBy { it }

        val pairs = chars.filter { it.value.size == 2 }
            .map { it.key }
            .sortedWith { a, b -> b compareCard a }

        if (pairs.size == 2) {
            return BoolWithHighCard.ofTrue(pairs[0])
        }

        if (this.contains('J')){
            val highest = chars.filter { it.key != 'J' }
                .maxBy { it.value.size }

            return this.replaceJokers(highest.key).isTwoPair()
        }

        if (this.contains('J')) {
            val itemThatHasPair = chars.filterValues { it.size == 2 }

            if (itemThatHasPair.isEmpty()) {
                return BoolWithHighCard.ofFalse()
            }

            val rmKey = itemThatHasPair.keys.first()
            val highestCard = this.toCharArray().filter { it != rmKey }.sortedWith { a, b -> b compareCard a }[0]

            return this.replaceJokers(highestCard).isTwoPair()
        }

        return BoolWithHighCard.ofFalse()
    }

    private fun String.isOnePair(): BoolWithHighCard {
        val chars = this.toCharArray().groupBy { it }
        val pairs = chars.filter { it.value.size == 2 }
            .map { it.key }
            .sortedWith { a, b -> b compareCard a }

        if (pairs.size == 1) {
            return BoolWithHighCard.ofTrue(pairs[0])
        }

        if (this.contains('J')){
            val highest = chars.filter { it.key != 'J' }
                .maxBy { it.value.size }

            return this.replaceJokers(highest.key).isOnePair()
        }

        if (this.contains('J')) {
            val highestCard = this.toCharArray().sortedWith { a, b -> b compareCard a }[0]

            return this.replaceJokers(highestCard).isOnePair()
        }

        return BoolWithHighCard.ofFalse()
    }

    private fun String.isHighCard(): BoolWithHighCard {
        val chars = this.toCharArray()
        val distinctArray = chars.distinct()

        if (chars.size == distinctArray.size) {
            val highestCard = chars.sortedWith { a, b -> b compareCard a }[0]

            return BoolWithHighCard.ofTrue(highestCard)
        }

        if (this.contains('J')){
            val highest = chars.groupBy { it }
                .filter { it.key != 'J' }
                .maxBy { it.value.size }

            return this.replaceJokers(highest.key).isThreeOfAKind()
        }

        return BoolWithHighCard.ofFalse()
    }

    private fun compareHands(hand1: String, hand2: String): Int {
        hand1.forEachIndexed { i, c1 ->
            val c2 = hand2[i]

            if (c1 != c2) {
                return c1 compareCard c2
            }
        }

        return 0
    }

    private fun String.replaceJokers(replacement: Char) = this.replace('J', replacement)
}

val cardPoints = listOf('J', '2', '3', '4', '5', '6', '7', '8', '9', 'T', 'Q', 'K', 'A')

infix fun Char.isHigherCardThan(other: Char): Boolean {
    val selfPoints = cardPoints.indexOf(this)
    val otherPoints = cardPoints.indexOf(other)
    return selfPoints > otherPoints
}

infix fun Char.compareCard(other: Char): Int {
    val selfPoints = cardPoints.indexOf(this)
    val otherPoints = cardPoints.indexOf(other)
    return selfPoints - otherPoints
}

open class FakeBool(private val boolVal: Boolean): Comparable<Boolean> {
    override fun compareTo(other: Boolean): Int {
        if (boolVal == other) {
            return 0
        }

        if (boolVal) {
            return 1
        }

        return -1
    }

    operator fun not() = !boolVal
    infix fun and(other: Boolean) = boolVal && other
    infix fun or(other: Boolean) = boolVal || other
    infix fun xor(other: Boolean) = (boolVal && !other) || (!boolVal && other)
}

data class BoolWithHighCard(val isValid: Boolean, val highestCard: Char) : FakeBool(isValid) {
    fun compareTo(other: BoolWithHighCard): Int {
        return highestCard compareCard other.highestCard
    }

    companion object {
        fun ofFalse() = BoolWithHighCard(false, 'X')
        fun ofTrue(card: Char) = BoolWithHighCard(true, card)
    }
}
