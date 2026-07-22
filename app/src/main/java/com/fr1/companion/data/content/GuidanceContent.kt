package com.fr1.companion.data.content

import com.fr1.companion.R
import com.fr1.companion.domain.model.GuidanceCategory
import com.fr1.companion.domain.model.GuidanceEntry

// Bundled at build time — this content doesn't change at runtime, so a database is unnecessary.
object GuidanceContent {

    val entries: List<GuidanceEntry> = listOf(
        GuidanceEntry(
            category = GuidanceCategory.SEVERE_BLEEDING,
            titleRes = R.string.guidance_category_severe_bleeding,
            stepsRes = listOf(
                R.string.guidance_severe_bleeding_step_1,
                R.string.guidance_severe_bleeding_step_2,
                R.string.guidance_severe_bleeding_step_3,
                R.string.guidance_severe_bleeding_step_4,
                R.string.guidance_severe_bleeding_step_5,
                R.string.guidance_severe_bleeding_step_6,
            ),
        ),
        GuidanceEntry(
            category = GuidanceCategory.FRACTURE,
            titleRes = R.string.guidance_category_fracture,
            stepsRes = listOf(
                R.string.guidance_fracture_step_1,
                R.string.guidance_fracture_step_2,
                R.string.guidance_fracture_step_3,
                R.string.guidance_fracture_step_4,
                R.string.guidance_fracture_step_5,
                R.string.guidance_fracture_step_6,
            ),
        ),
        GuidanceEntry(
            category = GuidanceCategory.BURNS,
            titleRes = R.string.guidance_category_burns,
            stepsRes = listOf(
                R.string.guidance_burns_step_1,
                R.string.guidance_burns_step_2,
                R.string.guidance_burns_step_3,
                R.string.guidance_burns_step_4,
                R.string.guidance_burns_step_5,
                R.string.guidance_burns_step_6,
            ),
        ),
        GuidanceEntry(
            category = GuidanceCategory.SHOCK,
            titleRes = R.string.guidance_category_shock,
            stepsRes = listOf(
                R.string.guidance_shock_step_1,
                R.string.guidance_shock_step_2,
                R.string.guidance_shock_step_3,
                R.string.guidance_shock_step_4,
                R.string.guidance_shock_step_5,
                R.string.guidance_shock_step_6,
            ),
        ),
        GuidanceEntry(
            category = GuidanceCategory.CHOKING,
            titleRes = R.string.guidance_category_choking,
            stepsRes = listOf(
                R.string.guidance_choking_step_1,
                R.string.guidance_choking_step_2,
                R.string.guidance_choking_step_3,
                R.string.guidance_choking_step_4,
                R.string.guidance_choking_step_5,
            ),
        ),
        GuidanceEntry(
            category = GuidanceCategory.POSITIONING,
            titleRes = R.string.guidance_category_positioning,
            stepsRes = listOf(
                R.string.guidance_positioning_step_1,
                R.string.guidance_positioning_step_2,
                R.string.guidance_positioning_step_3,
                R.string.guidance_positioning_step_4,
                R.string.guidance_positioning_step_5,
            ),
        ),
        GuidanceEntry(
            category = GuidanceCategory.CPR,
            titleRes = R.string.guidance_category_cpr,
            stepsRes = listOf(
                R.string.guidance_cpr_step_1,
                R.string.guidance_cpr_step_2,
                R.string.guidance_cpr_step_3,
                R.string.guidance_cpr_step_4,
                R.string.guidance_cpr_step_5,
                R.string.guidance_cpr_step_6,
            ),
        ),
    )

    fun entryFor(category: GuidanceCategory): GuidanceEntry =
        entries.first { it.category == category }
}
