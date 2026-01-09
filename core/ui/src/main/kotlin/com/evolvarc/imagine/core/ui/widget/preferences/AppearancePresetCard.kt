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

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.evolvarc.imagine.core.settings.presentation.model.AppearancePreset
import com.evolvarc.imagine.core.ui.widget.modifier.ShapeDefaults

/**
 * Visual preview card for appearance presets
 * Shows a mini UI preview with the preset's styling applied
 * 
 * Design: Apple-style preset cards with live visual previews
 * Inspired by iOS Settings > Display & Brightness preset selection
 */
@Composable
fun AppearancePresetCard(
    preset: AppearancePreset,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    
    // Animate selection state
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 0.95f else 1f,
        animationSpec = spring(
            dampingRatio = 0.6f,
            stiffness = 400f
        ),
        label = "PresetCardScale"
    )
    
    val borderColor by animateColorAsState(
        targetValue = if (isSelected) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
        },
        animationSpec = spring(
            dampingRatio = 0.85f,
            stiffness = 400f
        ),
        label = "PresetCardBorder"
    )
    
    Card(
        modifier = modifier
            .scale(scale)
            .clickable(onClick = onClick),
        shape = ShapeDefaults.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        ),
        border = BorderStroke(
            width = if (isSelected) 2.dp else 1.dp,
            color = borderColor
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header with name and selection indicator
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = context.getString(preset.nameResId),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = if (isSelected) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.onSurface
                        }
                    )
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Text(
                        text = context.getString(preset.descriptionResId),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                
                if (isSelected) {
                    Icon(
                        imageVector = Icons.Rounded.CheckCircle,
                        contentDescription = "Selected",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Visual preview of the preset styling
            PresetVisualPreview(preset = preset)
        }
    }
}

/**
 * Mini UI preview showing how the preset looks
 * Displays sample buttons, cards, and UI elements with the preset's styling
 */
@Composable
private fun PresetVisualPreview(preset: AppearancePreset) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color(0xFF1C1C1E), // Dark background for preview
                shape = ShapeDefaults.medium
            )
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Sample app bar preview
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .background(
                    color = preset.colorTuple.primary.copy(alpha = 0.9f),
                    shape = ShapeDefaults.small
                )
                .then(
                    if (preset.drawAppBarShadows) {
                        Modifier.border(
                            width = preset.borderWidth.dp,
                            color = Color.White.copy(alpha = 0.1f),
                            shape = ShapeDefaults.small
                        )
                    } else Modifier
                )
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Mini app bar icon
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .background(
                        color = Color.White.copy(alpha = 0.2f),
                        shape = CircleShape
                    )
            )
            
            Spacer(modifier = Modifier.width(8.dp))
            
            // Mini app bar title
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(16.dp)
                    .background(
                        color = Color.White.copy(alpha = 0.3f),
                        shape = ShapeDefaults.extraSmall
                    )
            )
        }
        
        // Sample card preview
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(
                    color = Color(0xFF2C2C2E),
                    shape = ShapeDefaults.medium
                )
                .then(
                    if (preset.drawContainerShadows) {
                        Modifier.border(
                            width = preset.borderWidth.dp,
                            color = Color.White.copy(alpha = 0.1f),
                            shape = ShapeDefaults.medium
                        )
                    } else Modifier
                )
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Mini content placeholder
                Column {
                    Box(
                        modifier = Modifier
                            .width(60.dp)
                            .height(8.dp)
                            .background(
                                color = Color.White.copy(alpha = 0.6f),
                                shape = ShapeDefaults.extraSmall
                            )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .width(40.dp)
                            .height(6.dp)
                            .background(
                                color = Color.White.copy(alpha = 0.3f),
                                shape = ShapeDefaults.extraSmall
                            )
                    )
                }
                
                // Mini button
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .background(
                            color = preset.colorTuple.primary.copy(alpha = 0.8f),
                            shape = CircleShape
                        )
                        .then(
                            if (preset.drawButtonShadows) {
                                Modifier.border(
                                    width = (preset.borderWidth / 2).dp,
                                    color = Color.White.copy(alpha = 0.2f),
                                    shape = CircleShape
                                )
                            } else Modifier
                        )
                )
            }
        }
        
        // Emoji count indicator
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            repeat(minOf(preset.emojisCount, 5)) {
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .background(
                            color = preset.colorTuple.primary.copy(alpha = 0.6f),
                            shape = CircleShape
                        )
                )
            }
        }
    }
}
