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

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.evolvarc.imagine.core.resources.R
import com.evolvarc.imagine.core.ui.widget.modifier.ShapeDefaults
import com.evolvarc.imagine.core.ui.widget.modifier.container

/**
 * Specialized card for advanced/expert options
 * 
 * Designed to hide complex features that only power users need,
 * while keeping the main UI clean for beginners.
 * 
 * Features:
 * - Distinctive styling to indicate "advanced" nature
 * - Icon + "Advanced" label for clarity
 * - Smooth expand/collapse with spring physics
 * - Optional badge showing number of active options
 */
@Composable
fun AdvancedOptionsCard(
    modifier: Modifier = Modifier,
    icon: ImageVector = Icons.Rounded.Tune,
    title: String = stringResource(R.string.advanced_options),
    defaultExpanded: Boolean = false,
    activeOptionsCount: Int? = null,
    content: @Composable () -> Unit
) {
    var isExpanded by rememberSaveable { mutableStateOf(defaultExpanded) }
    
    val arrowRotation by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f,
        animationSpec = spring(
            dampingRatio = 0.7f,
            stiffness = 400f
        ),
        label = "AdvancedOptionsArrow"
    )
    
    Column(
        modifier = modifier
            .fillMaxWidth()
            .container(
                shape = ShapeDefaults.extraLarge,
                color = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.5f),
                borderColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.3f)
            )
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(ShapeDefaults.extraLarge)
                .clickable { isExpanded = !isExpanded }
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.size(10.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
                
                // Active options badge
                activeOptionsCount?.let { count ->
                    if (count > 0) {
                        Text(
                            text = "($count)",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.tertiary,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(start = 6.dp)
                        )
                    }
                }
            }
            
            Icon(
                imageVector = Icons.Rounded.KeyboardArrowDown,
                contentDescription = if (isExpanded) "Collapse" else "Expand",
                tint = MaterialTheme.colorScheme.onTertiaryContainer,
                modifier = Modifier
                    .size(20.dp)
                    .rotate(arrowRotation)
            )
        }
        
        // Content
        AnimatedVisibility(
            visible = isExpanded,
            enter = expandVertically(
                animationSpec = spring(
                    dampingRatio = 0.8f,
                    stiffness = 380f
                )
            ) + fadeIn(),
            exit = shrinkVertically(
                animationSpec = spring(
                    dampingRatio = 0.8f,
                    stiffness = 380f
                )
            ) + fadeOut()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                content()
            }
        }
    }
}
