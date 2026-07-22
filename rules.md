# FR1 Companion App — Development Rules

These rules apply to all code generated for this project. Follow them exactly — they exist to keep a 48-hour build shippable, not to slow you down.

## 1. Do

- **Kotlin + Jetpack Compose only.** No XML layouts, no Java.
- **MVVM**: UI (Composable) → ViewModel (StateFlow) → Repository/UseCase → Data source. Keep Composables free of business logic.
- **String resources for ALL user-facing text** — every string must exist in both `values/strings.xml` (English) and `values-ur/strings.xml` (Urdu). Never hardcode UI text in Kotlin files.
- **Support RTL** for Urdu screens — test both language directions before marking a screen "done," not just at the end of the project.
- **Graceful degradation everywhere.** Any network call (Ollama) must have a timeout (~2–3s) and a defined fallback path. The app must never hang or crash waiting on network.
- **Clear separation** between the rule-based wound severity engine (`domain/rules/`) and everything else — you should be able to point to this code and explain the logic simply during the defense.
- **Comment the "why" on all simulated/rule-based logic** — e.g. `// SIMULATED: for demo purposes; not a clinically validated model`.
- **Test each phase's flow before moving to the next phase** (see `phases.md`) — don't let errors compound.
- **Commit after each completed phase** with a clear message referencing the phase (e.g. `"Phase 2: wound assessment rule engine + UI complete"`).
- **Keep the disclaimer visible** on the Wound Assessment and Chatbot screens: this is a simulated/demo tool, not a certified medical device.
- **Update `memory.md`** after every work session (see that file's own instructions).

## 2. Avoid

### Libraries / Scope
- Do **not** pull in TensorFlow Lite, ML Kit, PyTorch Mobile, or any real image-classification model. Wound assessment is explicitly rule-based per project decision — a real model burns time we don't have and wasn't asked for.
- Do **not** add a backend server, cloud database, or authentication system (Firebase Auth, etc.). Everything is local (Room + DataStore) except the optional local Ollama call.
- Do **not** introduce Dagger/Hilt unless there's genuine spare time in Phase 6+ polish. Manual dependency injection is fine and faster to get right under this deadline.
- Do **not** add unrelated "nice to have" libraries (analytics SDKs, crash reporters, ad libraries) — none of these help the FYP defense.

### Error Handling
- Do **not** let an Ollama network failure propagate as a crash or a blank screen. Every failure path must resolve to the `FallbackQAEngine`.
- Do **not** silently swallow errors either — log them (Logcat) so they're debuggable, and show the user a small, non-alarming status indicator ("Offline Mode") rather than an error dialog for the expected case of no Ollama server reachable.
- Do **not** block the main/UI thread for camera, file I/O, DB, or network operations — always use coroutines/Flow.

### AI / Content Boundaries
The chatbot and wound assessment must **never**:
- Recommend or discuss medication names, dosages, or injections
- Give guidance beyond basic first-aid stabilization (no surgical/invasive instructions)
- Claim diagnostic certainty ("this is definitely a fracture") — always frame as guidance, and for Severe cases, always push toward calling professional help
- Omit the disclaimer that this is a simulated academic prototype, not a certified medical device

Keep the **scripted fallback answers** conservative and safe — they represent the worst-case reliability floor of the whole app, so they should be simple and correct rather than clever. If in doubt about a rule or chatbot response boundary, default to: **"advise calling emergency services"** rather than more specific medical instruction.

## 3. Scope Discipline
- If a feature isn't in `prd.md`, don't build it, no matter how good the idea — the timeline has no slack. Note ideas under a "future work" list instead of building them ad hoc.
- Follow `phases.md` in order. Don't start a new phase's work with the previous phase unfinished or untested.
