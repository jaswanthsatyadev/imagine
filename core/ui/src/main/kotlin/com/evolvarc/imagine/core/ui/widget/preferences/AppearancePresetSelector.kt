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

package com.evolvarc.imagine.core.ui.widget.preferences

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.evolvarc.imagine.core.resources.R
import com.evolvarc.imagine.core.settings.presentation.model.AppearancePreset

/**
 * Preset selector that displays all available appearance presets
 * with visual preview cards
 * 
 * Design: Similar to iOS Settings > Display & Brightness
 * Shows all presets in a vertical list with live previews
 */
@Composable
fun AppearancePresetSelector(
    selectedPresetId: String?,
    onPresetSelected: (AppearancePreset) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(16.dp)
) {
    val context = LocalContext.current
    
    // All available presets
    val presets = listOf(
        AppearancePreset.Minimal,
        AppearancePreset.Vibrant,
        AppearancePreset.Professional
    )
    
    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Column {
                Text(
                    text = context.getString(R.string.appearance_presets),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = context.getString(R.string.appearance_presets_subtitle),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
        
        items(presets) { preset ->
            AppearancePresetCard(
                preset = preset,
                isSelected = preset.id == selectedPresetId,
                onClick = { onPresetSelected(preset) },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

/**
 * Helper function to apply a preset to settings
 * Returns a map of setting changes to apply
 */
fun AppearancePreset.toSettingsMap(): Map<String, Any> {
    return mapOf(
        "appColorTuple" to colorTuple,
        "borderWidth" to borderWidth,
        "isDynamicColors" to isDynamicColors,
        "isAmoledMode" to isAmoledMode,
        "drawContainerShadows" to drawContainerShadows,
        "drawAppBarShadows" to drawAppBarShadows,
        "drawButtonShadows" to drawButtonShadows,
        "drawFabShadows" to drawFABShadows,
        "drawSwitchShadows" to drawSwitchShadows,
        "drawSliderShadows" to drawSliderShadows,
        "emojisCount" to emojisCount,
        "isConfettiEnabled" to confettiEnabled
    )
}
