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
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Stop
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.evolvarc.imagine.core.resources.R
import com.evolvarc.imagine.core.ui.widget.enhanced.EnhancedIconButton
import com.evolvarc.imagine.core.ui.widget.modifier.ShapeDefaults
import com.evolvarc.imagine.core.ui.widget.modifier.container

/**
 * Operation state for queue management
 */
enum class OperationState {
    IDLE,
    RUNNING,
    PAUSED,
    COMPLETED,
    CANCELLED
}

/**
 * Operation queue card with pause/resume controls
 * 
 * Manages long-running batch operations with:
 * - Play/Pause/Stop controls
 * - Operation state display
 * - Progress tracking
 * - Queue information
 * 
 * Perfect for operations that may take significant time
 * and benefit from user control over execution.
 */
@Composable
fun OperationQueueCard(
    operationState: OperationState,
    queuedCount: Int,
    processedCount: Int,
    currentOperation: String,
    onPlay: () -> Unit,
    onPause: () -> Unit,
    onStop: () -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = operationState != OperationState.IDLE,
        enter = expandVertically() + fadeIn(),
        exit = shrinkVertically() + fadeOut()
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .container(
                    shape = ShapeDefaults.extraLarge,
                    color = when (operationState) {
                        OperationState.RUNNING -> MaterialTheme.colorScheme.primaryContainer
                        OperationState.PAUSED -> MaterialTheme.colorScheme.tertiaryContainer
                        OperationState.COMPLETED -> MaterialTheme.colorScheme.secondaryContainer
                        OperationState.CANCELLED -> MaterialTheme.colorScheme.errorContainer
                        else -> MaterialTheme.colorScheme.surfaceContainerHigh
                    }
                )
                .padding(16.dp)
        ) {
            // Header with state and controls
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    // Progress indicator or state icon
                    when (operationState) {
                        OperationState.RUNNING -> {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                strokeWidth = 2.dp,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        else -> {
                            Icon(
                                imageVector = when (operationState) {
                                    OperationState.PAUSED -> Icons.Rounded.Pause
                                    OperationState.COMPLETED -> Icons.Rounded.PlayArrow
                                    else -> Icons.Rounded.Stop
                                },
                                contentDescription = null,
                                tint = when (operationState) {
                                    OperationState.PAUSED -> MaterialTheme.colorScheme.tertiary
                                    OperationState.COMPLETED -> MaterialTheme.colorScheme.secondary
                                    else -> MaterialTheme.colorScheme.error
                                },
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                    
                    Spacer(Modifier.width(12.dp))
                    
                    Column {
                        Text(
                            text = when (operationState) {
                                OperationState.RUNNING -> stringResource(R.string.processing)
                                OperationState.PAUSED -> stringResource(R.string.paused)
                                OperationState.COMPLETED -> stringResource(R.string.completed)
                                OperationState.CANCELLED -> stringResource(R.string.cancelled)
                                else -> ""
                            },
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = when (operationState) {
                                OperationState.RUNNING -> MaterialTheme.colorScheme.onPrimaryContainer
                                OperationState.PAUSED -> MaterialTheme.colorScheme.onTertiaryContainer
                                OperationState.COMPLETED -> MaterialTheme.colorScheme.onSecondaryContainer
                                OperationState.CANCELLED -> MaterialTheme.colorScheme.onErrorContainer
                                else -> MaterialTheme.colorScheme.onSurface
                            }
                        )
                        
                        Text(
                            text = "$processedCount / $queuedCount ${stringResource(R.string.in_queue)}",
                            style = MaterialTheme.typography.bodySmall,
                            color = when (operationState) {
                                OperationState.RUNNING -> MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                                OperationState.PAUSED -> MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.7f)
                                OperationState.COMPLETED -> MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
                                OperationState.CANCELLED -> MaterialTheme.colorScheme.onErrorContainer.copy(alpha = 0.7f)
                                else -> MaterialTheme.colorScheme.onSurfaceVariant
                            }
                        )
                    }
                }
                
                // Control buttons
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    if (operationState == OperationState.RUNNING) {
                        EnhancedIconButton(
                            onClick = onPause,
                            containerColor = MaterialTheme.colorScheme.tertiaryContainer
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Pause,
                                contentDescription = stringResource(R.string.pause),
                                tint = MaterialTheme.colorScheme.onTertiaryContainer
                            )
                        }
                    } else if (operationState == OperationState.PAUSED) {
                        EnhancedIconButton(
                            onClick = onPlay,
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.PlayArrow,
                                contentDescription = stringResource(R.string.resume),
                                tint = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                    
                    if (operationState == OperationState.RUNNING || operationState == OperationState.PAUSED) {
                        EnhancedIconButton(
                            onClick = onStop,
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Stop,
                                contentDescription = stringResource(R.string.stop),
                                tint = MaterialTheme.colorScheme.onErrorContainer
                            )
                        }
                    }
                }
            }
            
            // Current operation description
            if (currentOperation.isNotEmpty() && operationState == OperationState.RUNNING) {
                Spacer(Modifier.height(8.dp))
                
                Text(
                    text = currentOperation,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f),
                    modifier = Modifier.padding(start = 36.dp)
                )
            }
        }
    }
}
