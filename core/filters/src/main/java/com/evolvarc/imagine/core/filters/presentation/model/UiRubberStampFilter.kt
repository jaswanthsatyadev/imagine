/*
 * Imagine is an image editor for android
 * Copyright (c) 2025 Jaswanth Satya Dev
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

import com.evolvarc.imagine.core.filters.domain.model.Filter
import com.evolvarc.imagine.core.filters.domain.model.params.RubberStampParams
import com.evolvarc.imagine.core.resources.R

class UiRubberStampFilter(
    override val value: RubberStampParams = RubberStampParams.Default
) : UiFilter<RubberStampParams>(
    title = R.string.rubber_stmp,
    paramsInfo = listOf(
        R.string.threshold paramTo 0f..1f,
        R.string.brush_softness paramTo 0f..1f,
        R.string.radius paramTo 0f..2f,
        R.string.first_color paramTo 0f..0f,
        R.string.second_color paramTo 0f..0f
    ),
    value = value
), Filter.RubberStamp