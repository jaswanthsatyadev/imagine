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
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Lightbulb
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.evolvarc.imagine.core.resources.R
import com.evolvarc.imagine.core.ui.widget.enhanced.EnhancedButton
import com.evolvarc.imagine.core.ui.widget.enhanced.EnhancedIconButton
import com.evolvarc.imagine.core.ui.widget.modifier.ShapeDefaults

/**
 * Quick help overlay for first-time feature discovery
 * 
 * Shows brief tips when users first encounter a feature.
 * Follows iOS coaching overlay patterns:
 * - Semi-transparent scrim to focus attention
 * - Clear, concise messaging
 * - Easy dismissal
 * - Appears once per feature (managed externally)
 * 
 * Perfect for:
 * - Onboarding new features
 * - Explaining complex gestures
 * - Highlighting powerful capabilities
 * - Reducing support requests
 */
@Composable
fun QuickHelpOverlay(
    visible: Boolean,
    title: String,
    description: String,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector = Icons.Rounded.Lightbulb,
    showDontShowAgain: Boolean = true,
    onDontShowAgain: (() -> Unit)? = null
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(spring()) + scaleIn(
            initialScale = 0.9f,
            animationSpec = spring(
                dampingRatio = 0.7f,
                stiffness = 300f
            )
        ),
        exit = fadeOut(spring()) + scaleOut(
            targetScale = 0.9f,
            animationSpec = spring(
                dampingRatio = 0.7f,
                stiffness = 300f
            )
        )
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { onDismiss() },
            contentAlignment = Alignment.Center
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { },
                shape = ShapeDefaults.superLarge,
                color = MaterialTheme.colorScheme.surfaceContainer,
                tonalElevation = 8.dp
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Header with icon and close button
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(32.dp)
                        )
                        
                        EnhancedIconButton(
                            onClick = onDismiss,
                            containerColor = MaterialTheme.colorScheme.surfaceContainerHighest
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Close,
                                contentDescription = "Close",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    
                    Spacer(Modifier.height(16.dp))
                    
                    // Title
                    Text(
                        text = title,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center
                    )
                    
                    Spacer(Modifier.height(12.dp))
                    
                    // Description
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        lineHeight = MaterialTheme.typography.bodyMedium.lineHeight
                    )
                    
                    Spacer(Modifier.height(24.dp))
                    
                    // Action buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = if (showDontShowAgain) {
                            Arrangement.spacedBy(12.dp)
                        } else {
                            Arrangement.Center
                        }
                    ) {
                        if (showDontShowAgain && onDontShowAgain != null) {
                            EnhancedButton(
                                onClick = {
                                    onDontShowAgain()
                                    onDismiss()
                                },
                                containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = stringResource(R.string.dont_show),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        
                        EnhancedButton(
                            onClick = onDismiss,
                            containerColor = MaterialTheme.colorScheme.primary,
                            modifier = if (showDontShowAgain) {
                                Modifier.weight(1f)
                            } else {
                                Modifier.fillMaxWidth(0.5f)
                            }
                        ) {
                            Text(
                                text = stringResource(R.string.got_it),
                                color = MaterialTheme.colorScheme.onPrimary,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Compact inline tip card
 * 
 * Smaller alternative for persistent tips that don't need
 * a full overlay. Can be embedded directly in screens.
 */
@Composable
fun TipCard(
    tip: String,
    modifier: Modifier = Modifier,
    icon: ImageVector = Icons.Rounded.Lightbulb,
    onDismiss: (() -> Unit)? = null
) {
    Surface(
        modifier = modifier,
        shape = ShapeDefaults.large,
        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
            
            Spacer(Modifier.width(12.dp))
            
            Text(
                text = tip,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.weight(1f)
            )
            
            onDismiss?.let {
                Spacer(Modifier.width(8.dp))
                EnhancedIconButton(
                    onClick = it,
                    containerColor = Color.Transparent
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Close,
                        contentDescription = "Dismiss",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}
