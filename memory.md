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
`Phase 4 — Chatbot (Ollama Integration + Fallback)` *(update this line as phases progress)*

## Files Created
- `domain/model/GuidanceEntry.kt` — `GuidanceCategory` enum (SEVERE_BLEEDING, FRACTURE, BURNS, SHOCK, CHOKING, POSITIONING, CPR), each carrying its own `ImageVector` icon; `GuidanceEntry(category, titleRes, stepsRes: List<Int>)`
- `data/content/GuidanceContent.kt` — bundled `object` with all 7 entries (Kotlin data, not JSON/DB, per `architecture.md`/`rules.md` — this content never changes at runtime)
- `ui/guidance/GuidanceListScreen.kt` (rewritten from stub) — `LazyColumn` of category cards (icon + localized title) via `NavCard`
- `ui/guidance/GuidanceDetailScreen.kt` — numbered step list + category icon/title + `guidance_disclaimer` footer
- `ui/components/NavCard.kt` — added an optional leading `icon: ImageVector?` param (backward compatible, `Home` screen cards unaffected)
- `domain/rules/WoundSeverityRuleEngine.kt` — added `recommendedGuidanceCategory(answers): GuidanceCategory?` (SIMULATED priority mapping, same answers as `assess()`), returns `null` for a clean Minor result (no single category fits, so "View Guidance" falls back to the full list)
- `ui/navigation/Routes.kt` + `FR1NavGraph.kt` — extended `severity_result` route to also carry the recommended category (`severity_result/{severity}/{category}`, `"none"` sentinel when absent) and added `guidance_detail/{category}`; `WoundAssessmentScreen`'s `onComplete` now passes `(Severity, GuidanceCategory?)` so a Severe/Moderate result's "View Guidance" button deep-links straight to the matching category page instead of the general list
- `res/values/strings.xml` + `res/values-ur/strings.xml` — full EN/UR parity for all 7 categories' titles + steps, plus `guidance_disclaimer`
- `domain/model/{Severity,WoundAssessmentAnswers}.kt` — `Severity` enum (MINOR/MODERATE/SEVERE), `BleedingLevel` enum, `WoundAssessmentAnswers` data class
- `domain/rules/WoundSeverityRuleEngine.kt` — fixed decision tree (SIMULATED, not clinically validated): any of unconscious/breathing difficulty/heavy bleeding/visible bone-muscle → Severe; light bleeding or wound-larger-than-palm → Moderate; else Minor
- `ui/woundassessment/WoundAssessmentViewModel.kt` — `AndroidViewModel` holding a 5-question guided flow as `StateFlow<WoundAssessmentUiState>`, calls the rule engine once all questions are answered
- `ui/woundassessment/CameraCaptureScreen.kt` — CameraX preview + capture, permission request flow, saves to app-private `filesDir/wound_photos/` and returns a `FileProvider` content `Uri`
- `ui/woundassessment/SeverityResultScreen.kt` — color-coded severity badge (green/amber/red) with severity-specific actions (Severe → Emergency Alert front and center)
- `res/xml/file_paths.xml` — FileProvider path config for `wound_photos/`
- `ui/woundassessment/WoundAssessmentScreen.kt` (rewritten from stub) — full guided question UI + optional photo attach, wired to the ViewModel
- `AndroidManifest.xml` — added `CAMERA` permission (+ optional `android.hardware.camera` feature) and the `FileProvider` `<provider>` entry
- `ui/navigation/{Routes,FR1NavGraph}.kt` — added `severity_result/{severity}` route between Wound Assessment and Guidance/Emergency/Home
- `res/values/strings.xml` + `res/values-ur/strings.xml` — full EN/UR parity for every Phase 2 string (questions, bleeding options, camera UI, severity results)
- `gradle/libs.versions.toml` — added Navigation Compose, Coroutines, Retrofit/OkHttp, Room (+ KSP), DataStore, CameraX, Lifecycle ViewModel/Runtime Compose, material-icons-core version/library entries
- `app/build.gradle.kts` — wired the new dependencies + `ksp` plugin
- `gradle.properties` — added `android.disallowKotlinSourceSets=false` (see Notes & Deviations)
- `app/src/main/java/com/fr1/companion/{ui/*,domain/*,data/*,util}/` — package skeleton per `architecture.md`, empty dirs hold `.gitkeep`
- `app/src/main/res/{values-ur,font}/` — resource dirs per `architecture.md`, empty, hold `.gitkeep`
- `ui/theme/{Color,Theme,Type}.kt` — rewritten per `design.md` palette/typography (dynamic/system color intentionally disabled)
- `data/local/datastore/UserPreferencesRepository.kt` — DataStore-backed language + Ollama server URL
- `util/LocaleManager.kt` — builds a locale-scoped `Context` + RTL check, applied via `CompositionLocalProvider` (no Activity recreation needed)
- `ui/FR1App.kt` — root composable wiring locale/RTL + theme + nav graph
- `ui/navigation/{Routes,FR1NavGraph}.kt` — nav graph: splash → language select (first launch only) → home → 6 feature routes
- `ui/splash/{SplashScreen,SplashViewModel}.kt`, `ui/onboarding/{LanguageSelectScreen,LanguageSelectViewModel}.kt`, `ui/home/HomeScreen.kt`, `ui/settings/{SettingsScreen,SettingsViewModel}.kt` (functional: language toggle + Ollama URL field), `ui/components/{NavCard,PlaceholderScreen}.kt`
- `ui/{woundassessment,chatbot,guidance,emergency,history}/*Screen.kt` — thin stub screens using `PlaceholderScreen`, real implementations come in their respective phases
- `res/values/strings.xml` + `res/values-ur/strings.xml` — full EN/UR parity for every Phase 1 string

