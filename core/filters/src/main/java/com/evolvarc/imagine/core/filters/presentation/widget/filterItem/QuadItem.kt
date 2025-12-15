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

package com.evolvarc.imagine.core.filters.presentation.widget.filterItem

import androidx.compose.runtime.Composable
import com.evolvarc.imagine.core.domain.model.ColorModel
import com.evolvarc.imagine.core.domain.utils.Quad
import com.evolvarc.imagine.core.domain.utils.cast
import com.evolvarc.imagine.core.filters.presentation.model.UiFilter
import com.evolvarc.imagine.core.filters.presentation.widget.filterItem.quad_components.NumberColorModelQuadItem
import com.evolvarc.imagine.core.filters.presentation.widget.filterItem.quad_components.NumberQuadItem


@Composable
internal fun QuadItem(
    value: Quad<*, *, *, *>,
    filter: UiFilter<Quad<*, *, *, *>>,
    onFilterChange: (value: Quad<*, *, *, *>) -> Unit,
    previewOnly: Boolean
) {
    when {
        value.first is Number && value.second is Number && value.third is Number && value.fourth is Number -> {
            NumberQuadItem(
                value = value.cast(),
                filter = filter,
                onFilterChange = onFilterChange,
                previewOnly = previewOnly
            )
        }

        value.first is Number && value.second is Number && value.third is Number && value.fourth is ColorModel -> {
            NumberColorModelQuadItem(
                value = value.cast(),
                filter = filter,
                onFilterChange = onFilterChange,
                previewOnly = previewOnly
            )
        }
    }
}