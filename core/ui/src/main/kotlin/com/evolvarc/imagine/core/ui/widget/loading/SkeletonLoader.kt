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

package com.evolvarc.imagine.core.ui.widget.loading

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.evolvarc.imagine.core.ui.widget.modifier.ShapeDefaults

/**
 * Skeleton loading shimmer effect modifier
 * Apple-style content placeholder with smooth animated gradient
 * 
 * Design Philosophy:
 * - Subtle, not distracting
 * - Shows structure while loading
 * - Smooth, continuous animation
 * - Matches theme colors
 */
@Composable
fun Modifier.shimmerEffect(
    shape: Shape = ShapeDefaults.medium,
    baseColor: Color = MaterialTheme.colorScheme.surfaceContainerHigh,
    highlightColor: Color = MaterialTheme.colorScheme.surfaceContainerHighest
): Modifier = composed {
    val transition = rememberInfiniteTransition(label = "shimmer")
    
    val translateAnimation by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmerTranslation"
    )

    val brush = Brush.linearGradient(
        colors = listOf(
            baseColor,
            highlightColor,
            baseColor
        ),
        start = Offset(translateAnimation - 1000f, translateAnimation - 1000f),
        end = Offset(translateAnimation, translateAnimation)
    )

    this
        .clip(shape)
        .background(brush)
}

/**
 * Skeleton loading placeholder for images
 * Shows aspect ratio and shimmer effect
 */
@Composable
fun SkeletonImage(
    modifier: Modifier = Modifier,
    aspectRatio: Float = 1f,
    shape: Shape = ShapeDefaults.large
) {
    Box(
        modifier = modifier
            .aspectRatio(aspectRatio)
            .shimmerEffect(shape = shape)
    )
}

/**
 * Skeleton loading placeholder for text lines
 * Variable width to simulate natural text layout
 */
@Composable
fun SkeletonText(
    modifier: Modifier = Modifier,
    height: Dp = 16.dp,
    widthFraction: Float = 1f,
    shape: Shape = ShapeDefaults.extraSmall
) {
    Box(
        modifier = modifier
            .fillMaxWidth(widthFraction)
            .height(height)
            .shimmerEffect(shape = shape)
    )
}

/**
 * Skeleton loading placeholder for circular elements (avatars, icons)
 */
@Composable
fun SkeletonCircle(
    size: Dp,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(size)
            .shimmerEffect(shape = CircleShape)
    )
}

/**
 * Skeleton loading placeholder for cards
 * Complete card structure with image + text
 */
@Composable
fun SkeletonCard(
    modifier: Modifier = Modifier,
    showImage: Boolean = true,
    imageAspectRatio: Float = 16f / 9f,
    textLines: Int = 2
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        if (showImage) {
            SkeletonImage(
                modifier = Modifier.fillMaxWidth(),
                aspectRatio = imageAspectRatio
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
        
        repeat(textLines) { index ->
            SkeletonText(
                height = if (index == 0) 20.dp else 16.dp,
                widthFraction = when (index) {
                    0 -> 0.7f // Title
                    textLines - 1 -> 0.5f // Last line shorter
                    else -> 1f // Full width
                }
            )
            
            if (index < textLines - 1) {
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

/**
 * Skeleton loading for list items
 * Row layout with avatar + text
 */
@Composable
fun SkeletonListItem(
    modifier: Modifier = Modifier,
    showAvatar: Boolean = true,
    textLines: Int = 2
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (showAvatar) {
            SkeletonCircle(size = 48.dp)
            Spacer(modifier = Modifier.width(16.dp))
        }
        
        Column(modifier = Modifier.weight(1f)) {
            repeat(textLines) { index ->
                SkeletonText(
                    height = if (index == 0) 18.dp else 14.dp,
                    widthFraction = if (index == textLines - 1) 0.6f else 1f
                )
                
                if (index < textLines - 1) {
                    Spacer(modifier = Modifier.height(6.dp))
                }
            }
        }
    }
}

/**
 * Skeleton loading for filter grid item
 * Square image with centered text below
 */
@Composable
fun SkeletonFilterItem(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SkeletonImage(
            modifier = Modifier.fillMaxWidth(),
            aspectRatio = 1f,
            shape = ShapeDefaults.large
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        SkeletonText(
            modifier = Modifier.fillMaxWidth(0.7f),
            height = 14.dp
        )
    }
}
