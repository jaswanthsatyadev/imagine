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

@file:Suppress("unused", "UNCHECKED_CAST")

package com.evolvarc.imagine.core.ui.theme

import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.material3.MotionScheme
import com.evolvarc.imagine.core.domain.utils.cast
import com.evolvarc.imagine.core.ui.utils.animation.FancyTransitionEasing


internal val CustomMotionScheme: MotionScheme = object : MotionScheme {
    // Apple-inspired spring physics for buttery smooth animations
    val SpringDefaultSpatialDamping = 0.85f
    val SpringDefaultSpatialStiffness = 400.0f
    val SpringDefaultEffectsDamping = 0.95f
    val SpringDefaultEffectsStiffness = 1400.0f
    val SpringFastSpatialDamping = 0.75f
    val SpringFastSpatialStiffness = 900.0f
    val SpringFastEffectsDamping = 0.9f
    val SpringFastEffectsStiffness = 3200.0f
    val SpringSlowSpatialDamping = 0.88f
    val SpringSlowSpatialStiffness = 250.0f
    val SpringSlowEffectsDamping = 0.95f
    val SpringSlowEffectsStiffness = 900.0f

    private val defaultSpatialSpec =
        spring<Any>(
            dampingRatio = SpringDefaultSpatialDamping,
            stiffness = SpringDefaultSpatialStiffness
        )

    private val fastSpatialSpec =
        spring<Any>(
            dampingRatio = SpringFastSpatialDamping,
            stiffness = SpringFastSpatialStiffness
        )

    private val slowSpatialSpec =
        spring<Any>(
            dampingRatio = SpringSlowSpatialDamping,
            stiffness = SpringSlowSpatialStiffness
        )

    private val defaultEffectsSpec =
        spring<Any>(
            dampingRatio = SpringDefaultEffectsDamping,
            stiffness = SpringDefaultEffectsStiffness
        )

    private val fastEffectsSpec =
        tween<Any>(
            durationMillis = 300,
            easing = FancyTransitionEasing
        )

    private val slowEffectsSpec =
        tween<Any>(
            durationMillis = 500,
            easing = FancyTransitionEasing
        )

    override fun <T> defaultSpatialSpec(): FiniteAnimationSpec<T> = defaultSpatialSpec.cast()

    override fun <T> fastSpatialSpec(): FiniteAnimationSpec<T> = fastSpatialSpec.cast()

    override fun <T> slowSpatialSpec(): FiniteAnimationSpec<T> = slowSpatialSpec.cast()

    override fun <T> defaultEffectsSpec(): FiniteAnimationSpec<T> = defaultEffectsSpec.cast()

    override fun <T> fastEffectsSpec(): FiniteAnimationSpec<T> = fastEffectsSpec.cast()

    override fun <T> slowEffectsSpec(): FiniteAnimationSpec<T> = slowEffectsSpec.cast()
}