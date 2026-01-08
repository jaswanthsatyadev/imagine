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

package com.evolvarc.imagine.core.ui.widget.color_picker

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Error
import androidx.compose.material.icons.rounded.DoneAll
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.evolvarc.imagine.core.ui.theme.inverse
import com.evolvarc.imagine.core.ui.utils.helper.ContextUtils.pasteColorFromClipboard
import com.evolvarc.imagine.core.ui.utils.provider.LocalContainerColor
import com.evolvarc.imagine.core.ui.utils.provider.ProvideContainerDefaults
import com.evolvarc.imagine.core.ui.widget.enhanced.hapticsClickable
import com.evolvarc.imagine.core.ui.widget.enhanced.hapticsCombinedClickable
import com.evolvarc.imagine.core.ui.widget.modifier.ShapeDefaults
import com.evolvarc.imagine.core.ui.widget.modifier.animateShape
import com.evolvarc.imagine.core.ui.widget.modifier.container
import com.evolvarc.imagine.core.ui.widget.modifier.fadingEdges
import com.evolvarc.imagine.core.ui.widget.modifier.transparencyChecker
import com.evolvarc.imagine.core.ui.widget.other.LocalToastHostState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ColorSelectionRow(
    modifier: Modifier = Modifier,
    defaultColors: List<Color> = ColorSelectionRowDefaults.colorList,
    allowAlpha: Boolean = false,
    allowScroll: Boolean = true,
    value: Color,
    onValueChange: (Color) -> Unit,
    contentPadding: PaddingValues = PaddingValues(),
) {
    val scope = rememberCoroutineScope()
    val toastHostState = LocalToastHostState.current
    val context = LocalContext.current
    var customColor by remember { mutableStateOf<Color?>(null) }
    var showColorPicker by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()

    LaunchedEffect(value) {
        if (value !in defaultColors) {
            customColor = value
        }
    }

    LaunchedEffect(Unit) {
        delay(250)
        if (value == customColor) {
            listState.scrollToItem(0)
        } else if (value in defaultColors) {
            listState.scrollToItem(defaultColors.indexOf(value))
        }
    }

    val itemSize = 42.dp

    ProvideContainerDefaults(
        color = LocalContainerColor.current
    ) {
        LazyRow(
            state = listState,
            modifier = modifier
                .fillMaxWidth()
                .height(64.dp)
                .fadingEdges(listState),
            userScrollEnabled = allowScroll,
            contentPadding = contentPadding,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            item {
                val background = customColor ?: MaterialTheme.colorScheme.primary
                val isSelected = customColor != null
                val shape = animateShape(
                    if (isSelected) ShapeDefaults.mini
                    else RoundedCornerShape(itemSize / 2)
                )

                Box(
                    modifier = Modifier
                        .size(itemSize)
                        .aspectRatio(1f)
                        .scale(
                            animateFloatAsState(
                                targetValue = if (isSelected) 0.7f else 1f,
                                animationSpec = tween(400)
                            ).value
                        )
                        .rotate(
                            animateFloatAsState(
                                targetValue = if (isSelected) 45f else 0f,
                                animationSpec = tween(400)
                            ).value
                        )
                        .container(
                            shape = shape,
                            color = background,
                            resultPadding = 0.dp
                        )
                        .transparencyChecker()
                        .background(background, shape)
                        .hapticsCombinedClickable(
                            onLongClick = {
                                context.pasteColorFromClipboard(
                                    onPastedColor = {
                                        val color = if (allowAlpha) it else it.copy(1f)

                                        onValueChange(color)
                                        customColor = color
                                    },
                                    onPastedColorFailure = { message ->
                                        scope.launch {
                                            toastHostState.showToast(
                                                message = message,
                                                icon = Icons.Outlined.Error
                                            )
                                        }
                                    }
                                )
                            },
                            onClick = {
                                showColorPicker = true
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Palette,
                        contentDescription = null,
                        tint = background.inverse(
                            fraction = {
                                if (it) 0.8f
                                else 0.5f
                            },
                            darkMode = background.luminance() < 0.3f
                        ),
                        modifier = Modifier
                            .size(32.dp)
                            .background(
                                color = background.copy(alpha = 1f),
                                shape = shape
                            )
                            .padding(4.dp)
                            .rotate(
                                animateFloatAsState(
                                    targetValue = if (isSelected) -45f else 0f,
                                    animationSpec = tween(400)
                                ).value
                            )
                    )
                }
            }
            items(
                items = defaultColors,
                key = { it.toArgb() }
            ) { color ->
                val isSelected = value == color && customColor == null
                val shape = animateShape(
                    if (isSelected) ShapeDefaults.mini
                    else RoundedCornerShape(itemSize / 2)
                )

                Box(
                    Modifier
                        .size(itemSize)
                        .aspectRatio(1f)
                        .scale(
                            animateFloatAsState(
                                targetValue = if (isSelected) 0.7f else 1f,
                                animationSpec = tween(400)
                            ).value
                        )
                        .rotate(
                            animateFloatAsState(
                                targetValue = if (isSelected) 45f else 0f,
                                animationSpec = tween(400)
                            ).value
                        )
                        .container(
                            shape = shape,
                            color = color,
                            resultPadding = 0.dp
                        )
                        .transparencyChecker()
                        .background(color, shape)
                        .hapticsClickable {
                            onValueChange(color.copy(if (allowAlpha) color.alpha else 1f))
                            customColor = null
                        },
                    contentAlignment = Alignment.Center
                ) {
                    AnimatedVisibility(isSelected) {
                        Icon(
                            imageVector = Icons.Rounded.DoneAll,
                            contentDescription = null,
                            tint = color.inverse(
                                fraction = {
                                    if (it) 0.8f
                                    else 0.5f
                                },
                                darkMode = color.luminance() < 0.3f
                            ),
                            modifier = Modifier
                                .size(24.dp)
                                .rotate(
                                    animateFloatAsState(
                                        targetValue = if (isSelected) -45f else 0f,
                                        animationSpec = tween(400)
                                    ).value
                                )
                        )
                    }
                }
            }
        }
    }

    ColorPickerSheet(
        visible = showColorPicker,
        onDismiss = { showColorPicker = false },
        color = customColor,
        onColorSelected = {
            val color = it.copy(if (allowAlpha) it.alpha else 1f)
            onValueChange(color)
            customColor = color
        },
        allowAlpha = allowAlpha
    )
}


object ColorSelectionRowDefaults {
    val colorList by lazy {
        listOf(
            Color(0xFFFF3B30),
            Color(0xFFFF2D55),
            Color(0xFFE63946),
            Color(0xFFFF6B35),
            Color(0xFFFF9500),
            Color(0xFFFFCC00),
            Color(0xFFFFD60A),
            Color(0xFFCBF042),
            Color(0xFF34C759),
            Color(0xFF30D158),
            Color(0xFF32D74B),
            Color(0xFF64D2FF),
            Color(0xFF007AFF),
            Color(0xFF5E5CE6),
            Color(0xFFBF5AF2),
            Color(0xFFAF52DE),
            Color(0xFFFF2D92),
            Color(0xFFFF375F),
            Color(0xFF5856D6),
            Color(0xFF0A84FF),
            Color(0xFFFFFFFF),
            Color(0xFFAEAEB2),
            Color(0xFF636366),
            Color(0xFF000000),
        )
    }
}