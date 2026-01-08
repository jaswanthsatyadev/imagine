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

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.HelpOutline
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

/**
 * Contextual help icon with tooltip
 * 
 * Provides just-in-time help without cluttering the UI.
 * Follows Apple HIG for contextual help:
 * - Available but not intrusive
 * - Clear visual indicator (help icon)
 * - Concise explanatory text
 * - Appears on tap/hover
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelpTooltip(
    helpText: String,
    modifier: Modifier = Modifier,
    icon: ImageVector = Icons.Outlined.HelpOutline
) {
    val tooltipState = rememberTooltipState()
    
    TooltipBox(
        positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
        tooltip = {
            PlainTooltip {
                Text(
                    text = helpText,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        },
        state = tooltipState
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "Help",
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
            modifier = modifier
                .size(18.dp)
                .clickable { }
        )
    }
}

/**
 * Composable with inline help tooltip
 * 
 * Wraps any composable and adds a help icon next to it.
 * Perfect for complex controls that need explanation.
 */
@Composable
fun WithHelpTooltip(
    helpText: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(modifier = modifier) {
        content()
        HelpTooltip(
            helpText = helpText,
            modifier = Modifier.padding(start = 4.dp)
        )
    }
}
