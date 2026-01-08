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

package com.evolvarc.imagine.core.ui.widget.disclosure

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.evolvarc.imagine.core.ui.widget.modifier.ShapeDefaults

enum class InfoChipType {
    INFO,
    TIP,
    WARNING,
    SUCCESS
}

/**
 * Inline information chip
 * 
 * Provides contextual information without being intrusive.
 * Different types for different contexts:
 * - INFO: General information (blue)
 * - TIP: Helpful suggestions (green)
 * - WARNING: Cautions (orange)
 * - SUCCESS: Confirmations (green)
 * 
 * Perfect for:
 * - Explaining feature behavior
 * - Providing usage tips
 * - Warning about consequences
 * - Confirming successful actions
 */
@Composable
fun InfoChip(
    text: String,
    modifier: Modifier = Modifier,
    type: InfoChipType = InfoChipType.INFO,
    icon: ImageVector? = Icons.Outlined.Info,
    maxLines: Int = 2
) {
    val (containerColor, contentColor) = when (type) {
        InfoChipType.INFO -> MaterialTheme.colorScheme.primaryContainer to MaterialTheme.colorScheme.onPrimaryContainer
        InfoChipType.TIP -> MaterialTheme.colorScheme.tertiaryContainer to MaterialTheme.colorScheme.onTertiaryContainer
        InfoChipType.WARNING -> MaterialTheme.colorScheme.errorContainer to MaterialTheme.colorScheme.onErrorContainer
        InfoChipType.SUCCESS -> MaterialTheme.colorScheme.secondaryContainer to MaterialTheme.colorScheme.onSecondaryContainer
    }
    
    Surface(
        modifier = modifier,
        shape = ShapeDefaults.medium,
        color = containerColor.copy(alpha = 0.7f)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            icon?.let {
                Icon(
                    imageVector = it,
                    contentDescription = null,
                    tint = contentColor,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(Modifier.width(8.dp))
            }
            
            Text(
                text = text,
                style = MaterialTheme.typography.bodySmall,
                color = contentColor,
                maxLines = maxLines,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

/**
 * Compact info chip for tight spaces
 * 
 * Smaller variant with single line of text.
 * Useful in cards, list items, or alongside controls.
 */
@Composable
fun CompactInfoChip(
    text: String,
    modifier: Modifier = Modifier,
    type: InfoChipType = InfoChipType.INFO
) {
    val contentColor = when (type) {
        InfoChipType.INFO -> MaterialTheme.colorScheme.primary
        InfoChipType.TIP -> MaterialTheme.colorScheme.tertiary
        InfoChipType.WARNING -> MaterialTheme.colorScheme.error
        InfoChipType.SUCCESS -> MaterialTheme.colorScheme.secondary
    }
    
    Text(
        text = text,
        style = MaterialTheme.typography.labelSmall,
        color = contentColor.copy(alpha = 0.8f),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier.padding(horizontal = 4.dp, vertical = 2.dp)
    )
}
