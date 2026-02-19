# Emoji Lock Lab — Retrofit Plan Mode Spec

## Plan Mode Status
- **Mode:** Codex PLAN MODE (retrofit)
- **Retrofit target:** raise retention/fairness quality beyond basic mastermind clone behavior
- **Constraint:** keep lightweight 2D, no heavy assets, one-screen usability

## Q&A Discovery (required)

### 1) Core fantasy and 10-second hook
**Q:** What fantasy is delivered instantly?  
**A:** “I can crack a secret emoji lock through deduction.” In <10 seconds player sees emoji keypad + clue loop.

### 2) Why users come back (daily/weekly loop)
**Q:** Why return tomorrow?  
**A:** Daily-seeded puzzle + streak tracking + stars based on efficiency/hint usage.

### 3) Session length targets
**Q:** Expected session lengths?  
**A:** 30s (quick fail), 2m (normal solve), 5m (multiple retries and streak maintenance).

### 4) Skill vs luck balance
**Q:** Is game mostly skill?  
**A:** Mostly deduction skill; randomness only in daily secret generation and hint reveal selection.

### 5) Fail-state fairness and frustration controls
**Q:** How to reduce dead-end frustration?  
**A:** Near-miss pity system grants extra hint charge after repeated close guesses.

### 6) Difficulty ramp and onboarding
**Q:** How does challenge ramp?  
**A:** Same core 4-slot puzzle for onboarding, but performance pressure introduced via star rating and streak value.

### 7) Distinctive mechanic vs common Android clones
**Q:** What differentiates this from low-quality clone puzzlers?  
**A:** Daily challenge identity + near-miss adaptive hints + transparent clue feedback with efficiency stars.

### 8) Art/animation scope feasible for small team
**Q:** Feasible scope?  
**A:** Yes. Keep text-first visual system with emoji-rich UI and color-coded feedback.

### 9) Audio/feedback plan
**Q:** Feedback without heavy audio pipeline?  
**A:** Color-coded status text and contextual messages (Near miss / +hint / stars / streak) in real time.

### 10) Monetization-safe design
**Q:** Future-safe monetization path?  
**A:** Optional cosmetic themes or extra puzzle packs. No manipulative timers or pay-to-win hints.

### 11) Technical constraints and performance budgets
**Q:** Constraints?  
**A:** Offline deterministic logic, min SDK 24, low-memory UI, unit-testable pure game engine.

## Assumptions
- Assumption: Android casual puzzle users respond positively to daily streak loop if entry friction remains low.
- Assumption: adaptive hint granting improves retention without trivializing challenge.

## USP (1 line)
A daily emoji code-breaker with adaptive near-miss hint recovery and streak-based mastery pressure.

## 3 Differentiators
1. Near-miss pity logic that awards hint charges.
2. Daily-seeded puzzle identity with streak progression.
3. Star rating that rewards solving efficiently without overusing hints.

## 3 Retention Hooks
1. Daily completion streak.
2. Star-chase optimization across attempts/hints.
3. Persistent revealed-hint memory during run to support comeback play.

## 3 Quality Bars
1. Clear, color-coded status feedback each submit.
2. Transparent clue history with hint-award indicators.
3. One-tap hint action with explicit remaining charges.

## Retrofit Scope (this run)
### In scope
- Add hint economy + near-miss adaptive reward
- Add star-rating solve quality
- Add daily-seed/streak manager
- Add UI hint button and richer feedback strings
- Expand engine unit tests around new systems

### Post-MVP retrofit
- Shareable daily result card
- Optional hard mode (no hints)
- Lightweight sound/haptic profile
