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

package com.evolvarc.imagine.feature.settings.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.evolvarc.imagine.core.domain.TELEGRAM_GROUP_LINK
import com.evolvarc.imagine.core.resources.R
import com.evolvarc.imagine.core.resources.icons.Telegram
import com.evolvarc.imagine.core.ui.widget.modifier.ShapeDefaults
import com.evolvarc.imagine.core.ui.widget.preferences.PreferenceRow

@Composable
fun TelegramGroupSettingItem(
    shape: Shape = ShapeDefaults.center,
    modifier: Modifier = Modifier.padding(horizontal = 8.dp)
) {
    val linkHandler = LocalUriHandler.current
    PreferenceRow(
        shape = shape,
        onClick = {
            linkHandler.openUri(TELEGRAM_GROUP_LINK)
        },
        startIcon = Icons.Rounded.Telegram,
        title = stringResource(R.string.tg_chat),
        subtitle = stringResource(R.string.tg_chat_sub),
        color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f),
        contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
        modifier = modifier
    )
}