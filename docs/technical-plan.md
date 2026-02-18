# Emoji Lock Lab — Technical Plan

## Stack
- Kotlin + Android SDK (XML layout + AppCompat)
- Min SDK 24, Target/Compile 34
- JUnit4 unit tests

## Architecture
- `EmojiLockEngine`: pure Kotlin game rules (secret generation, scoring, attempts)
- `MainActivity`: input wiring, state display, replay actions
- XML UI: static controls for fast MVP delivery

## Testing
- Correct solve behavior with exact guess
- Misplaced/exact scoring sanity
- Max-attempt game-over behavior

## Build commands
- `./gradlew test`
- `./gradlew assembleDebug`
