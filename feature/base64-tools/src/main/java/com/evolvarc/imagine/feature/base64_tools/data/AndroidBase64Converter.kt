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

package com.evolvarc.imagine.feature.base64_tools.data

import android.graphics.Bitmap
import android.util.Base64
import com.evolvarc.imagine.core.domain.coroutines.DispatchersHolder
import com.evolvarc.imagine.core.domain.image.ImageCompressor
import com.evolvarc.imagine.core.domain.image.ImageGetter
import com.evolvarc.imagine.core.domain.image.ShareProvider
import com.evolvarc.imagine.core.domain.image.model.ImageFormat
import com.evolvarc.imagine.core.domain.image.model.Quality
import com.evolvarc.imagine.core.domain.saving.FileController
import com.evolvarc.imagine.feature.base64_tools.domain.Base64Converter
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class AndroidBase64Converter @Inject constructor(
    private val imageGetter: ImageGetter<Bitmap>,
    private val fileController: FileController,
    private val shareProvider: ShareProvider,
    private val imageCompressor: ImageCompressor<Bitmap>,
    dispatchersHolder: DispatchersHolder
) : Base64Converter, DispatchersHolder by dispatchersHolder {

    override suspend fun decode(
        base64: String
    ): String? = withContext(ioDispatcher) {
        val decoded = runCatching {
            Base64.decode(base64, Base64.DEFAULT or Base64.NO_WRAP)
        }.getOrNull() ?: return@withContext null

        imageGetter.getImage(decoded)?.let { bitmap ->
            shareProvider.cacheData(
                writeData = {
                    it.writeBytes(
                        imageCompressor.compress(
                            image = bitmap,
                            imageFormat = ImageFormat.Png.Lossless,
                            quality = Quality.Base()
                        )
                    )
                },
                filename = "Base64_decoded_${System.currentTimeMillis()}.png"
            )
        }
    }

    override suspend fun encode(
        uri: String
    ): String = withContext(ioDispatcher) {
        Base64.encodeToString(fileController.readBytes(uri), Base64.DEFAULT or Base64.NO_WRAP)
    }

}