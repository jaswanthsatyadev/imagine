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

import com.evolvarc.imagine.core.domain.utils.NEAREST_ODD_ROUNDING
import com.evolvarc.imagine.core.filters.domain.model.Filter
import com.evolvarc.imagine.core.filters.domain.model.FilterParam
import com.evolvarc.imagine.core.resources.R

class UiCrossBlurFilter(
    override val value: Float = 25f,
) : UiFilter<Float>(
    title = R.string.cross_blur,
    value = value,
    paramsInfo = listOf(
        FilterParam(
            title = null,
            valueRange = 3f..200f,
            roundTo = NEAREST_ODD_ROUNDING
        )
    )
), Filter.CrossBlur