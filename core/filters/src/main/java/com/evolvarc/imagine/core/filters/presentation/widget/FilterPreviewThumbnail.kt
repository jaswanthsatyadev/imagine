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
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.evolvarc.imagine.core.filters.domain.FilterProvider
import com.evolvarc.imagine.core.filters.presentation.model.UiFilter
import com.evolvarc.imagine.core.domain.model.IntegerSize
import com.evolvarc.imagine.core.ui.theme.takeColorFromScheme
import com.evolvarc.imagine.core.ui.widget.modifier.ShapeDefaults
import com.evolvarc.imagine.core.ui.widget.modifier.container
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Instagram-style filter thumbnail with live preview
 * 
 * Displays a thumbnail preview of the filter applied to a sample image,
 * with the filter name below. Shows a checkmark when selected.
 * Designed for grid layouts to enable visual browsing of filters.
 */
@Composable
fun FilterPreviewThumbnail(
    filter: UiFilter<*>,
    previewBitmap: Bitmap?,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    filterProvider: FilterProvider<Bitmap>
) {
    val context = LocalContext.current
    var processedBitmap by remember(filter, previewBitmap) { mutableStateOf<Bitmap?>(null) }
    
    // Generate thumbnail preview asynchronously
    LaunchedEffect(filter, previewBitmap) {
        previewBitmap?.let { bitmap ->
            withContext(Dispatchers.Default) {
                runCatching {
                    // Scale down for performance (thumbnail size)
                    val thumbSize = 150
                    val scaledBitmap = Bitmap.createScaledBitmap(
                        bitmap,
                        thumbSize,
                        thumbSize,
                        true
                    )
                    
                    // Apply filter to thumbnail
                    val transformation = filterProvider.filterToTransformation(filter)
                    processedBitmap = transformation.transform(scaledBitmap, IntegerSize(thumbSize, thumbSize))
                }.getOrNull()
            }
        }
    }
    
    // Animate scale on selection
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 0.92f else 1f,
        animationSpec = spring(
            dampingRatio = 0.6f,
            stiffness = 400f
        ),
        label = "FilterThumbnailScale"
    )
    
    Column(
        modifier = modifier
            .scale(scale)
            .clip(ShapeDefaults.large)
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Thumbnail preview
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .container(
                    shape = ShapeDefaults.large,
                    color = MaterialTheme.colorScheme.surfaceContainerHigh,
                    borderColor = if (isSelected) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                    }
                )
        ) {
            // Show preview if available
            processedBitmap?.let { bitmap ->
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = context.getString(filter.title),
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(ShapeDefaults.large),
                    contentScale = ContentScale.Crop
                )
            } ?: run {
                // Loading placeholder
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            MaterialTheme.colorScheme.surfaceContainerHigh
                        )
                )
            }
            
            // Selection indicator
            androidx.compose.animation.AnimatedVisibility(
                visible = isSelected,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.CheckCircle,
                    contentDescription = "Selected",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(24.dp)
                        .background(
                            MaterialTheme.colorScheme.surface,
                            CircleShape
                        )
                        .border(
                            2.dp,
                            MaterialTheme.colorScheme.primary,
                            CircleShape
                        )
                )
            }
        }
        
        // Filter name
        Text(
            text = context.getString(filter.title),
            style = MaterialTheme.typography.labelSmall,
            color = if (isSelected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            },
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp, start = 4.dp, end = 4.dp)
        )
    }
}
