package com.fr1.companion.util

import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import android.content.res.Resources
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

    /**
     * A plain `createConfigurationContext()` result is a standalone Context, not a
     * `ContextWrapper` chained back to the Activity — anything that unwraps
     * `LocalContext.current` to find the Activity (e.g. permission/camera launchers
     * via `LocalActivityResultRegistryOwner`) breaks. Wrapping [base] and only
     * overriding [getResources] keeps that chain intact while still localizing
     * `stringResource()`.
     */
    private class LocalizedContextWrapper(
        base: Context,
        private val localizedResources: Resources,
    ) : ContextWrapper(base) {
        override fun getResources(): Resources = localizedResources
    }

    fun localizedContext(base: Context, languageCode: String): Context {
        val locale = Locale.forLanguageTag(languageCode)
        val config = Configuration(base.resources.configuration)
        config.setLocale(locale)
        val localizedResources = base.createConfigurationContext(config).resources
        return LocalizedContextWrapper(base, localizedResources)
    }

    fun isRtl(languageCode: String): Boolean {
        return TextUtils.getLayoutDirectionFromLocale(Locale.forLanguageTag(languageCode)) == View.LAYOUT_DIRECTION_RTL
    }
}
