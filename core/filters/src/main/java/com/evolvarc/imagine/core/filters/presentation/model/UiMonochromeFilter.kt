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

package com.evolvarc.imagine.core.filters.presentation.model

import androidx.compose.ui.graphics.Color
import com.evolvarc.imagine.core.domain.model.ColorModel
import com.evolvarc.imagine.core.filters.domain.model.Filter
import com.evolvarc.imagine.core.filters.domain.model.FilterParam
import com.evolvarc.imagine.core.resources.R
import com.evolvarc.imagine.core.ui.utils.helper.toModel

class UiMonochromeFilter(
    override val value: Pair<Float, ColorModel> = 1f to Color(
        red = 0.6f,
        green = 0.45f,
        blue = 0.3f,
        alpha = 1.0f
    ).toModel()
) : UiFilter<Pair<Float, ColorModel>>(
    title = R.string.monochrome,
    value = value,
    paramsInfo = listOf(
        FilterParam(
            title = R.string.strength,
            valueRange = 0f..1f,
            roundTo = 2
        ),
        FilterParam(
            title = R.string.color,
            valueRange = 0f..0f
        )
    )
), Filter.Monochrome