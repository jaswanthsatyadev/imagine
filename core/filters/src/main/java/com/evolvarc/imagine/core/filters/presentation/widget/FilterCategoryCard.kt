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
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowRight
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.evolvarc.imagine.core.filters.domain.FilterProvider
import com.evolvarc.imagine.core.filters.presentation.model.UiFilter
import com.evolvarc.imagine.core.domain.model.IntegerSize
import com.evolvarc.imagine.core.ui.widget.modifier.ShapeDefaults
import com.evolvarc.imagine.core.ui.widget.modifier.container
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Filter category card with cover image
 * 
 * Displays a category of filters with a representative cover image
 * showing a preview of the first filter in that category.
 * Designed to enable quick navigation through filter categories.
 */
@Composable
fun FilterCategoryCard(
    title: String,
    icon: ImageVector,
    filters: List<UiFilter<*>>,
    previewBitmap: Bitmap?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    filterProvider: FilterProvider<Bitmap>
) {
    var coverBitmap by remember(filters, previewBitmap) { mutableStateOf<Bitmap?>(null) }
    
    // Generate cover image with first filter
    LaunchedEffect(filters, previewBitmap) {
        if (filters.isNotEmpty() && previewBitmap != null) {
            withContext(Dispatchers.Default) {
                runCatching {
                    val coverSize = 300
                    val scaledBitmap = Bitmap.createScaledBitmap(
                        previewBitmap,
                        coverSize,
                        coverSize,
                        true
                    )
                    
                    val transformation = filterProvider.filterToTransformation(filters.first())
                    coverBitmap = transformation.transform(scaledBitmap, IntegerSize(coverSize, coverSize))
                }.getOrNull()
            }
        }
    }
    
    val scale by animateFloatAsState(
        targetValue = 1f,
        animationSpec = spring(
            dampingRatio = 0.7f,
            stiffness = 300f
        ),
        label = "CategoryCardScale"
    )
    
    Box(
        modifier = modifier
            .scale(scale)
            .clip(ShapeDefaults.superLarge)
            .clickable(onClick = onClick)
    ) {
        // Background cover image
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
                .container(
                    shape = ShapeDefaults.superLarge,
                    color = MaterialTheme.colorScheme.surfaceContainerHigh
                )
        ) {
            coverBitmap?.let { bitmap ->
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = title,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(ShapeDefaults.superLarge),
                    contentScale = ContentScale.Crop
                )
                
                // Gradient overlay for text readability
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.7f)
                                ),
                                startY = 100f
                            )
                        )
                )
            }
            
            // Category info overlay
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(20.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                Spacer(Modifier.height(4.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${filters.size} filters",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                    Spacer(Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.Rounded.KeyboardArrowRight,
                        contentDescription = "Navigate",
                        tint = Color.White.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
}
