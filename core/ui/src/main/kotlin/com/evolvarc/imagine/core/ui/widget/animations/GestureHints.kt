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

package com.evolvarc.imagine.core.ui.widget.animations

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.SwipeLeft
import androidx.compose.material.icons.rounded.SwipeRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Animated swipe hint indicator
 * Subtly shows users that an element is swipeable
 * 
 * Design: Inspired by iOS coaching overlays
 * - Gentle, non-intrusive animation
 * - Fades out after showing
 * - Only shows once per session (DataStore tracked elsewhere)
 */
@Composable
fun SwipeHintAnimation(
    isVisible: Boolean,
    direction: SwipeDirection,
    modifier: Modifier = Modifier,
    iconSize: Dp = 32.dp,
    color: Color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
) {
    val offsetX = remember { Animatable(0f) }
    val alpha = remember { Animatable(0f) }
    val scale = remember { Animatable(0.8f) }
    
    LaunchedEffect(isVisible) {
        if (isVisible) {
            // Fade in
            alpha.animateTo(1f, tween(300))
            
            // Animate swipe motion 3 times
            repeat(3) {
                when (direction) {
                    SwipeDirection.LEFT -> {
                        offsetX.animateTo(0f, tween(100))
                        offsetX.animateTo(-40f, tween(500))
                    }
                    SwipeDirection.RIGHT -> {
                        offsetX.animateTo(0f, tween(100))
                        offsetX.animateTo(40f, tween(500))
                    }
                    SwipeDirection.UP -> {
                        // For vertical swipes (future use)
                    }
                    SwipeDirection.DOWN -> {
                        // For vertical swipes (future use)
                    }
                }
                scale.animateTo(1f, tween(250))
                scale.animateTo(0.8f, tween(250))
            }
            
            // Fade out
            alpha.animateTo(0f, tween(300))
        } else {
            offsetX.snapTo(0f)
            alpha.snapTo(0f)
            scale.snapTo(0.8f)
        }
    }
    
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = when (direction) {
                SwipeDirection.LEFT -> Icons.Rounded.SwipeLeft
                SwipeDirection.RIGHT -> Icons.Rounded.SwipeRight
                else -> Icons.Rounded.SwipeRight
            },
            contentDescription = "Swipe ${direction.name.lowercase()} hint",
            tint = color,
            modifier = Modifier
                .size(iconSize)
                .offset(x = offsetX.value.dp)
                .scale(scale.value)
                .alpha(alpha.value)
        )
    }
}

/**
 * Pulsing tap hint indicator
 * Shows users where to tap for interaction
 */
@Composable
fun TapHintAnimation(
    isVisible: Boolean,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
) {
    val scale = remember { Animatable(0.5f) }
    val alpha = remember { Animatable(0f) }
    
    LaunchedEffect(isVisible) {
        if (isVisible) {
            alpha.animateTo(1f, tween(200))
            
            // Pulse animation
            scale.animateTo(
                targetValue = 1.2f,
                animationSpec = infiniteRepeatable(
                    animation = tween(800),
                    repeatMode = RepeatMode.Reverse
                )
            )
        } else {
            alpha.snapTo(0f)
            scale.snapTo(0.5f)
        }
    }
    
    Canvas(
        modifier = modifier
            .size(60.dp)
            .alpha(alpha.value)
    ) {
        drawCircle(
            color = color,
            radius = size.minDimension / 2 * scale.value,
            center = center
        )
    }
}

/**
 * Long press hint animation
 * Shows circular progress indicating hold duration
 */
@Composable
fun LongPressHint(
    isVisible: Boolean,
    modifier: Modifier = Modifier,
    size: Dp = 48.dp,
    color: Color = MaterialTheme.colorScheme.primary,
    duration: Int = 1000
) {
    val progress = remember { Animatable(0f) }
    val alpha = remember { Animatable(0f) }
    
    LaunchedEffect(isVisible) {
        if (isVisible) {
            alpha.animateTo(0.7f, tween(200))
            progress.animateTo(
                targetValue = 1f,
                animationSpec = tween(duration)
            )
            alpha.animateTo(0f, tween(200))
        } else {
            progress.snapTo(0f)
            alpha.snapTo(0f)
        }
    }
    
    Canvas(
        modifier = modifier
            .size(size)
            .alpha(alpha.value)
    ) {
        // Background circle
        drawCircle(
            color = color.copy(alpha = 0.2f),
            radius = this.size.minDimension / 2
        )
        
        // Progress arc
        if (progress.value > 0f) {
            drawArc(
                color = color,
                startAngle = -90f,
                sweepAngle = 360f * progress.value,
                useCenter = true
            )
        }
    }
}

/**
 * Drag hint animation
 * Shows draggable elements with subtle movement
 */
@Composable
fun DragHintAnimation(
    isVisible: Boolean,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    color: Color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
) {
    val offsetY = remember { Animatable(0f) }
    val alpha = remember { Animatable(0f) }
    
    LaunchedEffect(isVisible) {
        if (isVisible) {
            alpha.animateTo(1f, tween(300))
            
            // Gentle up-down motion
            repeat(2) {
                offsetY.animateTo(-8f, tween(400))
                offsetY.animateTo(8f, tween(400))
            }
            offsetY.animateTo(0f, tween(300))
            
            alpha.animateTo(0f, tween(300))
        } else {
            offsetY.snapTo(0f)
            alpha.snapTo(0f)
        }
    }
    
    Box(
        modifier = modifier
            .offset(y = offsetY.value.dp)
            .alpha(alpha.value),
        contentAlignment = Alignment.Center
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = "Drag hint",
                tint = color,
                modifier = Modifier.size(24.dp)
            )
        } else {
            // Default drag handle indicator
            Canvas(modifier = Modifier.size(width = 32.dp, height = 4.dp)) {
                drawRoundRect(
                    color = color,
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(
                        size.height / 2,
                        size.height / 2
                    )
                )
            }
        }
    }
}

/**
 * Pinch zoom hint animation
 * Shows users they can pinch to zoom
 */
@Composable
fun PinchZoomHint(
    isVisible: Boolean,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
) {
    val distance = remember { Animatable(20f) }
    val alpha = remember { Animatable(0f) }
    
    LaunchedEffect(isVisible) {
        if (isVisible) {
            alpha.animateTo(1f, tween(200))
            
            // Pinch in/out animation
            repeat(2) {
                distance.animateTo(60f, tween(600)) // Expand
                distance.animateTo(20f, tween(600)) // Contract
            }
            
            alpha.animateTo(0f, tween(300))
        } else {
            distance.snapTo(20f)
            alpha.snapTo(0f)
        }
    }
    
    Canvas(
        modifier = modifier
            .fillMaxSize()
            .alpha(alpha.value)
    ) {
        val centerX = size.width / 2
        val centerY = size.height / 2
        
        // Left finger
        drawCircle(
            color = color,
            radius = 20f,
            center = Offset(centerX - distance.value, centerY)
        )
        
        // Right finger
        drawCircle(
            color = color,
            radius = 20f,
            center = Offset(centerX + distance.value, centerY)
        )
    }
}

/**
 * Swipe direction enum for hint animations
 */
enum class SwipeDirection {
    LEFT,
    RIGHT,
    UP,
    DOWN
}
