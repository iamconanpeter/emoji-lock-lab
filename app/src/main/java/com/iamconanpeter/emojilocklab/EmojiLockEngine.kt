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
        val gameOver: Boolean
    )

    private val rng = Random(seed)
    private var secret: List<String> = generateSecret()
    private val history = mutableListOf<Pair<List<String>, GuessResult>>()

    fun getPool(): List<String> = emojiPool

    fun attemptsLeft(): Int = maxAttempts - history.size

    fun isSolved(): Boolean = history.lastOrNull()?.second?.solved == true

    fun isGameOver(): Boolean = isSolved() || history.size >= maxAttempts

    fun revealSecretForDebug(): List<String> = secret

    fun historyLines(): List<String> = history.mapIndexed { idx, entry ->
        val guess = entry.first.joinToString(" ")
        val r = entry.second
        "${idx + 1}. $guess  | exact:${r.exact} misplaced:${r.misplaced}"
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

        val solved = exact == codeLength
        val result = GuessResult(
            exact = exact,
            misplaced = misplaced,
            solved = solved,
            attemptsUsed = history.size + 1,
            gameOver = solved || history.size + 1 >= maxAttempts
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
    }

    private fun generateSecret(): List<String> {
        val result = mutableListOf<String>()
        repeat(codeLength) {
            result += emojiPool[rng.nextInt(emojiPool.size)]
        }
        return result
    }
}
