# FR1 Companion App — Phased Build Plan (48-Hour Timeline)

Total budget: **48 hours**. This plan allocates **~40 hours of build work** and leaves **~8 hours of buffer** inside that window for breaks, sleep, food, and defense prep — a 48-hour deadline is not 48 hours of coding capacity, and pretending otherwise is how demos fail. Adjust hour counts if your real availability differs, but keep the phase **order** — each phase is a checkpoint, and skipping ahead with an earlier phase broken will cost more time than it saves.

Each phase has: **Goal**, **Tasks**, **Deliverable**, **Acceptance Criteria** (how you know it's actually done), and **Dependencies**.

---

## Phase 0 — Environment & Project Setup
**Time: ~2 hours**

**Goal:** A blank but correctly configured Android project that builds and runs.

**Tasks:**
- Create Android Studio project (Kotlin, Jetpack Compose template), package `com.fr1.companion`, min SDK 26
- Set up Gradle dependencies: Compose, Material 3, Retrofit/OkHttp, Room, DataStore, CameraX, Coroutines
- Set up folder structure per `architecture.md`
- Set up Git repo, initial commit
- Confirm the app builds and shows a blank screen on an emulator or device

**Deliverable:** Empty runnable app, correct project skeleton, first commit pushed.

**Acceptance Criteria:** App launches with no crash. Folder structure matches `architecture.md`.

**Dependencies:** None.

---

## Phase 1 — Foundations: Theme, Navigation, Bilingual Scaffold
**Time: ~5 hours**

**Goal:** The app's skeleton — navigable, themed, and language-aware — before any real feature is built.

**Tasks:**
- Implement `Color.kt`, `Type.kt`, `Theme.kt` per `design.md`
- Set up Compose Navigation graph with placeholder screens for: Splash, Language Select, Home, Wound Assessment, Chatbot, Guidance, Emergency, History, Settings
- Implement `LocaleManager` for EN/UR switching + RTL layout handling
- Create `values/strings.xml` and `values-ur/strings.xml` with placeholder strings for every screen
- Build the Home screen with navigation cards to each feature (even if destinations are stubs)
- Set up DataStore for persisting language choice and Ollama server IP

**Deliverable:** Navigable app shell — every screen reachable, empty but themed and correctly localized.

**Acceptance Criteria:** Switching language updates every visible string and layout direction correctly. Navigating between all placeholder screens works without crashes.

**Dependencies:** Phase 0 complete.

---

## Phase 2 — Wound Assessment (Rule Engine + UI)
**Time: ~6 hours**

**Goal:** A working, honest, rule-based wound severity assessment flow.

**Tasks:**
- Build `WoundSeverityRuleEngine` — decision tree mapping guided-question answers (+ optional photo attached, not analyzed) to `Severity`
- Design the guided-question flow (bleeding severity, visible bone, consciousness, etc.) — keep to ~4–6 questions max for demo speed
- Integrate CameraX for optional photo capture/attach (stored with the case, not analyzed by any model)
- Build `WoundAssessmentScreen` (question flow) and `SeverityResultScreen` (result + next-step routing)
- Wire Severe results to prompt/route to Emergency Alert; Minor/Moderate to the relevant Guidance category
- Add the required disclaimer text on both screens

**Deliverable:** End-to-end wound assessment flow from question to result to routing.

**Acceptance Criteria:** All input combinations produce a sensible, explainable severity tier. Disclaimer visible. Routing to Emergency/Guidance works for each tier.

**Dependencies:** Phase 1 complete (theme, navigation, strings).

---

## Phase 3 — Treatment Guidance Library
**Time: ~4 hours**

**Goal:** The offline reliability backbone — content that works no matter what else fails.

**Tasks:**
- Write bilingual content for each category: severe bleeding control, fractures/immobilization, burns, shock, choking, positioning, CPR overview (short, step-by-step, plain language)
- Model content as `GuidanceEntry` list (bundled JSON or Kotlin data, not a database — this content doesn't change at runtime)
- Build `GuidanceListScreen` (category cards) and `GuidanceDetailScreen` (step list)
- Link Wound Assessment results to the matching guidance category

**Deliverable:** Fully browsable, bilingual guidance library.

**Acceptance Criteria:** Every category has complete EN + UR content. Navigation from Wound Assessment result to the correct guidance category works.

**Dependencies:** Phase 1 complete. Can run in parallel with Phase 2 if you have two people working (you do — split with your teammate).

---

## Phase 4 — Chatbot (Ollama Integration + Fallback)
**Time: ~8 hours** (largest phase — budget accordingly)

**Goal:** A working hybrid chatbot: live via local Ollama when reachable, safely scripted when not.

**Tasks:**
- Build `OllamaApiService` (Retrofit) hitting `/api/tags` (reachability) and `/api/chat` (conversation)
- Build `FallbackQAEngine` — a curated set of common bystander questions mapped to safe, pre-written bilingual answers (this is your safety net; spend real effort here, not just a stub)
- Build `ChatViewModel`: on chat open, ping Ollama with a short timeout, set mode (LIVE/FALLBACK), route all messages through the correct engine
- Build `ChatScreen` UI with the Live/Offline status badge
- Add Ollama server IP field to `SettingsScreen`, wired to DataStore
- Test with Ollama actually running locally (pick a small, fast model — e.g. `llama3.2:1b` or `phi3:mini` — large models will be too slow for a live demo)
- Test the fallback path by deliberately pointing the app at an unreachable IP

**Deliverable:** Chatbot screen that works in both modes, with visible mode indicator.

**Acceptance Criteria:** With Ollama reachable, chat responses come from the live model. With Ollama unreachable, chat falls back cleanly with no crash/hang, and the badge reflects the true mode. Both languages work in both modes.

**Dependencies:** Phase 1 complete. Needs a laptop with Ollama installed and a small model pulled for testing — do this early, not the night before the demo.

---

## Phase 5 — Emergency Alert & Incident History
**Time: ~4 hours**

**Goal:** Close the loop on escalation and add the local history log.

**Tasks:**
- Build `EmergencyAlertScreen`: one-tap dial intent (e.g. Rescue 1122), simulated "Alert Nearby Medical Professional" action (can be a confirmation UI — no real backend needed)
- Set up Room database: `WoundCase` entity + DAO
- Save each completed Wound Assessment to the local DB
- Build `HistoryScreen` listing past cases with timestamp and severity

**Deliverable:** Emergency screen functional; history screen shows real saved cases.

**Acceptance Criteria:** Completing a Wound Assessment creates a visible entry in History. Emergency call intent opens the phone dialer correctly.

**Dependencies:** Phase 2 complete (need real WoundCase data to store).

---

## Phase 6 — Settings Finalization
**Time: ~2 hours**

**Goal:** Make the configurable pieces genuinely easy to adjust on demo day.

**Tasks:**
- Finalize `SettingsScreen`: Ollama IP input (with validation/format hint), language toggle, maybe a "test connection" button that pings Ollama and shows success/failure inline
- Persist all settings correctly across app restarts

**Deliverable:** Settings screen judges/advisors could plausibly use themselves.

**Acceptance Criteria:** Changing the Ollama IP and testing the connection works without needing to restart the app.

**Dependencies:** Phase 4 complete.

---

## Phase 7 — Integration Testing & Bug Fixing
**Time: ~5 hours**

**Goal:** Make the whole app behave like one coherent product, not five separate features.

**Tasks:**
- Full run-through of every flow, in both languages, in both Ollama modes
- Fix layout bugs, especially RTL edge cases in Urdu
- Verify no crashes on: no camera permission granted, no Ollama reachable, no prior history, fresh install
- Check performance on the actual demo device, not just an emulator

**Deliverable:** A stable build with no known crash paths.

**Acceptance Criteria:** You can run the full demo script (see Phase 8) three times in a row with no crashes or visual glitches.

**Dependencies:** Phases 2–6 complete.

---

## Phase 8 — Polish, APK Build, Demo Rehearsal
**Time: ~4 hours**

**Goal:** Ready to present.

**Tasks:**
- Final visual polish pass against `design.md` (spacing, icons, contrast)
- Build a signed/debug APK for the demo device(s)
- Write a **3-minute demo script**: language toggle → wound assessment (Severe path) → guidance library → chatbot (both live and fallback, if feasible to show both) → emergency alert
- Rehearse the demo at least twice, including a plan for "what if Ollama/WiFi doesn't connect during the defense" (fallback mode should make this a non-issue — treat it as a feature, not a risk)
- Prepare 1–2 backup talking points connecting the app back to the FYP proposal's AI features and functional requirements

**Deliverable:** Final APK, rehearsed demo script.

**Acceptance Criteria:** You (and your teammate) can run the full demo confidently without narrating around bugs.

**Dependencies:** Phase 7 complete.

---

## Suggested Parallelization (2-person team)
Since the team has two members, run these in parallel where possible:
- **Person A:** Phase 2 (Wound Assessment) → Phase 5 (Emergency/History)
- **Person B:** Phase 3 (Guidance Library) → Phase 4 (Chatbot/Ollama)
- Both: Phase 1 together first (shared foundation), then Phases 6–8 together (integration always needs both people).

## Future Work (explicitly not in this 48-hour scope)
- Real ML-based wound image classification
- Cloud sync / multi-device history
- iOS version
- Real integration with drone telemetry (e.g. auto-populating incident location from drone GPS drop coordinates)
