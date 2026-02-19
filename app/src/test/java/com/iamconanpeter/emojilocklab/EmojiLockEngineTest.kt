package com.iamconanpeter.emojilocklab

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class EmojiLockEngineTest {

    @Test
    fun `perfect guess solves puzzle with 3 stars on first try`() {
        val engine = EmojiLockEngine(seed = 7)
        val secret = engine.revealSecretForDebug()

        val result = engine.submitGuess(secret)

        assertTrue(result.solved)
        assertEquals(4, result.exact)
        assertEquals(0, result.misplaced)
        assertEquals(3, result.starRating)
        assertTrue(result.gameOver)
    }

    @Test
    fun `two near misses award extra hint charge`() {
        val pool = listOf("A", "B", "C", "D", "E", "F")
        val engine = EmojiLockEngine(emojiPool = pool, codeLength = 4, maxAttempts = 8, seed = 3)
        val secret = engine.revealSecretForDebug().toMutableList()

        val guess1 = secret.toMutableList()
        guess1[0] = pool.first { it != secret[0] }

        val guess2 = secret.toMutableList()
        guess2[1] = pool.first { it != secret[1] }

        val r1 = engine.submitGuess(guess1)
        val r2 = engine.submitGuess(guess2)

        assertTrue(r1.nearMiss)
        assertTrue(r2.nearMiss)
        assertTrue(r2.awardedHint)
        assertEquals(2, engine.hintCharges())
    }

    @Test
    fun `consume hint reveals emoji from secret and decrements charge`() {
        val engine = EmojiLockEngine(seed = 11)
        val before = engine.hintCharges()

        val hint = engine.consumeHint()

        assertNotNull(hint)
        assertEquals(before - 1, engine.hintCharges())
        val revealed = hint!!.emoji
        assertTrue(engine.revealSecretForDebug().contains(revealed))
        assertTrue(engine.revealedHints().contains(revealed))
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
        assertFalse(second.solved)
    }
}
