package com.fr1.companion.domain.fallback

import com.fr1.companion.R

// SIMULATED: keyword-matched scripted answers, zero network — this is the
// reliability floor of the chatbot when no Ollama server is reachable, so
// answers are deliberately simple and conservative rather than clever. Never
// mentions medication/dosages, never gives invasive instructions, and always
// pushes toward calling emergency services when in doubt (see rules.md).
object FallbackQAEngine {

    private data class Entry(val keywords: List<String>, val answerRes: Int)

    // Medication/dosage questions are checked first and unconditionally
    // refused, regardless of what other keywords (e.g. "burn") also appear.
    private val entries = listOf(
        Entry(
            listOf("medicine", "medication", "painkiller", "tablet", "dose", "dosage", "injection", "drug", "دوا", "گولی", "ٹیکہ"),
            R.string.fallback_answer_medication,
        ),
        Entry(
            listOf("bleed", "bleeding", "blood", "خون"),
            R.string.fallback_answer_bleeding,
        ),
        Entry(
            listOf("fracture", "broken", "break", "bone", "فریکچر", "ہڈی", "ٹوٹ"),
            R.string.fallback_answer_fracture,
        ),
        Entry(
            listOf("burn", "burned", "burnt", "scald", "جل", "جھلس"),
            R.string.fallback_answer_burns,
        ),
        Entry(
            listOf("unconscious", "unresponsive", "not waking", "passed out", "faint", "بے ہوش"),
            R.string.fallback_answer_unconscious,
        ),
        Entry(
            listOf("not breathing", "no pulse", "cpr", "compressions", "heart stopped", "سانس نہیں", "دل بند", "سی پی آر"),
            R.string.fallback_answer_cpr,
        ),
        Entry(
            listOf("choking", "choke", "stuck in throat", "can't breathe", "دم گھٹ", "پھنس"),
            R.string.fallback_answer_choking,
        ),
        Entry(
            listOf("shock", "pale", "clammy", "cold sweat", "dizzy", "صدمہ", "شاک"),
            R.string.fallback_answer_shock,
        ),
        Entry(
            listOf("ambulance", "call emergency", "is this serious", "emergency number", "ایمبولینس"),
            R.string.fallback_answer_call_ambulance,
        ),
        Entry(
            listOf("what should i do", "help me", "what now", "first aid", "کیا کروں", "مدد"),
            R.string.fallback_answer_general,
        ),
    )

    fun answerRes(userMessage: String): Int {
        val lower = userMessage.lowercase()
        val match = entries.firstOrNull { entry -> entry.keywords.any { lower.contains(it) } }
        return match?.answerRes ?: R.string.fallback_answer_default
    }
}
