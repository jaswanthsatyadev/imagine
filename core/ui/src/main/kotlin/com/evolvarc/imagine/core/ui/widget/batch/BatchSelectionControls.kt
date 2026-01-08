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

package com.evolvarc.imagine.core.ui.widget.batch

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.SelectAll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.evolvarc.imagine.core.resources.R
import com.evolvarc.imagine.core.ui.widget.enhanced.EnhancedButton
import com.evolvarc.imagine.core.ui.widget.modifier.ShapeDefaults
import com.evolvarc.imagine.core.ui.widget.modifier.container

/**
 * Batch selection controls
 * 
 * Provides quick actions for multi-image selection:
 * - Select all
 * - Clear selection
 * - Selection count display
 * 
 * Appears when images can be batch-selected.
 */
@Composable
fun BatchSelectionControls(
    selectedCount: Int,
    totalCount: Int,
    onSelectAll: () -> Unit,
    onClearSelection: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    AnimatedVisibility(
        visible = totalCount > 0,
        enter = expandVertically() + fadeIn(),
        exit = shrinkVertically() + fadeOut()
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .container(
                    shape = ShapeDefaults.large,
                    color = if (selectedCount > 0) {
                        MaterialTheme.colorScheme.primaryContainer
                    } else {
                        MaterialTheme.colorScheme.surfaceContainerHigh
                    }
                )
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Selection count
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Rounded.CheckCircle,
                    contentDescription = null,
                    tint = if (selectedCount > 0) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    },
                    modifier = Modifier.size(20.dp)
                )
                
                Spacer(Modifier.width(8.dp))
                
                Text(
                    text = if (selectedCount > 0) {
                        "$selectedCount / $totalCount ${stringResource(R.string.selected)}"
                    } else {
                        stringResource(R.string.select_images)
                    },
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = if (selectedCount > 0) FontWeight.SemiBold else FontWeight.Normal,
                    color = if (selectedCount > 0) {
                        MaterialTheme.colorScheme.onPrimaryContainer
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    }
                )
            }
            
            // Action buttons
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                if (selectedCount > 0) {
                    EnhancedButton(
                        onClick = onClearSelection,
                        enabled = enabled,
                        containerColor = MaterialTheme.colorScheme.surfaceContainerHighest
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Clear,
                            contentDescription = stringResource(R.string.clear),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = stringResource(R.string.clear),
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
                
                if (selectedCount < totalCount) {
                    EnhancedButton(
                        onClick = onSelectAll,
                        enabled = enabled,
                        containerColor = MaterialTheme.colorScheme.primary
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.SelectAll,
                            contentDescription = stringResource(R.string.select_all),
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text = stringResource(R.string.select_all),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }
        }
    }
}
