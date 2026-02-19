# Emoji Lock Lab — Retrofit Tasks

## PLAN MODE gate
- [x] Q&A discovery + assumptions documented in spec
- [x] Differentiation/retention/quality checklist documented
- [x] Scope guardrail and post-MVP split documented

## Mandatory code delta
- [x] Change >=2 gameplay/source files (`EmojiLockEngine`, `MainActivity`, `DailyChallengeManager`)
- [x] Add UI/feedback polish in code (hint button + color-coded status + streak/hint subtitle)
- [x] Expand unit tests with new gameplay/fairness coverage

## Implementation
- [x] Add hint economy and near-miss reward loop
- [x] Add star rating solve-quality metric
- [x] Add daily seed + streak tracking helper
- [x] Wire daily/streak/hint UI feedback in activity

## Validation & release
- [x] Run `./gradlew test assembleDebug`
- [x] Commit retrofit changes
- [x] Push to same GitHub repo
- [x] Update `research/game_factory_status.json` with retrofit summary and changed code files
