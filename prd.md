# FR1 Companion App — Product Requirements Document (PRD)

> Companion Android app to the FR1 (First Responder 1) medical supply drone — Final Year Project, Department of Artificial Intelligence, School of Systems and Technology (SST), UMT Lahore.
> This document defines **WHAT** to build. See `architecture.md` for **HOW**, `phases.md` for **WHEN**, `rules.md` for **constraints**, `design.md` for **look & feel**.

---

## 1. Problem Being Solved

When the FR1 drone drops a medical supply package near an accident/incident site, there is usually no trained medical professional present yet — only untrained bystanders. They have supplies, but not the knowledge to use them safely or effectively, and they're likely panicked.

The **FR1 Companion App** turns an untrained bystander into a capable first responder for the critical minutes before an ambulance or paramedic arrives.

## 2. What To Build

A **native Android application** that:

1. Lets a bystander photograph/describe a wound and get a **severity assessment** — simulated/rule-based logic for this project phase, not a clinical diagnostic tool.
2. Provides a **bilingual (English + Urdu) AI chatbot** that walks the bystander step-by-step through stabilizing the patient (stop bleeding, positioning, safety) — powered by a **local Ollama model** over the local WiFi network, with a scripted rule-based fallback if no Ollama server is reachable.
3. Offers a **static treatment guidance library** (bleeding control, fractures, burns, shock, CPR basics) as a reliability backbone that always works, fully offline.
4. Provides an **Emergency Alert screen** to quickly call/contact emergency services (e.g. Rescue 1122) and represents the FYP's "alert nearby medical professional within 5–10 minutes" requirement (simulated for this app).
5. Works **fully offline** for all core safety-critical features. Network (for the Ollama chatbot) is an enhancement, never a dependency.

**Explicitly out of scope for this app:** drone flight control, drone hardware integration, GPS drone navigation — these belong to the drone side of FR1, not the companion app.

## 3. Target Users

| User | Context | Needs from the App |
|---|---|---|
| **Primary: Untrained bystander/citizen** | At an accident/emergency site, stressed, no medical training, phone in hand | Simple, high-contrast, fast instructions; no jargon; works with poor/no internet; usable one-handed under stress |
| **Secondary: Medical professional (remote)** | Notified when a case is too severe for bystander handling | Clear signal that a case needs escalation (simulated in this version) |
| **Tertiary: FYP evaluation panel** | Judging the defense/demo | A working, polished, coherent demonstration of the 3 AI-adjacent features tied to the FR1 narrative |

Target users are general members of the public in Pakistan — **Urdu language support is a first-class requirement, not an afterthought.**

## 4. Core Features

### 4.1 Wound Assessment (Simulated/Rule-Based)
- Bystander takes/selects a photo and answers a few guided questions (bleeding: yes/no, heavy/light; visible bone: yes/no; patient conscious: yes/no; etc.)
- App runs **rule-based logic** (decision tree, not a live ML model) to output a severity tier: **Minor / Moderate / Severe**
- Each tier routes to a specific guidance path; **Severe** auto-suggests the Emergency Alert flow
- Must carry a visible disclaimer: *"This is a simulated assessment for demonstration purposes and does not replace professional medical judgment."*

### 4.2 AI Chatbot Guide (Ollama-powered, hybrid fallback)
- Conversational assistant answering plain-language questions ("How do I stop the bleeding?", "Is it safe to move them?")
- Before first use, the app pings a configured local Ollama server (same WiFi network)
  - **Reachable** → live LLM conversation via Ollama
  - **Unreachable** → falls back to a **scripted rule-based Q&A** engine bundled in the app
- Bilingual: responds in the language the user selected
- Chatbot must stay within safe first-aid boundaries — see `rules.md`

### 4.3 Treatment Guidance Library (Static, Always-Available)
- Offline content covering: severe bleeding control, fractures/immobilization, burns, shock, choking, positioning, CPR overview
- Organized by category, simple icons, short steps, bilingual text
- This is the **reliability backbone** — never depends on network or Ollama

### 4.4 Emergency Alert Screen
- One-tap "Call Emergency Services" (e.g. Rescue 1122)
- Simulated "Alert Nearby Medical Professional" action, representing the 5–10 minute escalation requirement
- Auto-triggered/suggested when Wound Assessment returns "Severe"

### 4.5 Language Toggle
- English / Urdu switch, persisted across sessions
- Full RTL layout support for Urdu screens

### 4.6 Local Incident Log (nice-to-have, lower priority)
- Stores past assessments locally (Room database) — useful for the bystander and for showing judges a "history" during demo
- No cloud sync — fully local, respecting offline-first design

## 5. Success Criteria for the FYP Defense
- App installs and runs on a physical Android device or emulator without crashing
- All 3 AI-adjacent features are demonstrable live: wound assessment, chatbot, guidance library
- App works with **zero internet connection** except the optional Ollama chatbot enhancement
- Bilingual switch works cleanly on every screen
- Visual design reads as "emergency medical" at a glance
- Full demo flow runs start-to-finish in under 3 minutes

## 6. Explicit Non-Goals (for this 48-hour build)
- No real clinical wound-detection ML model (explicitly rule-based per project decision)
- No cloud backend, user accounts, or authentication
- No iOS version
- No real integration with actual drone hardware/telemetry
- No production-grade medical certification or liability coverage — this is an academic prototype
