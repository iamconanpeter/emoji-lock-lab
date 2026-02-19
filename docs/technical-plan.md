# Emoji Lock Lab — Retrofit Technical Plan

## Stack
- Kotlin + Android SDK (AppCompat + XML)
- Min SDK 24 / Target 34
- JUnit4 for deterministic engine tests

## Retrofit Architecture

### 1) Engine upgrades (`EmojiLockEngine`)
New systems:
- `hintCharges` and `consumeHint()`
- near-miss detector (`exact + misplaced >= 3` when unsolved)
- pity-like reward: two near misses grant +1 hint charge
- solve quality `starRating` (3/2/1 stars)
- richer `GuessResult` metadata (`nearMiss`, `awardedHint`, `starRating`)

### 2) Retention utility (`DailyChallengeManager`)
- Generates daily deterministic seed
- Tracks completion streak in SharedPreferences
- Returns structured streak update on win

### 3) UI/feedback polish (`MainActivity` + layout)
- Added explicit Hint button
- Color-coded status messaging by event outcome
- Subtitle now surfaces streak, hint inventory, revealed hint symbols
- Solve feedback includes stars and streak progression

## Differentiation Mapping vs clone quality
- Generic clone: static clues only → **Added adaptive hint economy**
- Generic clone: no return reason → **Added daily seed + streak persistence**
- Generic clone: flat win/lose messaging → **Added solve quality stars + rich outcome text**

## Test strategy
- Solved-on-first-try gets 3 stars
- Two near misses award extra hint charge
- Consuming hint reveals a true secret emoji and decrements charge
- Max attempts still ends game correctly

## Validation
- `./gradlew test`
- `./gradlew assembleDebug`
- combined: `./gradlew test assembleDebug`
