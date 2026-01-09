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
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Animated checkmark for success states
 * Apple-style smooth drawing animation
 * 
 * Design: Similar to iOS success animations
 * - Checkmark draws in smoothly
 * - Circle expands with spring physics
 * - Satisfying completion feel
 */
@Composable
fun AnimatedCheckmark(
    isVisible: Boolean,
    modifier: Modifier = Modifier,
    size: Dp = 48.dp,
    color: Color = MaterialTheme.colorScheme.primary,
    strokeWidth: Float = 4f
) {
    val circleProgress = remember { Animatable(0f) }
    val checkProgress = remember { Animatable(0f) }
    val scale = remember { Animatable(0.5f) }
    
    LaunchedEffect(isVisible) {
        if (isVisible) {
            launch {
                scale.animateTo(
                    targetValue = 1f,
                    animationSpec = spring(
                        dampingRatio = 0.6f,
                        stiffness = Spring.StiffnessLow
                    )
                )
            }
            
            launch {
                circleProgress.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(
                        durationMillis = 400,
                        easing = FastOutSlowInEasing
                    )
                )
            }
            
            delay(200)
            
            launch {
                checkProgress.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(
                        durationMillis = 300,
                        easing = FastOutSlowInEasing
                    )
                )
            }
        } else {
            circleProgress.snapTo(0f)
            checkProgress.snapTo(0f)
            scale.snapTo(0.5f)
        }
    }
    
    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size)) {
            val canvasSize = this.size.minDimension
            val center = this.center
            val radius = canvasSize / 2 - strokeWidth
            
            scale(scale.value, center) {
                // Draw circle
                if (circleProgress.value > 0f) {
                    drawArc(
                        color = color,
                        startAngle = -90f,
                        sweepAngle = 360f * circleProgress.value,
                        useCenter = false,
                        style = Stroke(
                            width = strokeWidth,
                            cap = StrokeCap.Round
                        ),
                        topLeft = androidx.compose.ui.geometry.Offset(
                            center.x - radius,
                            center.y - radius
                        ),
                        size = androidx.compose.ui.geometry.Size(radius * 2, radius * 2)
                    )
                }
                
                // Draw checkmark
                if (checkProgress.value > 0f) {
                    val checkPath = Path().apply {
                        val checkSize = canvasSize * 0.3f
                        val startX = center.x - checkSize * 0.3f
                        val startY = center.y
                        
                        moveTo(startX, startY)
                        
                        val midX = center.x - checkSize * 0.05f
                        val midY = center.y + checkSize * 0.25f
                        val endX = center.x + checkSize * 0.4f
                        val endY = center.y - checkSize * 0.3f
                        
                        val progress = checkProgress.value
                        
                        if (progress <= 0.5f) {
                            // First half: start to mid
                            val segmentProgress = progress * 2
                            lineTo(
                                startX + (midX - startX) * segmentProgress,
                                startY + (midY - startY) * segmentProgress
                            )
                        } else {
                            // Complete first half
                            lineTo(midX, midY)
                            // Second half: mid to end
                            val segmentProgress = (progress - 0.5f) * 2
                            lineTo(
                                midX + (endX - midX) * segmentProgress,
                                midY + (endY - midY) * segmentProgress
                            )
                        }
                    }
                    
                    drawPath(
                        path = checkPath,
                        color = color,
                        style = Stroke(
                            width = strokeWidth,
                            cap = StrokeCap.Round
                        )
                    )
                }
            }
        }
    }
}

/**
 * Success checkmark with icon
 * Simpler version using Material icon
 */
@Composable
fun SuccessCheckmark(
    isVisible: Boolean,
    modifier: Modifier = Modifier,
    size: Dp = 48.dp,
    color: Color = MaterialTheme.colorScheme.primary
) {
    val scale = remember { Animatable(0f) }
    val alpha = remember { Animatable(0f) }
    
    LaunchedEffect(isVisible) {
        if (isVisible) {
            launch {
                scale.animateTo(
                    targetValue = 1f,
                    animationSpec = spring(
                        dampingRatio = 0.5f,
                        stiffness = Spring.StiffnessMedium
                    )
                )
            }
            launch {
                alpha.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(200)
                )
            }
        } else {
            scale.snapTo(0f)
            alpha.snapTo(0f)
        }
    }
    
    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Rounded.CheckCircle,
            contentDescription = "Success",
            tint = color,
            modifier = Modifier
                .size(size)
                .scale(scale.value)
                .alpha(alpha.value)
        )
    }
}

/**
 * Pulsing success indicator
 * For long-running operations completion
 */
@Composable
fun PulsingSuccess(
    isVisible: Boolean,
    modifier: Modifier = Modifier,
    size: Dp = 64.dp,
    color: Color = MaterialTheme.colorScheme.primary
) {
    val scale = remember { Animatable(0.8f) }
    val alpha = remember { Animatable(0f) }
    
    LaunchedEffect(isVisible) {
        if (isVisible) {
            alpha.animateTo(1f, tween(200))
            
            // Pulse effect
            repeat(3) {
                scale.animateTo(
                    1.1f,
                    spring(dampingRatio = 0.6f, stiffness = Spring.StiffnessMedium)
                )
                scale.animateTo(
                    0.9f,
                    spring(dampingRatio = 0.6f, stiffness = Spring.StiffnessMedium)
                )
            }
            scale.animateTo(
                1f,
                spring(dampingRatio = 0.6f, stiffness = Spring.StiffnessMedium)
            )
        } else {
            scale.snapTo(0.8f)
            alpha.snapTo(0f)
        }
    }
    
    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Rounded.CheckCircle,
            contentDescription = "Completed",
            tint = color,
            modifier = Modifier
                .size(size)
                .scale(scale.value)
                .alpha(alpha.value)
        )
    }
}
