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

package com.evolvarc.imagine.core.ui.widget.buttons

import androidx.compose.animation.animateColorAsState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FrontHand
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.evolvarc.imagine.core.resources.R
import com.evolvarc.imagine.core.ui.theme.outlineVariant
import com.evolvarc.imagine.core.ui.widget.enhanced.EnhancedIconButton

@Composable
fun PanModeButton(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    EnhancedIconButton(
        modifier = modifier,
        containerColor = animateColorAsState(
            if (selected) MaterialTheme.colorScheme.primary
            else Color.Transparent
        ).value,
        contentColor = animateColorAsState(
            if (selected) MaterialTheme.colorScheme.onPrimary
            else MaterialTheme.colorScheme.onSurface
        ).value,
        borderColor = MaterialTheme.colorScheme.outlineVariant(
            luminance = 0.1f
        ),
        onClick = onClick
    ) {
        Icon(
            imageVector = Icons.Rounded.FrontHand,
            contentDescription = stringResource(R.string.draw)
        )
    }
}