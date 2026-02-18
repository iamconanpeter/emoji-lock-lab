package com.iamconanpeter.emojilocklab

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class EmojiLockEngineTest {

    @Test
    fun `perfect guess solves puzzle`() {
        val engine = EmojiLockEngine(seed = 7)
        val secret = engine.revealSecretForDebug()

        val result = engine.submitGuess(secret)

        assertTrue(result.solved)
        assertEquals(4, result.exact)
        assertEquals(0, result.misplaced)
        assertTrue(result.gameOver)
    }

    @Test
    fun `misplaced scoring works with duplicates`() {
        val pool = listOf("A", "B", "C", "D")
        val engine = EmojiLockEngine(emojiPool = pool, codeLength = 4, maxAttempts = 8, seed = 1)
        // force known secret by reset with deterministic seed
        engine.reset(seed = 9)
        val secret = engine.revealSecretForDebug()

        // rotate guess to create mostly misplaced chars
        val guess = listOf(secret[1], secret[2], secret[3], secret[0])
        val result = engine.submitGuess(guess)

        assertTrue(result.exact in 0..4)
        assertEquals(4 - result.exact, result.misplaced)
    }

    @Test
    fun `game over after max attempts`() {
        val pool = listOf("A", "B", "C", "D")
        val engine = EmojiLockEngine(emojiPool = pool, codeLength = 4, maxAttempts = 2, seed = 3)
        val impossible = listOf("A", "A", "A", "A")

        engine.submitGuess(impossible)
        val second = engine.submitGuess(impossible)

        assertTrue(second.gameOver)
        assertEquals(0, engine.attemptsLeft())
    }
}
