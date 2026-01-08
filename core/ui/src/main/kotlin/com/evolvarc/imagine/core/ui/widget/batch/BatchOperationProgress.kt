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

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
 * Batch operation progress indicator
 * 
 * Displays progress for multi-image operations with:
 * - Linear progress bar showing completion percentage
 * - Current/total count (e.g., "15 / 42")
 * - Operation description
 * - Cancel button
 * - Completion state
 * 
 * Integrates with existing _done/_left pattern used throughout the app.
 */
@Composable
fun BatchOperationProgress(
    isProcessing: Boolean,
    done: Int,
    total: Int,
    operationTitle: String,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier,
    showCancelButton: Boolean = true
) {
    AnimatedVisibility(
        visible = isProcessing && total > 0,
        enter = expandVertically() + fadeIn(),
        exit = shrinkVertically() + fadeOut()
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .container(
                    shape = ShapeDefaults.extraLarge,
                    color = MaterialTheme.colorScheme.primaryContainer
                )
                .padding(16.dp)
        ) {
            // Header with title and count
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = operationTitle,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    
                    Text(
                        text = "$done / $total",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                    )
                }
                
                if (showCancelButton) {
                    EnhancedButton(
                        onClick = onCancel,
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Cancel,
                            contentDescription = stringResource(R.string.cancel),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(6.dp))
                        Text(
                            text = stringResource(R.string.cancel),
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }
            }
            
            Spacer(Modifier.height(12.dp))
            
            // Progress bar
            val progress by animateFloatAsState(
                targetValue = if (total > 0) done.toFloat() / total.toFloat() else 0f,
                animationSpec = tween(durationMillis = 300, easing = LinearEasing),
                label = "BatchProgress"
            )
            
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                strokeCap = ProgressIndicatorDefaults.CircularDeterminateStrokeCap
            )
            
            Spacer(Modifier.height(4.dp))
            
            // Percentage text
            Text(
                text = "${(progress * 100).toInt()}%",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.6f),
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}

/**
 * Compact batch progress indicator
 * 
 * Minimal version for tight spaces showing only progress bar and count.
 */
@Composable
fun CompactBatchProgress(
    done: Int,
    total: Int,
    modifier: Modifier = Modifier
) {
    val progress = if (total > 0) done.toFloat() / total.toFloat() else 0f
    
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$done / $total",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Text(
                text = "${(progress * 100).toInt()}%",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        }
        
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surfaceContainerHighest
        )
    }
}

/**
 * Batch operation completion indicator
 * 
 * Shows success/failure state after batch operation completes.
 */
@Composable
fun BatchOperationComplete(
    isSuccess: Boolean,
    totalProcessed: Int,
    message: String,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedContent(
        targetState = isSuccess,
        modifier = modifier
    ) { success ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .container(
                    shape = ShapeDefaults.extraLarge,
                    color = if (success) {
                        MaterialTheme.colorScheme.secondaryContainer
                    } else {
                        MaterialTheme.colorScheme.errorContainer
                    }
                )
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = if (success) Icons.Rounded.CheckCircle else Icons.Rounded.Cancel,
                contentDescription = null,
                tint = if (success) {
                    MaterialTheme.colorScheme.secondary
                } else {
                    MaterialTheme.colorScheme.error
                },
                modifier = Modifier.size(32.dp)
            )
            
            Spacer(Modifier.height(8.dp))
            
            Text(
                text = message,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = if (success) {
                    MaterialTheme.colorScheme.onSecondaryContainer
                } else {
                    MaterialTheme.colorScheme.onErrorContainer
                }
            )
            
            if (success && totalProcessed > 0) {
                Text(
                    text = "$totalProcessed ${stringResource(R.string.images)} ${stringResource(R.string.processed)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (success) {
                        MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
                    } else {
                        MaterialTheme.colorScheme.onErrorContainer.copy(alpha = 0.7f)
                    }
                )
            }
            
            Spacer(Modifier.height(12.dp))
            
            EnhancedButton(
                onClick = onDismiss,
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                Text(stringResource(R.string.close))
            }
        }
    }
}
