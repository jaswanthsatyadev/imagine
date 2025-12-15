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

package com.evolvarc.imagine.app.presentation

import android.content.Intent
import androidx.compose.runtime.Composable
import com.arkivanov.decompose.retainedComponent
import com.evolvarc.imagine.core.ui.utils.ComposeActivity
import com.evolvarc.imagine.feature.root.presentation.RootContent
import com.evolvarc.imagine.feature.root.presentation.screenLogic.RootComponent
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AppActivity : ComposeActivity() {

    @Inject
    lateinit var rootComponentFactory: RootComponent.Factory

    private val component: RootComponent by lazy {
        retainedComponent(factory = rootComponentFactory::invoke)
    }

    override fun handleIntent(intent: Intent) = component.handleDeeplinks(intent)

    @Composable
    override fun Content() = RootContent(component = component)

}