## Files In Progress
*(none yet)*

## Completed Phases
- [x] Phase 0 — Environment & Project Setup
- [x] Phase 1 — Foundations: Theme, Navigation, Bilingual Scaffold
- [x] Phase 2 — Wound Assessment (Rule Engine + UI)
- [x] Phase 3 — Treatment Guidance Library
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
- `androidx.lifecycle:*` pinned to `2.9.0` (not the newer `2.11.0`) because `lifecycle-runtime-compose`/`lifecycle-viewmodel-compose` 2.11.0 requires compileSdk 37, but this project compiles against 36.1. Revisit if compileSdk is bumped later.
- Bilingual language switching is implemented **without** an Activity recreation: `LocaleManager` builds a locale-scoped `Context` and `FR1App` provides it (plus `LocalLayoutDirection`) via `CompositionLocalProvider`, wrapping the whole nav graph. This means every `stringResource()` call re-resolves instantly on language change — verified live on-device (see Session 2). No `androidx.appcompat` dependency needed.
- Font bundling (Noto Sans / Noto Naskh Urdu) from `design.md` section 3 is deferred — currently using `FontFamily.Default`, which already renders Urdu correctly via Android's built-in Noto fallback. `res/font/` exists but is empty. Revisit during Phase 8 polish if time allows; not a blocker for any acceptance criteria so far.
- Settings screen is more functional than a Phase 1 "placeholder" strictly implies (real language toggle + Ollama URL field wired to DataStore) — done deliberately since Phase 1's acceptance criteria required a working, testable language switch somewhere in the app. Phase 6 will still add the "test connection" ping button.
- `LocaleManager.localizedContext()` originally just returned `base.createConfigurationContext(config)` directly. That return value is a standalone `Context`, not a `ContextWrapper` chained back to the host Activity — anything that unwraps `LocalContext.current` looking for an Activity (e.g. `rememberLauncherForActivityResult` / the camera permission launcher used by `CameraCaptureScreen`) broke as a result. Fixed by introducing `LocalizedContextWrapper`, a `ContextWrapper(base)` that only overrides `getResources()`. Keep this in mind for any future `Context`-unwrapping API (e.g. other `ActivityResultContracts`, `findActivity()` helpers) — they all need the wrapper chain intact.
- Wound assessment always asks all 5 questions before scoring (no early-exit UI even though the rule engine would short-circuit on the first red-flag answer) — deliberate, so the flow feels like a consistent checklist rather than jumping around; the rule engine itself still short-circuits internally.
- Camera preview shows the emulator's default synthetic test-pattern feed (colored squares), not a black screen — that's the AVD's simulated camera, not a bug. Verified capture → `FileProvider` → return-to-assessment-screen round trip works with no crashes on `FR1_Test`.
- `architecture.md`'s core-data-model sketch (`GuidanceEntry(id, category, titleEn, titleUr, stepsEn, stepsUr, iconRes)`) uses raw English/Urdu strings directly on the model. Deviated from that and used `@StringRes Int` fields instead (`titleRes`, `stepsRes: List<Int>`) pointing into `values/strings.xml` + `values-ur/strings.xml`, since `rules.md` explicitly requires string resources for all user-facing text with no hardcoded UI strings in Kotlin — that rule outranks the architecture doc's illustrative model shape. Icons are plain `ImageVector` (`Icons.Filled.*` from `material-icons-core`, the only icons artifact pulled in) rather than `@DrawableRes`, matching how `SeverityResultScreen` already does severity icons.
- Guidance category → icon mapping only draws from `material-icons-core` (no `material-icons-extended` dependency exists yet): Warning (severe bleeding), Build (fracture/splint), Info (burns), Person (shock), Clear (choking), LocationOn (positioning), Favorite (CPR). Revisit if `material-icons-extended` gets added later and a more literal icon (flame, lungs, heartbeat) is wanted.
- Wound Assessment → Guidance deep-linking: `WoundSeverityRuleEngine.recommendedGuidanceCategory()` mirrors `assess()`'s priority order (unconscious → POSITIONING, breathing difficulty → CPR, heavy bleeding → SEVERE_BLEEDING, bone/muscle visible → FRACTURE, light bleeding/large wound → SEVERE_BLEEDING) and returns `null` only for a clean Minor result. This is a second SIMULATED mapping layered on the same answers, not persisted anywhere — it only decides where the Severity Result screen's "View Guidance" button navigates.

