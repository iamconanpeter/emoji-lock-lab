package com.iamconanpeter.emojilocklab

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var engine: EmojiLockEngine
    private lateinit var dailyManager: DailyChallengeManager
    private val currentGuess = mutableListOf<String>()

    private lateinit var subtitleText: TextView
    private lateinit var guessText: TextView
    private lateinit var statusText: TextView
    private lateinit var historyText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dailyManager = DailyChallengeManager(this)
        engine = EmojiLockEngine(seed = dailyManager.dailySeed())

        subtitleText = findViewById(R.id.subtitleText)
        guessText = findViewById(R.id.guessText)
        statusText = findViewById(R.id.statusText)
        historyText = findViewById(R.id.historyText)

        bindEmojiButtons()

        findViewById<Button>(R.id.clearBtn).setOnClickListener {
            currentGuess.clear()
            updateGuessLabel()
            statusText.text = "Guess cleared"
            statusText.setTextColor(Color.parseColor("#A7F3D0"))
        }

        findViewById<Button>(R.id.hintBtn).setOnClickListener {
            val hint = engine.consumeHint()
            if (hint == null) {
                statusText.text = "No hint available"
                statusText.setTextColor(Color.parseColor("#FCA5A5"))
            } else {
                statusText.text = "Hint: code contains ${hint.emoji} • hints left ${hint.remainingCharges}"
                statusText.setTextColor(Color.parseColor("#93C5FD"))
            }
            refreshSubtitle()
        }

        findViewById<Button>(R.id.submitBtn).setOnClickListener {
            submitGuess()
        }

        findViewById<Button>(R.id.newGameBtn).setOnClickListener {
            engine.reset(seed = dailyManager.dailySeed())
            currentGuess.clear()
            statusText.text = "Daily puzzle refreshed"
            statusText.setTextColor(Color.parseColor("#A7F3D0"))
            updateGuessLabel()
            renderHistory()
            refreshSubtitle()
        }

        updateGuessLabel()
        renderHistory()
        refreshSubtitle()
    }

    private fun bindEmojiButtons() {
        val emojiButtons = listOf(
            R.id.emoji1, R.id.emoji2, R.id.emoji3,
            R.id.emoji4, R.id.emoji5, R.id.emoji6
        )

        emojiButtons.forEach { id ->
            findViewById<Button>(id).setOnClickListener { button ->
                if (engine.isGameOver()) return@setOnClickListener
                if (currentGuess.size < 4) {
                    currentGuess += (button as Button).text.toString()
                    updateGuessLabel()
                }
            }
        }
    }

    private fun submitGuess() {
        if (engine.isGameOver()) return
        if (currentGuess.size != 4) {
            statusText.text = "Pick 4 emojis first"
            statusText.setTextColor(Color.parseColor("#FCA5A5"))
            return
        }

        val result = engine.submitGuess(currentGuess.toList())
        val attemptsLeft = engine.attemptsLeft()

        if (result.solved) {
            val streak = dailyManager.recordDailyWin()
            val stars = result.starRating ?: 1
            val streakSuffix = if (streak.alreadyCompletedToday) {
                ""
            } else {
                " • Streak ${streak.streak}"
            }
            statusText.text = "Unlocked! ${"⭐".repeat(stars)}$streakSuffix"
            statusText.setTextColor(Color.parseColor("#34D399"))
        } else if (result.gameOver) {
            statusText.text = "Lockdown! Code was ${engine.revealSecretForDebug().joinToString(" ")}"
            statusText.setTextColor(Color.parseColor("#FCA5A5"))
        } else {
            val near = if (result.nearMiss) " • Near miss" else ""
            val bonus = if (result.awardedHint) " • +1 hint" else ""
            statusText.text = "Exact ${result.exact}, misplaced ${result.misplaced} • $attemptsLeft tries left$near$bonus"
            statusText.setTextColor(Color.parseColor("#A7F3D0"))
        }

        currentGuess.clear()
        updateGuessLabel()
        renderHistory()
        refreshSubtitle()
    }

    private fun refreshSubtitle() {
        val streak = dailyManager.currentStreak()
        val hints = engine.hintCharges()
        val revealed = if (engine.revealedHints().isEmpty()) "none" else engine.revealedHints().joinToString(" ")
        subtitleText.text = "Daily streak: $streak • Hints: $hints • Revealed: $revealed"
    }

    private fun updateGuessLabel() {
        val slots = (currentGuess + List(4 - currentGuess.size) { "_" }).joinToString(" ")
        guessText.text = "Current: $slots"
    }

    private fun renderHistory() {
        val lines = engine.historyLines()
        historyText.text = if (lines.isEmpty()) "No guesses yet" else lines.joinToString("\n")
    }
}
