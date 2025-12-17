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
 */

package com.evolvarc.imagine.feature.main.presentation.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.outlined.Collections
import androidx.compose.material.icons.outlined.DocumentScanner
import androidx.compose.material.icons.outlined.GifBox
import androidx.compose.material.icons.outlined.PictureAsPdf
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.evolvarc.imagine.core.resources.R
import com.evolvarc.imagine.core.resources.icons.ImageConvert
import com.evolvarc.imagine.core.ui.utils.animation.EmphasizedDecelerateEasing
import com.evolvarc.imagine.core.ui.utils.navigation.Screen
import com.evolvarc.imagine.core.ui.widget.modifier.ShapeDefaults
import com.evolvarc.imagine.core.ui.widget.modifier.container
import com.evolvarc.imagine.core.ui.widget.preferences.PreferenceItemOverload

@Composable
fun ConvertHub(
    screenList: List<Screen>,
    onNavigate: (Screen) -> Unit,
    contentPadding: PaddingValues
) {
    // Animation state for staggered entry
    val heroAlpha = remember { Animatable(0f) }
    val heroTranslationY = remember { Animatable(50f) }

    val quickActionsAlpha = remember { Animatable(0f) }
    val quickActionsTranslationY = remember { Animatable(50f) }

    val essentialsAlpha = remember { Animatable(0f) }
    val essentialsTranslationY = remember { Animatable(50f) }

    LaunchedEffect(Unit) {
        heroAlpha.animateTo(1f, tween(300, 0, EmphasizedDecelerateEasing))
        heroTranslationY.animateTo(0f, tween(400, 0, EmphasizedDecelerateEasing))

        quickActionsAlpha.animateTo(1f, tween(300, 0, EmphasizedDecelerateEasing))
        quickActionsTranslationY.animateTo(0f, tween(400, 0, EmphasizedDecelerateEasing))

        essentialsAlpha.animateTo(1f, tween(300, 0, EmphasizedDecelerateEasing))
        essentialsTranslationY.animateTo(0f, tween(400, 0, EmphasizedDecelerateEasing))
    }

    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Adaptive(220.dp),
        verticalItemSpacing = 12.dp,
        horizontalArrangement = Arrangement.spacedBy(
            space = 12.dp,
            alignment = Alignment.CenterHorizontally
        ),
        contentPadding = contentPadding
    ) {
        item(span = StaggeredGridItemSpan.FullLine) {
            Box(
                modifier = Modifier.graphicsLayer {
                    alpha = heroAlpha.value
                    translationY = heroTranslationY.value
                }
            ) {
                HeroCard(
                    onClick = { onNavigate(Screen.FormatConversion()) }
                )
            }
        }

        item(span = StaggeredGridItemSpan.FullLine) {
            Box(
                modifier = Modifier.graphicsLayer {
                    alpha = quickActionsAlpha.value
                    translationY = quickActionsTranslationY.value
                }
            ) {
                QuickActionsRow(onNavigate = onNavigate)
            }
        }

        item(span = StaggeredGridItemSpan.FullLine) {
            Box(
                modifier = Modifier.graphicsLayer {
                    alpha = essentialsAlpha.value
                    translationY = essentialsTranslationY.value
                }
            ) {
                Text(
                    text = stringResource(R.string.tools),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
                )
            }
        }

        val filteredList = screenList.filter {
            it !is Screen.FormatConversion &&
            it !is Screen.PdfTools &&
            it !is Screen.GifTools &&
            it !is Screen.DocumentScanner
        }

        itemsIndexed(
            items = filteredList,
            key = { _, screen -> screen.id }
        ) { index, screen ->
            val itemAlpha = remember { Animatable(0f) }
            val itemTranslationY = remember { Animatable(50f) }

            LaunchedEffect(Unit) {
                val delay = 200 + (index * 30).coerceAtMost(500)
                itemAlpha.animateTo(1f, tween(300, delay, EmphasizedDecelerateEasing))
                itemTranslationY.animateTo(0f, tween(400, delay, EmphasizedDecelerateEasing))
            }

            Box(
                modifier = Modifier.graphicsLayer {
                    alpha = itemAlpha.value
                    translationY = itemTranslationY.value
                }
            ) {
                PreferenceItemOverload(
                    onClick = { onNavigate(screen) },
                    title = stringResource(screen.title),
                    subtitle = stringResource(screen.subtitle),
                    startIcon = {
                        screen.icon?.let { icon ->
                            Icon(imageVector = icon, contentDescription = null)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = ShapeDefaults.medium
                )
            }
        }
    }
}

@Composable
private fun HeroCard(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp)
            .height(180.dp)
            .container(
                resultPadding = 0.dp,
                color = MaterialTheme.colorScheme.tertiaryContainer,
                shape = ShapeDefaults.large
            )
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.format_conversion),
                    style = MaterialTheme.typography.displaySmall,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.format_conversion_sub),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.8f)
                )
            }
            Icon(
                imageVector = Icons.Outlined.Collections,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.onTertiaryContainer
            )
        }

        Icon(
            imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp)
                .size(24.dp),
            tint = MaterialTheme.colorScheme.onTertiaryContainer
        )
    }
}

@Composable
private fun QuickActionsRow(onNavigate: (Screen) -> Unit) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            text = "Popular",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            QuickActionItem(
                title = "PDF Tools",
                icon = Icons.Outlined.PictureAsPdf,
                onClick = { onNavigate(Screen.PdfTools()) },
                modifier = Modifier.weight(1f)
            )
            QuickActionItem(
                title = "GIF Tools",
                icon = Icons.Outlined.GifBox,
                onClick = { onNavigate(Screen.GifTools()) },
                modifier = Modifier.weight(1f)
            )
            QuickActionItem(
                title = "Scanner",
                icon = Icons.Outlined.DocumentScanner,
                onClick = { onNavigate(Screen.DocumentScanner) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun QuickActionItem(
    title: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .container(
                color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f),
                shape = ShapeDefaults.medium,
                resultPadding = 16.dp
            )
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSecondaryContainer,
            modifier = Modifier.size(28.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )
    }
}
