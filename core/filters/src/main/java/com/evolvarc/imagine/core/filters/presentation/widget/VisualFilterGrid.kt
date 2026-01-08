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

package com.evolvarc.imagine.core.filters.presentation.widget

import android.graphics.Bitmap
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.evolvarc.imagine.core.filters.domain.FilterProvider
import com.evolvarc.imagine.core.filters.presentation.model.UiFilter

/**
 * Instagram-style visual filter grid
 * 
 * Displays filters in a 3-column grid with live preview thumbnails.
 * Enables quick visual browsing and comparison of filters.
 * Optimized for smooth scrolling with lazy loading.
 */
@Composable
fun VisualFilterGrid(
    filters: List<UiFilter<*>>,
    previewBitmap: Bitmap?,
    selectedFilter: UiFilter<*>?,
    onFilterClick: (UiFilter<*>) -> Unit,
    modifier: Modifier = Modifier,
    filterProvider: FilterProvider<Bitmap>,
    columns: Int = 3
) {
    val gridState = rememberLazyGridState()
    
    LazyVerticalGrid(
        columns = GridCells.Fixed(columns),
        state = gridState,
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        itemsIndexed(
            items = filters,
            key = { _, filter -> filter::class.simpleName ?: filter.hashCode() }
        ) { _, filter ->
            FilterPreviewThumbnail(
                filter = filter,
                previewBitmap = previewBitmap,
                isSelected = selectedFilter?.let { 
                    it::class.simpleName == filter::class.simpleName 
                } ?: false,
                onClick = { onFilterClick(filter) },
                modifier = Modifier.fillMaxWidth(),
                filterProvider = filterProvider
            )
        }
    }
}
