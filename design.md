# FR1 Companion App — Design System

Distinct emergency-medical theme: urgency-focused, high contrast, legible under stress and in bright outdoor daylight (accident sites are often outdoors). This is not a soft, calming health-and-wellness app — it should read as **"emergency response tool"** at a glance.

## 1. Color Palette

### Core
| Token | Hex | Use |
|---|---|---|
| `PrimaryRed` | `#D32F2F` | Primary brand color — app bar, primary buttons, key icons |
| `PrimaryRedDark` | `#B71C1C` | Pressed states, emphasis, Severe severity tag |
| `OnPrimary` | `#FFFFFF` | Text/icons on red surfaces |
| `Background` | `#FFFFFF` | Main app background (light mode default) |
| `Surface` | `#F5F5F5` | Cards, elevated surfaces |
| `TextPrimary` | `#1A1A1A` | Body text — near-black for max contrast, not pure black (softer on eyes) |
| `TextSecondary` | `#5C5C5C` | Secondary/help text |
| `Divider` | `#E0E0E0` | Borders, separators |

### Severity Indicators (Wound Assessment)
| Token | Hex | Meaning |
|---|---|---|
| `SeverityMinor` | `#2E7D32` (green) | Minor — self-manageable with guidance |
| `SeverityModerate` | `#F57C00` (amber) | Moderate — treat and monitor closely |
| `SeveritySevere` | `#C62828` (deep red) | Severe — escalate immediately |

### Status Badges (Chatbot)
| Token | Hex | Meaning |
|---|---|---|
| `LiveIndicator` | `#2E7D32` (green) | "🟢 Live AI" — Ollama reachable |
| `FallbackIndicator` | `#F9A825` (amber/yellow) | "🟡 Offline Mode" — scripted fallback active |

### Dark Mode
Support dark mode for low-light/nighttime scenes, but keep the red/white identity intact rather than muting it:
| Token | Hex |
|---|---|
| `Background (dark)` | `#121212` |
| `Surface (dark)` | `#1E1E1E` |
| `TextPrimary (dark)` | `#F5F5F5` |
| `PrimaryRed (dark)` | `#EF5350` (slightly lighter red for contrast against dark bg) |

**Rule:** Never rely on color alone to convey severity — always pair color with a label and an icon (accessibility, and also useful for colorblind users in an emergency context).

## 2. Theme

- **Base:** Material 3 (Compose `MaterialTheme`), with the palette above overriding default Material colors — do not use stock Material blue/purple anywhere.
- **Shape language:** Mostly rounded corners (12–16dp radius) for a modern, approachable feel — but the Emergency Alert screen's primary call button should be large, bold, and unmissable (think: a big red circular/pill button, not a subtle text link).
- **Elevation:** Keep elevation modest (cards at 2–4dp) — avoid heavy shadows that reduce contrast.
- **Iconography:** Simple, filled (not thin-line) icons for maximum legibility at a glance under stress — medical cross, alert triangle, heartbeat/pulse, phone-call icon, droplet (bleeding), bandage.
- **Touch targets:** Minimum 48dp height on all interactive elements — bigger than typical for a general consumer app, because hands may be shaking or gloved and users are not carefully aiming taps.
- **Motion:** Minimal, fast transitions (150–200ms) — avoid playful/bouncy animation; this is a utility, not a lifestyle app.

## 3. Typography

Because the app must render both **English (Latin)** and **Urdu (Perso-Arabic script, RTL)** cleanly, font choice matters more than usual.

| Language | Font | Why |
|---|---|---|
| English | **Noto Sans** (bundle as a font resource) | Clean, highly legible, wide weight range, free/open license, pairs visually with Noto Urdu fonts |
| Urdu | **Noto Naskh Urdu** or **Noto Sans Arabic** (bundle) | Naskh-style rendering stays legible at small UI sizes; avoid Nastaliq-style fonts (e.g. Jameel Noori Nastaleeq) for body/UI text — beautiful but hard to read small and awkward in tight UI layouts. Nastaliq is fine for a stylized logo/title only, if desired. |

### Scale (bigger than a typical consumer app — legibility under stress matters more than density)
| Style | Size | Use |
|---|---|---|
| Display / Screen Title | 26–28sp, bold | Screen headers ("Wound Assessment", "Emergency Alert") |
| Section Header | 20sp, semi-bold | Card/section titles |
| Body | 16–18sp, regular | Instructions, chat text, guidance steps |
| Button Label | 16sp minimum, bold | All buttons — never smaller |
| Caption/Disclaimer | 13sp, regular, `TextSecondary` | Disclaimers, timestamps |

**Rule:** Never go below 13sp anywhere in the app. This app may be read in bright sunlight, at arm's length, by a stressed person — err large.

## 4. Layout Notes
- Design mobile-first, portrait orientation as primary (landscape is not a priority for this timeline)
- RTL: when Urdu is active, mirror layout direction fully (icons that imply direction, like back arrows, should flip; icons that don't imply direction, like a medical cross, should not)
- Keep primary actions reachable in the lower two-thirds of the screen where possible — for one-handed use while the other hand may be occupied helping the patient
