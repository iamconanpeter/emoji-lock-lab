package com.iamconanpeter.emojilocklab

import android.content.Context
import java.util.Calendar

class DailyChallengeManager(context: Context) {
    private val prefs = context.getSharedPreferences("emoji_lock_lab", Context.MODE_PRIVATE)

    data class StreakUpdate(
        val streak: Int,
        val alreadyCompletedToday: Boolean
    )

    fun dailySeed(): Int {
        val cal = Calendar.getInstance()
        val y = cal.get(Calendar.YEAR)
        val d = cal.get(Calendar.DAY_OF_YEAR)
        return (y * 1000) + d
    }

    fun dailyKey(): String {
        val cal = Calendar.getInstance()
        val y = cal.get(Calendar.YEAR)
        val d = cal.get(Calendar.DAY_OF_YEAR)
        return "$y-$d"
    }

    fun currentStreak(): Int = prefs.getInt("daily_streak", 0)

    fun recordDailyWin(): StreakUpdate {
        val today = dailyKey()
        val completedKey = prefs.getString("last_completed_day", null)
        if (completedKey == today) {
            return StreakUpdate(currentStreak(), alreadyCompletedToday = true)
        }

        val previousDay = prefs.getString("previous_completed_day", null)
        val previousStreak = currentStreak()

        val continues = previousDay != null && isPreviousDay(previousDay, today)
        val nextStreak = if (continues) previousStreak + 1 else 1

        prefs.edit()
            .putString("previous_completed_day", completedKey)
            .putString("last_completed_day", today)
            .putInt("daily_streak", nextStreak)
            .apply()

        return StreakUpdate(nextStreak, alreadyCompletedToday = false)
    }

    private fun isPreviousDay(prev: String, today: String): Boolean {
        val p = prev.split("-")
        val t = today.split("-")
        if (p.size != 2 || t.size != 2) return false

        val py = p[0].toIntOrNull() ?: return false
        val pd = p[1].toIntOrNull() ?: return false
        val ty = t[0].toIntOrNull() ?: return false
        val td = t[1].toIntOrNull() ?: return false

        return (py == ty && pd + 1 == td) || (py + 1 == ty && pd >= 365 && td == 1)
    }
}
