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

package com.evolvarc.imagine.core.settings.presentation.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.t8rin.dynamic.theme.ColorTuple

/**
 * Predefined appearance configurations for quick styling
 * Provides Apple-level design presets that users can apply with one tap
 */
sealed class AppearancePreset(
    val id: String,
    val nameResId: Int,
    val descriptionResId: Int,
    val colorTuple: ColorTuple,
    val borderWidth: Float,
    val isDynamicColors: Boolean,
    val isAmoledMode: Boolean,
    val drawContainerShadows: Boolean,
    val drawAppBarShadows: Boolean,
    val drawButtonShadows: Boolean,
    val drawFABShadows: Boolean,
    val drawSwitchShadows: Boolean,
    val drawSliderShadows: Boolean,
    val emojisCount: Int,
    val confettiEnabled: Boolean
) {
    /**
     * MINIMAL - Clean, distraction-free design
     * - No borders
     * - Minimal shadows
     * - Single emoji
     * - No confetti
     * - Perfect for productivity-focused users
     */
    data object Minimal : AppearancePreset(
        id = "minimal",
        nameResId = com.evolvarc.imagine.core.resources.R.string.preset_minimal,
        descriptionResId = com.evolvarc.imagine.core.resources.R.string.preset_minimal_desc,
        colorTuple = ColorTuple(Color(0xFF007AFF)), // Apple Blue
        borderWidth = 0f,
        isDynamicColors = false,
        isAmoledMode = false,
        drawContainerShadows = false,
        drawAppBarShadows = false,
        drawButtonShadows = false,
        drawFABShadows = true, // Keep FAB shadow for depth
        drawSwitchShadows = false,
        drawSliderShadows = false,
        emojisCount = 1,
        confettiEnabled = false
    )

    /**
     * VIBRANT - Colorful, playful design  
     * - Medium borders
     * - All shadows enabled
     * - Multiple emojis
     * - Confetti enabled
     * - Perfect for creative users
     */
    data object Vibrant : AppearancePreset(
        id = "vibrant",
        nameResId = com.evolvarc.imagine.core.resources.R.string.preset_vibrant,
        descriptionResId = com.evolvarc.imagine.core.resources.R.string.preset_vibrant_desc,
        colorTuple = ColorTuple(Color(0xFFBF5AF2)), // Apple Purple
        borderWidth = 1f,
        isDynamicColors = true, // Use dynamic colors for variety
        isAmoledMode = false,
        drawContainerShadows = true,
        drawAppBarShadows = true,
        drawButtonShadows = true,
        drawFABShadows = true,
        drawSwitchShadows = true,
        drawSliderShadows = true,
        emojisCount = 5,
        confettiEnabled = true
    )

    /**
     * PROFESSIONAL - Refined, business-appropriate design
     * - Subtle borders
     * - Conservative shadows
     * - Medium emoji count
     * - No confetti
     * - Perfect for professional work
     */
    data object Professional : AppearancePreset(
        id = "professional",
        nameResId = com.evolvarc.imagine.core.resources.R.string.preset_professional,
        descriptionResId = com.evolvarc.imagine.core.resources.R.string.preset_professional_desc,
        colorTuple = ColorTuple(Color(0xFF5856D6)), // Apple Indigo
        borderWidth = 0.5f,
        isDynamicColors = false,
        isAmoledMode = false,
        drawContainerShadows = true,
        drawAppBarShadows = true,
        drawButtonShadows = true,
        drawFABShadows = true,
        drawSwitchShadows = false,
        drawSliderShadows = false,
        emojisCount = 2,
        confettiEnabled = false
    )

    companion object {
        val entries = listOf(Minimal, Vibrant, Professional)
        
        fun fromId(id: String): AppearancePreset? {
            return entries.firstOrNull { it.id == id }
        }
    }
}