## Session Log
*(append one entry per work session)*

### Session 1 — 2026-07-22
- Started: Phase 0 — Environment & Project Setup
- Completed: Verified existing AS-generated project skeleton (package `com.fr1.companion`, minSdk 26, Compose). Added Retrofit/OkHttp, Room+KSP, DataStore, CameraX, Coroutines, Navigation Compose to the version catalog and app module. Scaffolded the full `architecture.md` package/resource folder structure. Confirmed `assembleDebug` builds successfully. Initialized git repo and made the initial commit. Set up Android SDK cmdline-tools, an API 35 system image, and an AVD (`FR1_Test`); installed the debug APK and confirmed via logcat + screenshot that the app launches with no crash.
- Next up: Phase 1 — theme, navigation graph, LocaleManager, bilingual strings, Home screen, DataStore setup

### Session 2 — 2026-07-22
- Started: Phase 1 — Foundations: Theme, Navigation, Bilingual Scaffold
- Completed: Built `Color.kt`/`Theme.kt`/`Type.kt` per `design.md`; DataStore-backed `UserPreferencesRepository` (language + Ollama URL); `LocaleManager` for runtime locale/RTL switching without Activity recreation; full nav graph (Splash → Language Select (first launch only) → Home → Wound Assessment/Chatbot/Guidance/Emergency/History/Settings); Settings screen with a real, working language toggle and Ollama URL field; EN/UR string parity for every new string. Verified live on the `FR1_Test` emulator: cold launch → language select → Home → stub screen navigation → Settings → live EN⇄UR switch with correct RTL mirroring (back arrow flips, layout reverses, all strings translate) and no crashes anywhere in the flow.
- Next up: Phase 2 — Wound Assessment rule engine + guided-question UI

### Session 3 — 2026-07-22
- Started: Phase 2 — Wound Assessment (Rule Engine + UI)
- Completed: `WoundSeverityRuleEngine` (fixed decision tree over 5 answers → Minor/Moderate/Severe); `WoundAssessmentViewModel` driving a 5-question guided flow; `WoundAssessmentScreen` (Yes/No + bleeding-level questions, optional photo attach, progress indicator, disclaimer footer); `CameraCaptureScreen` (CameraX preview, permission handling, `FileProvider`-backed capture to app-private storage); `SeverityResultScreen` (color-coded badge + severity-specific next actions); new `severity_result/{severity}` nav route; full EN/UR string parity. Found and fixed a real bug along the way: `LocaleManager`'s localized `Context` wasn't a proper `ContextWrapper`, which silently broke the camera permission launcher — see Notes & Deviations. Rebuilt (`assembleDebug`, clean pass), reinstalled on `FR1_Test`, and manually walked the entire flow on-device: Home → Wound Assessment → all 5 questions → camera capture (permission grant → preview → capture → returns "Retake Photo" state) → Severe result → Emergency Alert/Guidance/Home actions all present, no crashes in logcat. Also re-verified the full flow in Urdu (RTL mirrors correctly, all new strings translated, button order flips as expected).
- Next up: Phase 3 — Treatment Guidance Library

### Session 4 — 2026-07-22
- Started: Phase 3 — Treatment Guidance Library
- Completed: `GuidanceCategory`/`GuidanceEntry` models; bundled `GuidanceContent` object with bilingual EN/UR content for all 7 categories (severe bleeding, fractures, burns, shock, choking, positioning, CPR overview), each 5-6 short plain-language steps, safety-boundary-compliant (no meds, no invasive procedures, always pushes to professional/emergency help); `GuidanceListScreen` (icon + title cards) and `GuidanceDetailScreen` (numbered steps + disclaimer); extended `WoundSeverityRuleEngine` with a `recommendedGuidanceCategory()` mapping so Severe/Moderate wound-assessment results deep-link straight to the matching guidance page instead of the general list (Minor-with-no-red-flags still falls back to the list). Rebuilt clean, reinstalled on `FR1_Test`, and manually verified on-device: browsing all 7 categories from Home in Urdu (RTL-correct icons/steps/numbering), and the full Wound Assessment → Severe (bone/muscle visible) → Severity Result → "View Guidance" → deep-link straight to Fractures & Immobilization, with no crashes in logcat throughout.
- Next up: Phase 4 — Chatbot (Ollama Integration + Fallback)
