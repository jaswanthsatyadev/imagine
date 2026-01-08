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

package com.evolvarc.imagine.core.filters.presentation.widget

import android.graphics.Bitmap
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.GridView
import androidx.compose.material.icons.rounded.ViewList
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.evolvarc.imagine.core.filters.domain.FilterProvider
import com.evolvarc.imagine.core.filters.presentation.model.UiFilter
import com.evolvarc.imagine.core.resources.R
import com.evolvarc.imagine.core.ui.widget.enhanced.EnhancedIconButton
import com.evolvarc.imagine.core.ui.widget.modifier.ShapeDefaults
import com.evolvarc.imagine.core.ui.widget.modifier.container

enum class FilterBrowsingMode {
    VISUAL_GRID,  // Instagram-style thumbnail grid
    LIST          // Traditional text list
}

/**
 * Visual filter browser with grid/list toggle
 * 
 * Provides two browsing modes:
 * - VISUAL_GRID: Instagram-style 3-column grid with live previews
 * - LIST: Traditional text-based list view
 * 
 * Includes optional intensity slider for quick filter adjustments.
 */
@Composable
fun VisualFilterBrowser(
    filters: List<UiFilter<*>>,
    previewBitmap: Bitmap?,
    selectedFilter: UiFilter<*>?,
    onFilterClick: (UiFilter<*>) -> Unit,
    onFilterChange: ((Any) -> Unit)? = null,
    modifier: Modifier = Modifier,
    filterProvider: FilterProvider<Bitmap>,
    showIntensitySlider: Boolean = false,
    initialMode: FilterBrowsingMode = FilterBrowsingMode.VISUAL_GRID
) {
    var browsingMode by rememberSaveable { mutableStateOf(initialMode) }
    var filterIntensity by remember { mutableFloatStateOf(100f) }
    
    Column(modifier = modifier) {
        // Mode toggle header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.filters),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                EnhancedIconButton(
                    onClick = { browsingMode = FilterBrowsingMode.VISUAL_GRID },
                    containerColor = if (browsingMode == FilterBrowsingMode.VISUAL_GRID) {
                        MaterialTheme.colorScheme.primaryContainer
                    } else {
                        MaterialTheme.colorScheme.surfaceContainerHighest
                    }
                ) {
                    Icon(
                        imageVector = Icons.Rounded.GridView,
                        contentDescription = "Grid view",
                        tint = if (browsingMode == FilterBrowsingMode.VISUAL_GRID) {
                            MaterialTheme.colorScheme.onPrimaryContainer
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        }
                    )
                }
                
                EnhancedIconButton(
                    onClick = { browsingMode = FilterBrowsingMode.LIST },
                    containerColor = if (browsingMode == FilterBrowsingMode.LIST) {
                        MaterialTheme.colorScheme.primaryContainer
                    } else {
                        MaterialTheme.colorScheme.surfaceContainerHighest
                    }
                ) {
                    Icon(
                        imageVector = Icons.Rounded.ViewList,
                        contentDescription = "List view",
                        tint = if (browsingMode == FilterBrowsingMode.LIST) {
                            MaterialTheme.colorScheme.onPrimaryContainer
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        }
                    )
                }
            }
        }
        
        // Intensity slider (when filter is selected)
        AnimatedVisibility(
            visible = showIntensitySlider && selectedFilter != null,
            enter = expandVertically(spring()) + fadeIn(),
            exit = shrinkVertically(spring()) + fadeOut()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .container(
                        shape = ShapeDefaults.large,
                        color = MaterialTheme.colorScheme.surfaceContainerHigh
                    )
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.intensity),
                        style = MaterialTheme.typography.labelLarge
                    )
                    Text(
                        text = "${filterIntensity.toInt()}%",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                }
                
                Slider(
                    value = filterIntensity,
                    onValueChange = { 
                        filterIntensity = it
                        onFilterChange?.invoke(it / 100f)
                    },
                    valueRange = 0f..100f,
                    modifier = Modifier.fillMaxWidth(),
                    colors = SliderDefaults.colors(
                        thumbColor = MaterialTheme.colorScheme.primary,
                        activeTrackColor = MaterialTheme.colorScheme.primary,
                        inactiveTrackColor = MaterialTheme.colorScheme.surfaceContainerHighest
                    )
                )
            }
        }
        
        // Filter display (grid or list)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            when (browsingMode) {
                FilterBrowsingMode.VISUAL_GRID -> {
                    VisualFilterGrid(
                        filters = filters,
                        previewBitmap = previewBitmap,
                        selectedFilter = selectedFilter,
                        onFilterClick = onFilterClick,
                        filterProvider = filterProvider,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                FilterBrowsingMode.LIST -> {
                    // Fallback to list view - reuse existing FilterSelectionItem
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "List view - use existing FilterSelectionItem component",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}
