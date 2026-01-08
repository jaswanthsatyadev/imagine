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

package com.evolvarc.imagine.core.ui.widget.batch

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.HighQuality
import androidx.compose.material.icons.outlined.Speed
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.evolvarc.imagine.core.resources.R
import com.evolvarc.imagine.core.ui.widget.enhanced.EnhancedButton
import com.evolvarc.imagine.core.ui.widget.modifier.ShapeDefaults
import com.evolvarc.imagine.core.ui.widget.modifier.container

/**
 * Export preset for batch operations
 */
data class ExportPreset(
    val name: String,
    val description: String,
    val icon: ImageVector,
    val quality: Int, // 0-100
    val format: String,
    val optimizeForSpeed: Boolean = false
)

/**
 * Batch export options with presets
 * 
 * Provides quick export presets for batch operations:
 * - High Quality: Maximum quality, larger file size
 * - Balanced: Good quality, reasonable file size
 * - Fast: Lower quality, optimized for speed
 * - Custom: User-defined settings
 * 
 * Helps users quickly configure export settings for
 * batch processing without dealing with individual parameters.
 */
@Composable
fun BatchExportOptions(
    selectedPreset: ExportPreset,
    onPresetSelect: (ExportPreset) -> Unit,
    modifier: Modifier = Modifier,
    customPreset: ExportPreset? = null
) {
    val defaultPresets = listOf(
        ExportPreset(
            name = stringResource(R.string.high_quality),
            description = stringResource(R.string.high_quality_sub),
            icon = Icons.Outlined.HighQuality,
            quality = 100,
            format = "PNG",
            optimizeForSpeed = false
        ),
        ExportPreset(
            name = stringResource(R.string.balanced),
            description = stringResource(R.string.balanced_sub),
            icon = Icons.Rounded.CheckCircle,
            quality = 85,
            format = "JPEG",
            optimizeForSpeed = false
        ),
        ExportPreset(
            name = stringResource(R.string.fast_export),
            description = stringResource(R.string.fast_sub),
            icon = Icons.Outlined.Speed,
            quality = 70,
            format = "JPEG",
            optimizeForSpeed = true
        )
    )
    
    val presets = if (customPreset != null) {
        defaultPresets + customPreset
    } else {
        defaultPresets
    }
    
    Column(
        modifier = modifier
            .fillMaxWidth()
            .container(
                shape = ShapeDefaults.extraLarge,
                color = MaterialTheme.colorScheme.surfaceContainerLow
            )
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = stringResource(R.string.export_presets),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
        
        Text(
            text = stringResource(R.string.export_presets_sub),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(Modifier.height(8.dp))
        
        presets.forEach { preset ->
            ExportPresetItem(
                preset = preset,
                isSelected = preset == selectedPreset,
                onClick = { onPresetSelect(preset) }
            )
        }
    }
}

@Composable
private fun ExportPresetItem(
    preset: ExportPreset,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    EnhancedButton(
        onClick = onClick,
        containerColor = if (isSelected) {
            MaterialTheme.colorScheme.primaryContainer
        } else {
            MaterialTheme.colorScheme.surfaceContainerHighest
        },
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = preset.icon,
                contentDescription = null,
                tint = if (isSelected) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                },
                modifier = Modifier.size(24.dp)
            )
            
            Spacer(Modifier.width(12.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = preset.name,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                    color = if (isSelected) {
                        MaterialTheme.colorScheme.onPrimaryContainer
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    }
                )
                
                Text(
                    text = preset.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isSelected) {
                        MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    }
                )
                
                Text(
                    text = "${preset.format} • ${stringResource(R.string.quality)}: ${preset.quality}%",
                    style = MaterialTheme.typography.labelSmall,
                    color = if (isSelected) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                    }
                )
            }
            
            if (isSelected) {
                Icon(
                    imageVector = Icons.Rounded.CheckCircle,
                    contentDescription = stringResource(R.string.selected),
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
