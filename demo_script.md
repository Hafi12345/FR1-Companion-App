# FR1 Companion App — Demo Script

> Companion to `phases.md` Phase 8. Target: under 3 minutes, rehearsed, no narrating around bugs.

---

## Pre-Demo Checklist

- [ ] Device fully charged, brightness up (judges' room lighting varies).
- [ ] App already installed; do a cold-start once before judges arrive so the OS doesn't show a first-install hiccup.
- [ ] Decide **before walking in** whether Ollama will be live for this run:
  - If yes: laptop on, model already pulled and warmed up (send it one throwaway prompt beforehand — first-token latency on a cold model is otherwise a visible stall), Settings → Ollama Server URL pointed at the laptop's current IP, "Test Connection" already shows green.
  - If no / unsure: don't fight it. Fallback mode **is** the demo's offline-first story — see "If Ollama/WiFi doesn't cooperate" below.
- [ ] Incident History has at least one entry already in it from a prior run, OR be ready to explain the empty state takes one Wound Assessment to populate (don't let an empty History screen look broken).
- [ ] Know which language the app is currently set to before you start talking, so the language toggle moment isn't a surprise to you either.

---

## The Script (~3 minutes)

### 1. Open on Home, toggle language (0:00–0:25)

- Launch the app (skip splash narration — just let it land on Home).
- Say: *"Everything in this app is bilingual by design, not translated as an afterthought — this matters because our target users are the Pakistani general public, and Urdu needs to be a first-class citizen."*
- Go to **Settings → tap اردو**. Don't just say it's bilingual — show the whole screen flip to RTL live: back arrows mirror, buttons reorder, text re-renders, no app restart.
- Toggle back to English (or stay in Urdu and keep narrating in Urdu if that's stronger for your panel — either works, the point already landed).

### 2. Wound Assessment — Severe path (0:25–1:10)

- Home → **Wound Assessment**.
- Answer toward a **Severe** result on purpose (fastest: answer "Yes" to bone/muscle visible, or "Heavy" bleeding) — Severe is the most visually dramatic tier and sets up the Emergency Alert handoff naturally.
- Tap **Attach Photo** once to show the CameraX capture flow works (permission prompt if not yet granted, then capture) — don't dwell on it, just prove it's real and not a stub.
- Say while answering: *"This is a rule-based decision tree, not a live ML model — that's a deliberate project decision, not a shortcut. It's transparent, fast, and I can point to the exact logic in `WoundSeverityRuleEngine` and explain every branch in one sentence each."*
- Land on the **Severity Result** screen — point out the color-coded badge (never color alone — always paired with an icon and a label) and the visible disclaimer text.

### 3. Escalation: Emergency Alert (1:10–1:40)

- Tap **Go to Emergency Alert** from the Severe result.
- Tap **Call 1122** — show the phone dialer opens pre-filled (don't actually place the call). Say: *"This uses `ACTION_DIAL`, not `ACTION_CALL` — it opens the dialer but a human still has to press Call. We're not silently placing calls on someone's behalf."*
- Tap **Alert Nearby Medical Professional** — show the simulated confirmation with the 5–10 minute estimate. Say: *"This directly represents the FYP proposal's 'alert nearby medical professional within 5–10 minutes' requirement — simulated here since there's no real dispatch backend in this phase, but the UI and flow are exactly where a real integration would slot in."*

### 4. Treatment Guidance Library (1:40–2:05)

- Back to Home → **Treatment Guidance**.
- Say: *"This is the reliability backbone — it never depends on network or Ollama. Even if everything else in the app failed, this still works."*
- Open one category (Severe Bleeding Control or CPR Overview read well visually) — show the numbered steps, plain language, no jargon.
- Mention in passing: *"A Severe or Moderate result actually deep-links straight into the matching category here instead of dropping you on a generic list — the routing logic is in the same rule engine as the severity assessment."*

### 5. Chatbot — mode badge is the whole point (2:05–2:40)

- Home → **Chatbot**.
- Whatever mode it's in, **name the badge out loud immediately** — don't let the judges wonder if it's supposed to look like that: *"Offline Mode"* (amber) or *"Live AI"* (green).
- Type one plain-language question (e.g. *"how do I stop bleeding"*) and show the response come back.
- If in Fallback: *"This is the safety net — a curated, keyword-matched Q&A engine, zero network. It never mentions medication or invasive procedures, and it always pushes toward calling emergency services when in doubt."*
- If in Live: show the Ollama response, then say: *"Every message also carries a system prompt constraining the model to the same safety boundaries as the fallback engine — no diagnostic certainty, no medication advice."*

### 6. Incident History — close the loop (2:40–3:00)

- Home → **Incident History**.
- Point at the entry just created: *"Every completed assessment is saved locally via Room — no cloud, nothing leaves the device."* Point out severity + timestamp.

---

## If Ollama/WiFi Doesn't Cooperate

**Treat this as a feature, not a risk** — it's explicitly designed for this.

- If the chatbot opens in Offline Mode when you expected Live: don't apologize or fumble with WiFi settings live. Say the line above about the safety net and move on — the whole architectural point of this app is that it degrades gracefully. A judge watching you calmly demonstrate the fallback path is arguably a *better* demo moment than a flawless live-AI response, because it proves the offline-first claim rather than just asserting it.
- If you do want to attempt reconnecting: Settings has a **Test Connection** button that pings inline without leaving the screen or restarting the app — use that once, and if it's still red, drop it and move on. Don't loop on it.
- Never let a network hiccup become the story of the demo. The fallback path working *is* the story.

---

## Backup Talking Points (FYP Proposal Tie-Back)

Use one or two of these if there's Q&A time or a lull:

1. **"Why rule-based instead of a real ML model for wound severity?"** — Deliberate scope decision (see `rules.md`), not a limitation we ran out of time to fix. A transparent decision tree is explainable in a defense setting in a way a black-box model isn't, and it's honest about not being a certified diagnostic tool — the disclaimer says so on-screen. Real ML wound classification is explicitly listed as future work.
2. **"How does this connect to the actual FR1 drone?"** — This app is the companion to the drone drop: the drone delivers medical supplies to an accident site where there's no trained responder yet, and this app turns the bystander who receives the package into a capable first responder for the critical minutes before an ambulance arrives. The Emergency Alert screen's "alert nearby medical professional within 5–10 minutes" action is a direct, simulated stand-in for that escalation requirement in the original proposal.
3. **"What's actually offline vs. what needs network?"** — Only the chatbot's *live* mode needs network (local WiFi to a laptop running Ollama — never internet). Wound assessment, the guidance library, emergency alert, incident history, and the chatbot's fallback mode all work with zero connectivity. That split is deliberate and demonstrated live in this demo, not just claimed in a slide.
