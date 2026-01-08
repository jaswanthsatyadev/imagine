/*
 * Imagine is an image editor for android
 * Copyright (c) 2024 Jaswanth Satya Dev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * You should have received a copy of the Apache License
 * along with this program.  If not, see <http://www.apache.org/licenses/LICENSE-2.0>.
 */

@file:Suppress("NOTHING_TO_INLINE")

package com.evolvarc.imagine.core.ui.theme

import androidx.annotation.FloatRange
import androidx.compose.animation.animateColorAsState
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.ColorUtils
import com.evolvarc.imagine.core.settings.presentation.provider.LocalSettingsState

fun ColorScheme.outlineVariant(
    luminance: Float = 0.3f,
    onTopOf: Color = surfaceContainer
) = onSecondaryContainer
    .copy(alpha = luminance)
    .compositeOver(onTopOf)

@Composable
inline fun takeColorFromScheme(
    action: @Composable ColorScheme.(isNightMode: Boolean) -> Color
) = animateColorAsState(
    MaterialTheme.colorScheme.run {
        action(LocalSettingsState.current.isNightMode)
    }
).value


fun ColorScheme.suggestContainerColorBy(color: Color) = when (color) {
    onPrimary -> primary
    onSecondary -> secondary
    onTertiary -> tertiary
    onPrimaryContainer -> primaryContainer
    onSecondaryContainer -> secondaryContainer
    onTertiaryContainer -> tertiaryContainer
    onError -> error
    onErrorContainer -> errorContainer
    onSurfaceVariant -> surfaceVariant
    inverseOnSurface -> inverseSurface
    else -> surface
}

inline val ColorScheme.mixedContainer: Color
    @Composable get() = run {
        tertiaryContainer.blend(
            primaryContainer,
            0.4f
        )
    }

inline val ColorScheme.primaryContainerFixed: Color
    @Composable get() = run {
        if (LocalSettingsState.current.isNightMode) primaryContainer.copy(alpha = 0.6f)
        else primary.copy(alpha = 0.6f)
    }

inline val ColorScheme.onPrimaryContainerFixed: Color
    @Composable get() = run {
        if (LocalSettingsState.current.isNightMode) onPrimaryContainer else onPrimary
    }.blend(Color.White)

inline val ColorScheme.secondaryContainerFixed: Color
    @Composable get() = run {
        if (LocalSettingsState.current.isNightMode) secondaryContainer.copy(alpha = 0.6f)
        else secondary.copy(alpha = 0.6f)
    }

inline val ColorScheme.onSecondaryContainerFixed: Color
    @Composable get() = run {
        if (LocalSettingsState.current.isNightMode) onSecondaryContainer else onSecondary
    }.blend(Color.White)

inline val ColorScheme.onMixedContainer: Color
    @Composable get() = run {
        onTertiaryContainer.blend(
            onPrimaryContainer,
            0.4f
        )
    }

inline fun Color.takeIf(predicate: Boolean) = if (predicate) this else Color.Unspecified

inline fun Color.takeUnless(predicate: Boolean) = takeIf(!predicate)

fun Color.blend(
    color: Color,
    @FloatRange(from = 0.0, to = 1.0) fraction: Float = 0.2f
): Color = Color(ColorUtils.blendARGB(this.toArgb(), color.toArgb(), fraction))

@Composable
fun Color.inverse(
    fraction: (Boolean) -> Float = { 0.5f },
    darkMode: Boolean = LocalSettingsState.current.isNightMode,
): Color = if (darkMode) blend(Color.White, fraction(true))
else blend(Color.Black, fraction(false))

fun Color.inverseByLuma(
    fraction: (Boolean) -> Float = { 0.5f },
): Color = if (luminance() < 0.3f) blend(Color.White, fraction(true))
else blend(Color.Black, fraction(false))

@Composable
fun Color.inverse(
    fraction: (Boolean) -> Float = { 0.5f },
    color: (Boolean) -> Color,
    darkMode: Boolean = LocalSettingsState.current.isNightMode,
): Color = if (darkMode) blend(color(true), fraction(true))
else blend(color(true), fraction(false))


fun Int.blend(
    color: Color,
    @FloatRange(from = 0.0, to = 1.0) fraction: Float = 0.2f
): Int = ColorUtils.blendARGB(this, color.toArgb(), fraction)

@Composable
fun Color.harmonizeWithPrimary(
    @FloatRange(
        from = 0.0,
        to = 1.0
    ) fraction: Float = 0.2f
): Color = blend(MaterialTheme.colorScheme.primary, fraction)


fun Int.toColor() = Color(this)

inline val Green: Color
    @Composable get() = Color(0xFF34C759).harmonizeWithPrimary(0.15f)

inline val Red: Color
    @Composable get() = Color(0xFFFF3B30).harmonizeWithPrimary(0.15f)

inline val Blue: Color
    @Composable get() = Color(0xFF007AFF).harmonizeWithPrimary(0.15f)

inline val Black: Color
    @Composable get() = Color(0xFF1C1C1E).harmonizeWithPrimary(0.12f)

inline val StrongBlack: Color
    @Composable get() = Color(0xFF000000).harmonizeWithPrimary(0.05f)

inline val White: Color
    @Composable get() = Color(0xFFFFFFFF).harmonizeWithPrimary(0.03f)

inline val BitcoinColor: Color
    @Composable get() = Color(0xFFFF9500).harmonizeWithPrimary(0.15f)

inline val USDTColor: Color
    @Composable get() = Color(0xFF30D158).harmonizeWithPrimary(0.15f)

inline val TONSpaceColor: Color
    @Composable get() = Color(0xFF2C2C2E).harmonizeWithPrimary(0.12f)

inline val TONColor: Color
    @Composable get() = Color(0xFF0A84FF).harmonizeWithPrimary(0.15f)
