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

package com.evolvarc.imagine.core.ui.widget.color_picker

import androidx.compose.ui.graphics.Color
import com.t8rin.dynamic.theme.ColorTuple
import com.t8rin.dynamic.theme.calculateSecondaryColor
import com.t8rin.dynamic.theme.calculateSurfaceColor
import com.t8rin.dynamic.theme.calculateTertiaryColor
import com.evolvarc.imagine.core.ui.theme.toColor

object ColorTupleDefaults {
    val defaultColorTuples by lazy {
        listOf(
            Color(0xFFFF3B30),
            Color(0xFFE63946),
            Color(0xFFFF6B35),
            Color(0xFFFF9500),
            Color(0xFFFFD60A),
            Color(0xFF34C759),
            Color(0xFF30D158),
            Color(0xFF32D74B),
            Color(0xFF007AFF),
            Color(0xFF0A84FF),
            Color(0xFF5E5CE6),
            Color(0xFFBF5AF2),
            Color(0xFFAF52DE),
            Color(0xFF5856D6),
            Color(0xFFFFFFFF),
            Color(0xFF000000),
        ).map {
            ColorTuple(
                primary = it,
                secondary = it.calculateSecondaryColor().toColor(),
                tertiary = it.calculateTertiaryColor().toColor(),
                surface = it.calculateSurfaceColor().toColor()
            )
        }
    }
}