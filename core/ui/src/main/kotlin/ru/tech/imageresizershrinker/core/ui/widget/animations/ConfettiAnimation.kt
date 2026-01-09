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

package ru.tech.imageresizershrinker.core.ui.widget.animations

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

/**
 * Data class representing a single confetti particle
 */
private data class ConfettiParticle(
    val initialX: Float,
    val initialY: Float,
    val velocityX: Float,
    val velocityY: Float,
    val color: Color,
    val size: Float,
    val rotationSpeed: Float,
    val seed: Int
)

/**
 * Confetti explosion animation
 * 
 * Creates an explosion of colorful particles from a specified origin point.
 * Perfect for celebrating successful operations or achievements.
 * 
 * @param trigger Change this value to trigger a new confetti explosion
 * @param originX X position of the explosion origin (0-1, where 0.5 is center)
 * @param originY Y position of the explosion origin (0-1, where 0.5 is center)
 * @param particleCount Number of confetti particles to generate
 * @param colors List of colors for confetti particles
 * @param durationMillis Duration of the animation in milliseconds
 * @param modifier Modifier for the confetti canvas
 */
@Composable
fun ConfettiExplosion(
    trigger: Int,
    originX: Float = 0.5f,
    originY: Float = 0.5f,
    particleCount: Int = 50,
    colors: List<Color> = listOf(
        Color(0xFFFF6B6B),
        Color(0xFFFFD93D),
        Color(0xFF6BCF7F),
        Color(0xFF4ECDC4),
        Color(0xFF95E1D3),
        Color(0xFFE74C3C),
        Color(0xFF9B59B6),
        Color(0xFF3498DB)
    ),
    durationMillis: Int = 3000,
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current
    val progress = remember { Animatable(0f) }
    
    val particles = remember(trigger) {
        List(particleCount) { index ->
            val angle = (index.toFloat() / particleCount) * 2 * Math.PI.toFloat() + 
                       Random.nextFloat() * 0.5f - 0.25f
            val speed = Random.nextFloat() * 300f + 200f
            
            ConfettiParticle(
                initialX = originX,
                initialY = originY,
                velocityX = cos(angle) * speed,
                velocityY = sin(angle) * speed - 100f, // Initial upward boost
                color = colors.random(),
                size = Random.nextFloat() * 8f + 4f,
                rotationSpeed = Random.nextFloat() * 720f - 360f,
                seed = Random.nextInt()
            )
        }
    }

    LaunchedEffect(trigger) {
        progress.snapTo(0f)
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = durationMillis,
                easing = LinearEasing
            )
        )
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        val canvasWidth = size.width
        val canvasHeight = size.height
        val currentProgress = progress.value
        
        if (currentProgress > 0f && currentProgress < 1f) {
            particles.forEach { particle ->
                // Calculate position with gravity
                val time = currentProgress * (durationMillis / 1000f)
                val gravity = 500f // Pixels per second squared
                
                val x = canvasWidth * particle.initialX + particle.velocityX * time
                val y = canvasHeight * particle.initialY + 
                       particle.velocityY * time + 
                       0.5f * gravity * time * time
                
                // Calculate opacity (fade out in last 30% of animation)
                val opacity = if (currentProgress < 0.7f) {
                    1f
                } else {
                    1f - ((currentProgress - 0.7f) / 0.3f)
                }
                
                // Calculate rotation
                val rotation = particle.rotationSpeed * time
                
                // Only draw if particle is still on screen
                if (x >= -50 && x <= canvasWidth + 50 && 
                    y >= -50 && y <= canvasHeight + 50) {
                    rotate(
                        degrees = rotation,
                        pivot = Offset(x, y)
                    ) {
                        drawRect(
                            color = particle.color.copy(alpha = opacity),
                            topLeft = Offset(
                                x - particle.size / 2,
                                y - particle.size / 2
                            ),
                            size = androidx.compose.ui.geometry.Size(
                                particle.size,
                                particle.size
                            )
                        )
                    }
                }
            }
        }
    }
}

/**
 * Confetti burst that appears at the top and falls down
 * 
 * Perfect for celebrating achievements with a shower of confetti
 * falling from the top of the screen.
 * 
 * @param trigger Change this value to trigger a new confetti burst
 * @param particleCount Number of confetti particles to generate
 * @param colors List of colors for confetti particles
 * @param durationMillis Duration of the animation in milliseconds
 * @param spreadWidth How wide the confetti spreads horizontally (0-1)
 * @param modifier Modifier for the confetti canvas
 */
