# FR1 Companion App — Architecture

> Note: named `architecture.md` (standard spelling) so tooling/imports don't trip over it — content matches what you asked for as "archietecture.md."

## 1. High-Level App Flow

```
Splash Screen
     ↓
Language Select (first launch only) → saved in DataStore
     ↓
Home Screen
   ├── Wound Assessment → Photo + guided questions → Rule engine → Severity Result
   │        ├── (Severe)            → Emergency Alert Screen
   │        └── (Minor / Moderate)  → Relevant Treatment Guidance page
   ├── Chatbot → Ollama reachability check → Chat screen (Live badge or Fallback badge)
   ├── Treatment Guidance Library → Category list → Step-by-step detail
   ├── Emergency Alert → Call action / Simulated "alert professional" action
   ├── Incident History → List of past local assessments
   └── Settings → Ollama server IP, language
```

## 2. Tech Stack

| Layer | Choice | Why |
|---|---|---|
| Language | Kotlin | Standard modern Android |
| UI | Jetpack Compose + Material 3 (custom theme) | Faster to build than XML; good for rapid 48h iteration with Claude Code |
| Architecture pattern | MVVM (ViewModel + StateFlow) | Simple, testable, standard, pairs well with Compose |
| Local DB | Room | Incident history, fully offline |
| Preferences | DataStore | Language choice, Ollama IP, settings |
| Networking | Retrofit + OkHttp (or Ktor client) | HTTP calls to the local Ollama REST API |
| Async | Kotlin Coroutines + Flow | Standard for network/DB/camera calls |
| Camera | CameraX | Photo capture for wound assessment |
| Dependency Injection | Manual DI / lightweight service locator (Hilt only if time remains — see `rules.md`) | Keep it simple given the time budget |
| Min SDK | API 26 (Android 8.0) | Broad device compatibility for demo hardware |
| Build | Gradle (Kotlin DSL) | Standard |

### Ollama Integration Details
- Ollama runs on a laptop on the **same local WiFi network** — no internet uplink required, but the laptop must be reachable on the network.
- The Ollama server IP/port is **configurable in Settings** (e.g. default placeholder `http://192.168.X.X:11434`) — never hardcode this, since the laptop's local IP will change between networks/demo locations.
- On opening the chatbot: send a short, low-timeout (~2s) reachability check (e.g. `GET {ollama_url}/api/tags`).
  - **Success** → live mode, conversation goes through `POST {ollama_url}/api/chat`
  - **Timeout/failure** → fallback mode via the bundled `FallbackQAEngine` (keyword-matched Q&A, zero network)
- The chat UI must show a small, honest status badge — e.g. **🟢 Live AI** vs **🟡 Offline Mode** — so the mode is transparent during the defense demo (this also makes for a good talking point about the app's offline-first design).

## 3. Folder / File Structure

```
FR1CompanionApp/
├── app/
│   ├── src/main/
│   │   ├── java/com/fr1/companion/
│   │   │   ├── FR1Application.kt
│   │   │   ├── MainActivity.kt
│   │   │   │
│   │   │   ├── ui/
│   │   │   │   ├── theme/
│   │   │   │   │   ├── Color.kt
│   │   │   │   │   ├── Type.kt
│   │   │   │   │   └── Theme.kt
│   │   │   │   ├── components/            # shared buttons, cards, status badges
│   │   │   │   ├── splash/
│   │   │   │   ├── onboarding/            # language select
│   │   │   │   ├── home/
│   │   │   │   ├── woundassessment/
│   │   │   │   │   ├── WoundAssessmentScreen.kt
│   │   │   │   │   ├── WoundAssessmentViewModel.kt
│   │   │   │   │   └── SeverityResultScreen.kt
│   │   │   │   ├── chatbot/
│   │   │   │   │   ├── ChatScreen.kt
│   │   │   │   │   └── ChatViewModel.kt
│   │   │   │   ├── guidance/
│   │   │   │   │   ├── GuidanceListScreen.kt
│   │   │   │   │   └── GuidanceDetailScreen.kt
│   │   │   │   ├── emergency/
│   │   │   │   │   └── EmergencyAlertScreen.kt
│   │   │   │   ├── history/
│   │   │   │   │   └── HistoryScreen.kt
│   │   │   │   └── settings/
│   │   │   │       └── SettingsScreen.kt   # Ollama IP config, language
│   │   │   │
│   │   │   ├── domain/
│   │   │   │   ├── model/                  # WoundCase, Severity, ChatMessage, GuidanceEntry
│   │   │   │   ├── rules/
│   │   │   │   │   └── WoundSeverityRuleEngine.kt   # the rule-based "AI"
│   │   │   │   └── fallback/
│   │   │   │       └── FallbackQAEngine.kt          # scripted chatbot fallback
│   │   │   │
│   │   │   ├── data/
│   │   │   │   ├── local/
│   │   │   │   │   ├── db/                 # Room entities, DAOs, database
│   │   │   │   │   └── datastore/          # preferences
│   │   │   │   ├── network/
│   │   │   │   │   ├── OllamaApiService.kt
│   │   │   │   │   └── OllamaRepository.kt
│   │   │   │   └── content/
│   │   │   │       └── guidance_content.kt (or bundled JSON)
│   │   │   │
│   │   │   └── util/
│   │   │       ├── ConnectivityChecker.kt
│   │   │       └── LocaleManager.kt        # EN/UR switching, RTL handling
│   │   │
│   │   ├── res/
│   │   │   ├── values/strings.xml          # English
│   │   │   ├── values-ur/strings.xml       # Urdu
│   │   │   ├── font/                       # bundled fonts (see design.md)
│   │   │   ├── drawable/                   # icons
│   │   │   └── ...
│   │   └── AndroidManifest.xml
│   └── build.gradle.kts
├── gradle/
├── build.gradle.kts
├── settings.gradle.kts
└── README.md
```

## 4. Core Data Models

- `WoundCase(id, timestamp, photoUri?, answers: Map<String, Any>, severity: Severity, notes)`
- `Severity` enum: `MINOR, MODERATE, SEVERE`
- `ChatMessage(id, sender: USER | BOT, text, timestamp, mode: LIVE | FALLBACK)`
- `GuidanceEntry(id, category, titleEn, titleUr, stepsEn: List<String>, stepsUr: List<String>, iconRes)`

## 5. Key Architectural Decisions & Why

1. **Offline-first.** Every safety-critical path (guidance library, rule-based assessment, fallback chatbot) works with zero network. Ollama is an enhancement layer only, never a dependency.
2. **Rule-based, not ML, for wound severity.** Transparent, fast to build, easy to explain to a defense panel, and honest about not being a validated clinical tool.
3. **Configurable Ollama endpoint.** Local network IPs change between rooms/buildings; hardcoding will break on demo day.
4. **Compose over XML.** Faster iteration for a 48-hour build.
5. **No backend/cloud.** Removes an entire class of setup work (hosting, auth, deployment) the timeline can't afford.
6. **Bilingual by design, not bolted on.** Strings, fonts, and layout direction are planned from Phase 1 so Urdu isn't a rushed afterthought late in the build.
