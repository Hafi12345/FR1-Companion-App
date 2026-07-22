package com.fr1.companion.util

import android.content.Context
import android.content.res.Configuration
import android.text.TextUtils
import android.view.View
import java.util.Locale

/**
 * Applies the app's selected language at runtime without an Activity recreation.
 * Wrap the composable tree with the returned context via
 * `CompositionLocalProvider(LocalContext provides ...)` so `stringResource()`
 * resolves against the right locale, and pair with [isRtl] for layout direction.
 */
object LocaleManager {

    fun localizedContext(base: Context, languageCode: String): Context {
        val locale = Locale.forLanguageTag(languageCode)
        val config = Configuration(base.resources.configuration)
        config.setLocale(locale)
        return base.createConfigurationContext(config)
    }

    fun isRtl(languageCode: String): Boolean {
        return TextUtils.getLayoutDirectionFromLocale(Locale.forLanguageTag(languageCode)) == View.LAYOUT_DIRECTION_RTL
    }
}
