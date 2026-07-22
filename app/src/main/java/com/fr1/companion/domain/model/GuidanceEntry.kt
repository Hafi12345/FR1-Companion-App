package com.fr1.companion.domain.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Warning
import androidx.compose.ui.graphics.vector.ImageVector

enum class GuidanceCategory(val icon: ImageVector) {
    SEVERE_BLEEDING(Icons.Filled.Warning),
    FRACTURE(Icons.Filled.Build),
    BURNS(Icons.Filled.Info),
    SHOCK(Icons.Filled.Person),
    CHOKING(Icons.Filled.Clear),
    POSITIONING(Icons.Filled.LocationOn),
    CPR(Icons.Filled.Favorite),
}

data class GuidanceEntry(
    val category: GuidanceCategory,
    val titleRes: Int,
    val stepsRes: List<Int>,
)
