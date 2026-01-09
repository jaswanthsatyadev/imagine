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

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

/**
 * Spotlight overlay that highlights a specific UI element
 * 
 * Creates a dimmed overlay with a transparent "spotlight" shape that draws
 * attention to a specific element. Shows a floating card with explanation.
 * 
 * @param config Spotlight configuration
 * @param onNext Callback when user taps "Next" button
 * @param onSkip Callback when user taps "Skip" button
 * @param onDismiss Callback when spotlight is dismissed
 * @param modifier Modifier for the spotlight container
 */
@Composable
fun OnboardingSpotlight(
    config: SpotlightConfig,
    onNext: () -> Unit,
    onSkip: (() -> Unit)? = null,
    onDismiss: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var targetBounds by remember { mutableStateOf<Rect?>(null) }
    val alpha = remember { Animatable(0f) }
    val scale = remember { Animatable(0.8f) }
    
    LaunchedEffect(Unit) {
        // Fade in and scale up animation
        alpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 300)
        )
        scale.animateTo(
            targetValue = 1f,
            animationSpec = spring(
                dampingRatio = 0.6f,
                stiffness = Spring.StiffnessMedium.toFloat()
            )
        )
    }
    
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.7f * alpha.value))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = { /* Prevent clicks from passing through */ }
            )
    ) {
        // Draw spotlight effect
        Canvas(modifier = Modifier.fillMaxSize()) {
            targetBounds?.let { bounds ->
                drawSpotlight(bounds, config.shape, config.padding)
            }
        }
        
        // Explanation card
        SpotlightCard(
            config = config,
            alpha = alpha.value,
            scale = scale.value,
            onNext = onNext,
            onSkip = onSkip
        )
    }
}

/**
 * Draw the spotlight effect with cutout
 */
private fun DrawScope.drawSpotlight(
    targetBounds: Rect,
    shape: SpotlightShape,
    padding: Float
) {
    val path = Path().apply {
        // Create full screen rectangle
        addRect(
            Rect(
                offset = Offset.Zero,
                size = size
            )
        )
        
        // Create cutout based on shape
        val cutoutPath = when (shape) {
            SpotlightShape.Circle -> createCirclePath(targetBounds, padding)
            SpotlightShape.Rectangle -> createRectanglePath(targetBounds, padding)
            SpotlightShape.Oval -> createOvalPath(targetBounds, padding)
        }
        
        // Subtract cutout from full rectangle using even-odd fill
        addPath(cutoutPath)
    }
    
    // Draw with blend mode to create transparency
    drawPath(
        path = path,
        color = Color.Black.copy(alpha = 0.7f),
        blendMode = BlendMode.SrcOver
    )
}

/**
 * Create circular spotlight path
 */
private fun createCirclePath(bounds: Rect, padding: Float): Path {
    val center = bounds.center
    val radius = maxOf(bounds.width, bounds.height) / 2 + padding
    
    return Path().apply {
        addOval(
            Rect(
                center = center,
                radius = radius
            )
        )
    }
}

/**
 * Create rectangular spotlight path
 */
private fun createRectanglePath(bounds: Rect, padding: Float): Path {
    return Path().apply {
        addRoundRect(
            RoundRect(
                rect = Rect(
                    left = bounds.left - padding,
                    top = bounds.top - padding,
                    right = bounds.right + padding,
                    bottom = bounds.bottom + padding
                ),
                cornerRadius = CornerRadius(16f, 16f)
            )
        )
    }
}

/**
 * Create oval spotlight path
 */
private fun createOvalPath(bounds: Rect, padding: Float): Path {
    return Path().apply {
        addOval(
            Rect(
                left = bounds.left - padding,
                top = bounds.top - padding,
                right = bounds.right + padding,
                bottom = bounds.bottom + padding
            )
        )
    }
}

/**
 * Floating card with spotlight explanation
 */
@Composable
private fun SpotlightCard(
    config: SpotlightConfig,
    alpha: Float,
    scale: Float,
    onNext: () -> Unit,
    onSkip: (() -> Unit)?
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Surface(
            modifier = Modifier
                .padding(bottom = 48.dp),
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface.copy(alpha = alpha),
            tonalElevation = 8.dp,
            shadowElevation = 8.dp
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = config.title,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Text(
                    text = config.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Row {
                    if (onSkip != null) {
                        TextButton(onClick = onSkip) {
                            Text("Skip")
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                    }
                    
                    Button(onClick = onNext) {
                        Text("Got it")
                    }
                }
            }
        }
    }
}

/**
 * Full-screen spotlight dialog
 * 
 * Use this variant when you want the spotlight to be modal and overlay everything
 */
@Composable
fun OnboardingSpotlightDialog(
    show: Boolean,
    config: SpotlightConfig,
    onNext: () -> Unit,
    onSkip: (() -> Unit)? = null,
    onDismiss: () -> Unit = {}
) {
    if (show) {
        Dialog(
            onDismissRequest = onDismiss,
            properties = DialogProperties(
                usePlatformDefaultWidth = false,
                dismissOnBackPress = true,
                dismissOnClickOutside = false
            )
        ) {
            OnboardingSpotlight(
                config = config,
                onNext = onNext,
                onSkip = onSkip,
                onDismiss = onDismiss
            )
        }
    }
}

/**
 * Simple spotlight without card (just highlights element)
 * 
 * Useful for subtle highlighting without explanatory text
 */
@Composable
fun SimpleSpotlight(
    targetBounds: Rect?,
    shape: SpotlightShape = SpotlightShape.Rectangle,
    padding: Float = 16f,
    overlayAlpha: Float = 0.7f,
    modifier: Modifier = Modifier
) {
    val alpha = remember { Animatable(0f) }
    
    LaunchedEffect(Unit) {
        alpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 300)
        )
    }
    
    Canvas(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = overlayAlpha * alpha.value))
    ) {
        targetBounds?.let { bounds ->
            drawSpotlight(bounds, shape, padding)
        }
    }
}
