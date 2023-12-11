package me.duncte123.adventofcode

import me.duncte123.adventofcode.partial.AbstractSolution

class DaySeven : AbstractSolution() {
    private val cardPoints = listOf('2', '3', '4', '5', '6', '7', '8', '9', 'T', 'J', 'Q', 'K', 'A')

    // cards -> bid
    override fun getTestInput() =
        "32T3K 765\n" +
        "T55J5 684\n" +
        "KK677 28\n" +
        "KTJJT 220\n" +
        "QQQJA 483"

    override fun run(input: String): String {
        val hands = input.split("\n").map {
            val (cards, bid) = it.split(" ")

            cards to bid.toInt()
        }

        println(hands)

        val sortedHands = hands.sortedWith(this::compareHands)

        println(sortedHands)

        var result = 0

        sortedHands.forEachIndexed { i, (_, points) ->
            result += points * (i + 1)
        }

        return "$result"
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

        if (this == testStr) {
            val highestCard = this.toCharArray()
                .sortedWith { a, b -> a compareCard b }[0]
            return BoolWithHighCard.ofTrue(highestCard)
        }

        return BoolWithHighCard.ofFalse()
    }

    private fun String.isFourOfAKind(): BoolWithHighCard {
        val chars = this.toCharArray().groupBy { it }

        if (chars.size != 2) {
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
            return BoolWithHighCard.ofFalse()
        }

        // TODO: this may be wrong, double check the rules
        val highestOtherCard = chars.filter { it.value.size != 3 }
            .map { it.key }
            .sortedWith { a, b -> a compareCard b}[0]

        return BoolWithHighCard.ofTrue(highestOtherCard)
    }

    private fun String.isTwoPair(): BoolWithHighCard {
        val chars = this.toCharArray().groupBy { it }

        val pairs = chars.filter { it.value.size == 2 }
            .map { it.key }
            .sortedWith { a, b -> a compareCard b }

        if (pairs.size == 2) {
            return BoolWithHighCard.ofTrue(pairs[0])
        }

        return BoolWithHighCard.ofFalse()
    }

    private fun String.isOnePair(): BoolWithHighCard {
        val chars = this.toCharArray().groupBy { it }
        val pairs = chars.filter { it.value.size == 2 }
            .map { it.key }
            .sortedWith { a, b -> a compareCard b }

        if (pairs.size == 1) {
            return BoolWithHighCard.ofTrue(pairs[0])
        }

        return BoolWithHighCard.ofFalse()
    }

    private fun String.isHighCard(): BoolWithHighCard {
        val chars = this.toCharArray()
        val distinctArray = chars.distinct()

        if (chars.size == distinctArray.size) {
            val highestCard = chars.sortedWith { a, b -> a compareCard b }[0]

            return BoolWithHighCard.ofTrue(highestCard)
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

        if (boolVal && !other) {
            return 1
        }

        return -1
    }

    operator fun not() = !boolVal
    infix fun and(other: Boolean) = boolVal && other
    infix fun or(other: Boolean) = boolVal || other
    infix fun xor(other: Boolean) = (boolVal && !other) || (!boolVal && other)
}

data class FullHouseResult(val isFullHouse: Boolean, val card1: Char, val card2: Char) : FakeBool(isFullHouse) {
    companion object {
        fun ofFalse() = FullHouseResult(false, 'X', 'X')
        fun ofTrue(card1: Char, card2: Char) = FullHouseResult(true, card1, card2)
    }
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

data class ThreeOfAKind(val isValid: Boolean, val highestOtherCard: Char): FakeBool(isValid) {
    companion object {
        fun ofFalse() = ThreeOfAKind(false, 'X')
        fun ofTrue(card: Char) = ThreeOfAKind(true, card)
    }
}

