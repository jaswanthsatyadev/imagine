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

package com.evolvarc.imagine.feature.cipher.data

import com.evolvarc.imagine.core.domain.saving.RandomStringGenerator
import com.evolvarc.imagine.feature.cipher.domain.CryptographyManager
import javax.inject.Inject

internal class AndroidRandomStringGenerator @Inject constructor(
    private val cryptographyManager: CryptographyManager
) : RandomStringGenerator {

    override fun generate(length: Int): String = cryptographyManager.generateRandomString(length)

}