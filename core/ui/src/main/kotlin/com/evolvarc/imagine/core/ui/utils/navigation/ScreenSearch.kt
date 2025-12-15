package com.evolvarc.imagine.core.ui.utils.navigation

import android.content.Context

/**
 * Searches through a list of Screens based on a query.
 *
 * Strategies:
 * 1. Checks Localized Title (via Context)
 * 2. Checks Localized Subtitle (via Context)
 * 3. Checks "Invisible" English Keywords (Screen.searchKeywords)
 */
fun searchScreens(
    query: String,
    screens: List<Screen>,
    context: Context
): List<Screen> {
    val normalizedQuery = query.trim().lowercase()
    if (normalizedQuery.isEmpty()) return emptyList()

    return screens.filter { screen ->
        // 1. Check localized title
        val titleMatch = if (screen.title != 0) {
            context.getString(screen.title).lowercase().contains(normalizedQuery)
        } else false

        // 2. Check localized subtitle
        val subtitleMatch = if (screen.subtitle != 0) {
            context.getString(screen.subtitle).lowercase().contains(normalizedQuery)
        } else false

        // 3. Check English keywords
        val keywordMatch = screen.searchKeywords().any { keyword ->
            keyword.lowercase().contains(normalizedQuery)
        }

        titleMatch || subtitleMatch || keywordMatch
    }
}
