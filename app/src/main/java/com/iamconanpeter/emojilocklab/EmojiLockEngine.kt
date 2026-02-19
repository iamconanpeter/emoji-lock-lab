package com.iamconanpeter.emojilocklab

import kotlin.random.Random

class EmojiLockEngine(
    private val emojiPool: List<String> = listOf("😀", "😎", "🤖", "🐱", "🔥", "🌙"),
    private val codeLength: Int = 4,
    private val maxAttempts: Int = 8,
    seed: Int = 1337
) {
    data class GuessResult(
        val exact: Int,
        val misplaced: Int,
        val solved: Boolean,
        val attemptsUsed: Int,
        val gameOver: Boolean,
        val nearMiss: Boolean,
        val awardedHint: Boolean,
        val starRating: Int?
    )

    data class HintReveal(
        val emoji: String,
        val remainingCharges: Int
    )

    private val rng = Random(seed)
    private var secret: List<String> = generateSecret()
    private val history = mutableListOf<Pair<List<String>, GuessResult>>()

    private var hintCharges = 1
    private var nearMissStreak = 0
    private val revealedHints = mutableListOf<String>()

    fun getPool(): List<String> = emojiPool

    fun attemptsLeft(): Int = maxAttempts - history.size

    fun hintCharges(): Int = hintCharges

    fun revealedHints(): List<String> = revealedHints.toList()

    fun isSolved(): Boolean = history.lastOrNull()?.second?.solved == true

    fun isGameOver(): Boolean = isSolved() || history.size >= maxAttempts

    fun revealSecretForDebug(): List<String> = secret

    fun historyLines(): List<String> = history.mapIndexed { idx, entry ->
        val guess = entry.first.joinToString(" ")
        val r = entry.second
        val bonus = if (r.awardedHint) "  +hint" else ""
        "${idx + 1}. $guess  | exact:${r.exact} misplaced:${r.misplaced}$bonus"
    }

    fun consumeHint(): HintReveal? {
        if (isGameOver() || hintCharges <= 0) return null

        val hidden = secret.filterNot { revealedHints.contains(it) }
        if (hidden.isEmpty()) return null

        val pick = hidden[rng.nextInt(hidden.size)]
        hintCharges -= 1
        revealedHints += pick
        return HintReveal(pick, hintCharges)
    }

    fun submitGuess(guess: List<String>): GuessResult {
        require(guess.size == codeLength) { "Guess size must be $codeLength" }
        require(!isGameOver()) { "Game is over" }
        require(guess.all { emojiPool.contains(it) }) { "Guess contains emoji outside pool" }

        val exactMask = BooleanArray(codeLength)
        val secretUsed = BooleanArray(codeLength)

        var exact = 0
        for (i in 0 until codeLength) {
            if (guess[i] == secret[i]) {
                exact += 1
                exactMask[i] = true
                secretUsed[i] = true
            }
        }

        var misplaced = 0
        for (i in 0 until codeLength) {
            if (exactMask[i]) continue
            val g = guess[i]
            for (j in 0 until codeLength) {
                if (!secretUsed[j] && secret[j] == g) {
                    misplaced += 1
                    secretUsed[j] = true
                    break
                }
            }
        }

        val nearMiss = ! (exact == codeLength) && (exact + misplaced >= codeLength - 1)
        var awardedHint = false
        if (nearMiss) {
            nearMissStreak += 1
            if (nearMissStreak >= 2 && hintCharges < 2) {
                hintCharges += 1
                nearMissStreak = 0
                awardedHint = true
            }
        } else {
            nearMissStreak = 0
        }

        val solved = exact == codeLength
        val attemptsUsed = history.size + 1
        val gameOver = solved || attemptsUsed >= maxAttempts
        val stars = if (solved) calculateStars(attemptsUsed, revealedHints.size) else null

        val result = GuessResult(
            exact = exact,
            misplaced = misplaced,
            solved = solved,
            attemptsUsed = attemptsUsed,
            gameOver = gameOver,
            nearMiss = nearMiss,
            awardedHint = awardedHint,
            starRating = stars
        )
        history += guess to result
        return result
    }

    fun reset(seed: Int? = null) {
        if (seed != null) {
            val newRng = Random(seed)
            val newSecret = mutableListOf<String>()
            repeat(codeLength) { newSecret += emojiPool[newRng.nextInt(emojiPool.size)] }
            secret = newSecret
        } else {
            secret = generateSecret()
        }
        history.clear()
        hintCharges = 1
        nearMissStreak = 0
        revealedHints.clear()
    }

    private fun calculateStars(attemptsUsed: Int, hintsUsed: Int): Int = when {
        attemptsUsed <= 4 && hintsUsed == 0 -> 3
        attemptsUsed <= 6 -> 2
        else -> 1
    }

    private fun generateSecret(): List<String> {
        val result = mutableListOf<String>()
        repeat(codeLength) {
            result += emojiPool[rng.nextInt(emojiPool.size)]
        }
        return result
    }
}
