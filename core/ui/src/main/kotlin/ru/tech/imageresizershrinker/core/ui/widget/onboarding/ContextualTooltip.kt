/*
 * ImageToolbox is an image editor for android
 * Copyright (c) 2024 T8RIN (Malik Mukhametzyanov)
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

package ru.tech.imageresizershrinker.core.ui.widget.onboarding

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

/**
 * Contextual tooltip that appears near UI elements
 * 
 * Smart tooltip that positions itself relative to an anchor element
 * and automatically dismisses after a delay.
 * 
 * @param show Whether to show the tooltip
 * @param config Tooltip configuration
 * @param anchorPosition Position of the anchor element
 * @param onDismiss Callback when tooltip is dismissed
 * @param modifier Modifier for the tooltip
 */
@Composable
fun ContextualTooltip(
    show: Boolean,
    config: TooltipConfig,
    anchorPosition: Offset,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    var visible by remember(show) { mutableStateOf(show) }
    val alpha = remember { Animatable(0f) }
    val scale = remember { Animatable(0.8f) }
    
    LaunchedEffect(show) {
        if (show) {
            visible = true
            // Fade in and scale up
            alpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 200)
            )
            scale.animateTo(
                targetValue = 1f,
                animationSpec = spring(
                    dampingRatio = 0.6f,
                    stiffness = 400f
                )
            )
            
            // Auto-dismiss if configured
            config.autoDismissDelay?.let { delayMs ->
                delay(delayMs)
                // Fade out
                alpha.animateTo(
                    targetValue = 0f,
                    animationSpec = tween(durationMillis = 200)
                )
                visible = false
                onDismiss()
            }
        } else {
            visible = false
        }
    }
    
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn() + scaleIn(),
        exit = fadeOut() + scaleOut()
    ) {
        TooltipBubble(
            message = config.message,
            showArrow = config.showArrow,
            position = config.position,
            alpha = alpha.value,
            scale = scale.value,
            modifier = modifier
        )
    }
}

/**
 * Tooltip bubble with optional arrow
 */
@Composable
private fun TooltipBubble(
    message: String,
    showArrow: Boolean,
    position: TooltipPosition,
    alpha: Float,
    scale: Float,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .graphicsLayer {
                this.alpha = alpha
                scaleX = scale
                scaleY = scale
            }
    ) {
        // Arrow pointing to anchor (if enabled)
        if (showArrow) {
            Canvas(
                modifier = Modifier
                    .padding(
                        bottom = when (position) {
                            TooltipPosition.Top, TooltipPosition.Auto -> 0.dp
                            else -> 8.dp
                        }
                    )
            ) {
                val arrowPath = Path().apply {
                    when (position) {
                        TooltipPosition.Top, TooltipPosition.Auto -> {
                            // Arrow pointing down
                            moveTo(size.width / 2 - 8.dp.toPx(), 0f)
                            lineTo(size.width / 2, 8.dp.toPx())
                            lineTo(size.width / 2 + 8.dp.toPx(), 0f)
                        }
                        TooltipPosition.Bottom -> {
                            // Arrow pointing up
                            moveTo(size.width / 2 - 8.dp.toPx(), size.height)
                            lineTo(size.width / 2, size.height - 8.dp.toPx())
                            lineTo(size.width / 2 + 8.dp.toPx(), size.height)
                        }
                        TooltipPosition.Start -> {
                            // Arrow pointing right
                            moveTo(size.width, size.height / 2 - 8.dp.toPx())
                            lineTo(size.width - 8.dp.toPx(), size.height / 2)
                            lineTo(size.width, size.height / 2 + 8.dp.toPx())
                        }
                        TooltipPosition.End -> {
                            // Arrow pointing left
                            moveTo(0f, size.height / 2 - 8.dp.toPx())
                            lineTo(8.dp.toPx(), size.height / 2)
                            lineTo(0f, size.height / 2 + 8.dp.toPx())
                        }
                    }
                    close()
                }
                
                drawPath(
                    path = arrowPath,
                    color = Color.Black.copy(alpha = 0.9f)
                )
            }
        }
        
        // Tooltip content
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(Color.Black.copy(alpha = 0.9f))
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            Text(
                text = message,
                style = MaterialTheme.typography.bodySmall,
                color = Color.White
            )
        }
    }
}

/**
 * Simple tooltip without animation
 * 
 * Use for static tooltips that don't need entrance/exit animations
 */
@Composable
fun SimpleTooltip(
    message: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color.Black.copy(alpha = 0.9f))
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodySmall,
            color = Color.White
        )
    }
}

/**
 * Tooltip with custom background color
 */
@Composable
fun ColoredTooltip(
    message: String,
    backgroundColor: Color,
    textColor: Color = Color.White,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodySmall,
            color = textColor
        )
    }
}
