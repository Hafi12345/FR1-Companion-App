# FR1 Companion App — Build Memory

> **Purpose:** This file is the single source of truth for "where are we right now." Claude Code should update it at the end of every work session and after every completed phase — not just at the end of the whole project. If a new Claude Code session starts, read this file first before touching any code.

## How To Update This File
1. After finishing a task or phase, move it from "In Progress" to "Completed."
2. If you start something new, add it to "In Progress" immediately — don't wait until it's done.
3. Log any blocker, workaround, or deviation from `phases.md` / `architecture.md` under "Notes & Deviations" with a short reason.
4. Keep entries short — this is a status log, not documentation. Detailed explanations belong in code comments or the other planning docs.
5. Never delete history — append to "Session Log" each session rather than overwriting it.

---

## Current Phase
`Phase 1 — Foundations: Theme, Navigation, Bilingual Scaffold` *(update this line as phases progress)*

## Files Created
- `gradle/libs.versions.toml` — added Navigation Compose, Coroutines, Retrofit/OkHttp, Room (+ KSP), DataStore, CameraX version/library entries
- `app/build.gradle.kts` — wired the new dependencies + `ksp` plugin
- `gradle.properties` — added `android.disallowKotlinSourceSets=false` (see Notes & Deviations)
- `app/src/main/java/com/fr1/companion/{ui/*,domain/*,data/*,util}/` — package skeleton per `architecture.md`, empty dirs hold `.gitkeep`
- `app/src/main/res/{values-ur,font}/` — resource dirs per `architecture.md`, empty, hold `.gitkeep`

## Files In Progress
*(none yet)*

## Completed Phases
- [x] Phase 0 — Environment & Project Setup
- [ ] Phase 1 — Foundations: Theme, Navigation, Bilingual Scaffold
- [ ] Phase 2 — Wound Assessment (Rule Engine + UI)
- [ ] Phase 3 — Treatment Guidance Library
- [ ] Phase 4 — Chatbot (Ollama Integration + Fallback)
- [ ] Phase 5 — Emergency Alert & Incident History
- [ ] Phase 6 — Settings Finalization
- [ ] Phase 7 — Integration Testing & Bug Fixing
- [ ] Phase 8 — Polish, APK Build, Demo Rehearsal

## Known Issues / Blockers
*(none currently — see Notes & Deviations for the disk-space workaround used to get the emulator running)*

## Notes & Deviations from Plan
- The project's AGP version (9.3.0) uses AGP's new "built-in Kotlin" compilation model, which by default rejects KSP's classic `kotlin.sourceSets` DSL usage (needed for Room's annotation processing) with: `Using kotlin.sourceSets DSL to add Kotlin sources is not allowed with built-in Kotlin`. Fixed by setting `android.disallowKotlinSourceSets=false` in `gradle.properties` per AGP's own suggested remedy (https://developer.android.com/r/tools/built-in-kotlin). Revisit if a future AGP/KSP release removes the need for this flag.
- KSP version pinned to `2.2.10-2.0.2` to match the project's Kotlin version `2.2.10` (verified via Maven Central; `2.2.10-2.0.1` does not exist and fails plugin resolution).
- Dev machine's C: drive was at 95% capacity (~6GB free) with no cmdline-tools/AVDs installed. Installed `cmdline-tools`, `platforms;android-35`, `system-images;android-35;google_apis;x86_64`, and `emulator`, then created an AVD (`FR1_Test`, Pixel 6 profile, API 35). The API 35 image enforces a hard ~7.2GB minimum userdata-partition floor regardless of configured size, which didn't fit until the user freed disk space (~6GB → ~10GB free). Emulator boots fine now; keep this in mind if disk space gets tight again during later phases (APK installs, Room DB files, etc. all need headroom on the same drive).

## Session Log
*(append one entry per work session)*

### Session 1 — 2026-07-22
- Started: Phase 0 — Environment & Project Setup
- Completed: Verified existing AS-generated project skeleton (package `com.fr1.companion`, minSdk 26, Compose). Added Retrofit/OkHttp, Room+KSP, DataStore, CameraX, Coroutines, Navigation Compose to the version catalog and app module. Scaffolded the full `architecture.md` package/resource folder structure. Confirmed `assembleDebug` builds successfully. Initialized git repo and made the initial commit. Set up Android SDK cmdline-tools, an API 35 system image, and an AVD (`FR1_Test`); installed the debug APK and confirmed via logcat + screenshot that the app launches with no crash.
- Next up: Phase 1 — theme, navigation graph, LocaleManager, bilingual strings, Home screen, DataStore setup
