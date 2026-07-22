package com.fr1.companion.ui.navigation

object Routes {
    const val SPLASH = "splash"
    const val LANGUAGE_SELECT = "language_select"
    const val HOME = "home"
    const val WOUND_ASSESSMENT = "wound_assessment"
    const val SEVERITY_RESULT_ARG = "severity"
    const val SEVERITY_RESULT_CATEGORY_ARG = "category"
    const val SEVERITY_RESULT_NO_CATEGORY = "none"
    const val SEVERITY_RESULT =
        "severity_result/{$SEVERITY_RESULT_ARG}/{$SEVERITY_RESULT_CATEGORY_ARG}"
    const val CHATBOT = "chatbot"
    const val GUIDANCE = "guidance"
    const val GUIDANCE_DETAIL_ARG = "category"
    const val GUIDANCE_DETAIL = "guidance_detail/{$GUIDANCE_DETAIL_ARG}"
    const val EMERGENCY = "emergency"
    const val HISTORY = "history"
    const val SETTINGS = "settings"

    fun severityResult(severity: String, category: String?) =
        "severity_result/$severity/${category ?: SEVERITY_RESULT_NO_CATEGORY}"

    fun guidanceDetail(category: String) = "guidance_detail/$category"
}
