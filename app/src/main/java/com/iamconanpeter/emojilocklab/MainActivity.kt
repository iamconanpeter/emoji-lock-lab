package com.iamconanpeter.emojilocklab

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var engine: EmojiLockEngine
    private val currentGuess = mutableListOf<String>()

    private lateinit var guessText: TextView
    private lateinit var statusText: TextView
    private lateinit var historyText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        engine = EmojiLockEngine()

        guessText = findViewById(R.id.guessText)
        statusText = findViewById(R.id.statusText)
        historyText = findViewById(R.id.historyText)

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

        findViewById<Button>(R.id.clearBtn).setOnClickListener {
            currentGuess.clear()
            updateGuessLabel()
        }

        findViewById<Button>(R.id.submitBtn).setOnClickListener {
            if (engine.isGameOver()) return@setOnClickListener
            if (currentGuess.size != 4) {
                statusText.text = "Pick 4 emojis first"
                return@setOnClickListener
            }

            val result = engine.submitGuess(currentGuess.toList())
            val attemptsLeft = engine.attemptsLeft()
            statusText.text = when {
                result.solved -> "Unlocked! 🎉"
                result.gameOver -> "Lockdown! Code was ${engine.revealSecretForDebug().joinToString(" ")}"
                else -> "Exact ${result.exact}, misplaced ${result.misplaced} • $attemptsLeft tries left"
            }

            currentGuess.clear()
            updateGuessLabel()
            renderHistory()
        }

        findViewById<Button>(R.id.newGameBtn).setOnClickListener {
            engine.reset()
            currentGuess.clear()
            statusText.text = "New puzzle ready"
            updateGuessLabel()
            renderHistory()
        }

        updateGuessLabel()
        renderHistory()
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