@Composable
fun ConfettiBurst(
    trigger: Int,
    particleCount: Int = 60,
    colors: List<Color> = listOf(
        Color(0xFFFF6B6B),
        Color(0xFFFFD93D),
        Color(0xFF6BCF7F),
        Color(0xFF4ECDC4),
        Color(0xFF95E1D3),
        Color(0xFFE74C3C),
        Color(0xFF9B59B6),
        Color(0xFF3498DB)
    ),
    durationMillis: Int = 4000,
    spreadWidth: Float = 0.8f,
    modifier: Modifier = Modifier
) {
    val progress = remember { Animatable(0f) }
    
    val particles = remember(trigger) {
        List(particleCount) { index ->
            val xPos = Random.nextFloat() * spreadWidth + (1f - spreadWidth) / 2f
            
            ConfettiParticle(
                initialX = xPos,
                initialY = -0.1f, // Start above screen
                velocityX = Random.nextFloat() * 100f - 50f, // Slight horizontal drift
                velocityY = Random.nextFloat() * 200f + 150f, // Downward velocity
                color = colors.random(),
                size = Random.nextFloat() * 10f + 5f,
                rotationSpeed = Random.nextFloat() * 360f - 180f,
                seed = Random.nextInt()
            )
        }
    }

    LaunchedEffect(trigger) {
        progress.snapTo(0f)
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = durationMillis,
                easing = LinearEasing
            )
        )
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        val canvasWidth = size.width
        val canvasHeight = size.height
        val currentProgress = progress.value
        
        if (currentProgress > 0f && currentProgress < 1f) {
            particles.forEach { particle ->
                val time = currentProgress * (durationMillis / 1000f)
                
                // Add wobble effect
                val wobble = sin(time * 3f + particle.seed) * 30f
                
                val x = canvasWidth * particle.initialX + 
                       particle.velocityX * time + wobble
                val y = canvasHeight * particle.initialY + 
                       particle.velocityY * time
                
                // Calculate opacity (fade out when near bottom)
                val fadeStartY = canvasHeight * 0.9f
                val opacity = if (y < fadeStartY) {
                    1f
                } else {
                    1f - ((y - fadeStartY) / (canvasHeight * 0.1f))
                }.coerceIn(0f, 1f)
                
                val rotation = particle.rotationSpeed * time
                
                if (y <= canvasHeight + 50) {
                    rotate(
                        degrees = rotation,
                        pivot = Offset(x, y)
                    ) {
                        drawRect(
                            color = particle.color.copy(alpha = opacity),
                            topLeft = Offset(
                                x - particle.size / 2,
                                y - particle.size / 2
                            ),
                            size = androidx.compose.ui.geometry.Size(
                                particle.size,
                                particle.size
                            )
                        )
                    }
                }
            }
        }
    }
}

/**
 * Confetti overlay that can be placed on top of content
 * 
 * Use this to celebrate successful operations while keeping
 * the underlying content visible.
 * 
 * @param show Whether to show the confetti animation
 * @param onComplete Callback when animation completes
 * @param style Style of confetti animation (Explosion or Burst)
 * @param particleCount Number of confetti particles
 * @param modifier Modifier for the confetti overlay
 */
@Composable
fun ConfettiOverlay(
    show: Boolean,
    onComplete: () -> Unit = {},
    style: ConfettiStyle = ConfettiStyle.Explosion,
    particleCount: Int = 50,
    modifier: Modifier = Modifier
) {
    if (show) {
        Box(modifier = modifier) {
            when (style) {
                ConfettiStyle.Explosion -> {
                    ConfettiExplosion(
                        trigger = 1,
                        particleCount = particleCount,
                        modifier = Modifier.fillMaxSize()
                    )
                    LaunchedEffect(Unit) {
                        kotlinx.coroutines.delay(3000)
                        onComplete()
                    }
                }
                ConfettiStyle.Burst -> {
                    ConfettiBurst(
                        trigger = 1,
                        particleCount = particleCount,
                        modifier = Modifier.fillMaxSize()
                    )
                    LaunchedEffect(Unit) {
                        kotlinx.coroutines.delay(4000)
                        onComplete()
                    }
                }
            }
        }
    }
}

/**
 * Style of confetti animation
 */
enum class ConfettiStyle {
    /**
     * Explosion from center point
     */
    Explosion,
    
    /**
     * Burst falling from top
     */
    Burst
}
