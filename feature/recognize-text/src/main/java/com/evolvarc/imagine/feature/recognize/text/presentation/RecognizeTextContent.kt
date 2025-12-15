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

package com.evolvarc.imagine.feature.recognize.text.presentation

import android.net.Uri
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.evolvarc.imagine.core.domain.model.MimeType
import com.evolvarc.imagine.core.resources.R
import com.evolvarc.imagine.core.ui.utils.content_pickers.rememberFileCreator
import com.evolvarc.imagine.core.ui.utils.content_pickers.rememberImagePicker
import com.evolvarc.imagine.core.ui.utils.helper.ImageUtils.safeAspectRatio
import com.evolvarc.imagine.core.ui.utils.helper.isPortraitOrientationAsState
import com.evolvarc.imagine.core.ui.utils.navigation.Screen
import com.evolvarc.imagine.core.ui.utils.provider.rememberLocalEssentials
import com.evolvarc.imagine.core.ui.widget.AdaptiveLayoutScreen
import com.evolvarc.imagine.core.ui.widget.buttons.ShareButton
import com.evolvarc.imagine.core.ui.widget.buttons.ZoomButton
import com.evolvarc.imagine.core.ui.widget.dialogs.LoadingDialog
import com.evolvarc.imagine.core.ui.widget.enhanced.EnhancedIconButton
import com.evolvarc.imagine.core.ui.widget.image.AutoFilePicker
import com.evolvarc.imagine.core.ui.widget.image.Picture
import com.evolvarc.imagine.core.ui.widget.image.UrisPreview
import com.evolvarc.imagine.core.ui.widget.modifier.animateContentSizeNoClip
import com.evolvarc.imagine.core.ui.widget.modifier.container
import com.evolvarc.imagine.core.ui.widget.other.TopAppBarEmoji
import com.evolvarc.imagine.core.ui.widget.sheets.ZoomModalSheet
import com.evolvarc.imagine.core.ui.widget.text.TopAppBarTitle
import com.evolvarc.imagine.core.ui.widget.utils.AutoContentBasedColors
import com.evolvarc.imagine.feature.recognize.text.presentation.components.RecognizeTextButtons
import com.evolvarc.imagine.feature.recognize.text.presentation.components.RecognizeTextControls
import com.evolvarc.imagine.feature.recognize.text.presentation.components.RecognizeTextDownloadDataDialog
import com.evolvarc.imagine.feature.recognize.text.presentation.components.RecognizeTextNoDataControls
import com.evolvarc.imagine.feature.recognize.text.presentation.screenLogic.RecognizeTextComponent
import com.evolvarc.imagine.feature.single_edit.presentation.components.CropEditOption


@Composable
fun RecognizeTextContent(
    component: RecognizeTextComponent
) {
    val type = component.type
    val isExtraction = type is Screen.RecognizeText.Type.Extraction

    val isHaveText = component.editedText.orEmpty().isNotEmpty()

    val essentials = rememberLocalEssentials()
    val showConfetti: () -> Unit = essentials::showConfetti

    val startRecognition = {
        component.startRecognition(
            onFailure = essentials::showFailureToast
        )
    }

    LaunchedEffect(component.initialType) {
        component.initialType?.let {
            component.updateType(
                type = it,
                onImageSet = startRecognition
            )
        }
    }

    AutoContentBasedColors(
        model = (type as? Screen.RecognizeText.Type.Extraction)?.uri
    )

    LaunchedEffect(component.previewBitmap, component.filtersAdded) {
        if (component.previewBitmap != null) startRecognition()
    }

    val multipleImagePicker = rememberImagePicker { uris: List<Uri> ->
        when {
            isExtraction || (uris.size == 1) -> {
                component.updateType(
                    type = Screen.RecognizeText.Type.Extraction(uris.firstOrNull()),
                    onImageSet = startRecognition
                )
            }

            type is Screen.RecognizeText.Type.WriteToFile -> {
                component.updateType(
                    type = Screen.RecognizeText.Type.WriteToFile(uris),
                    onImageSet = startRecognition
                )
            }

            type is Screen.RecognizeText.Type.WriteToMetadata -> {
                component.updateType(
                    type = Screen.RecognizeText.Type.WriteToMetadata(uris),
                    onImageSet = startRecognition
                )
            }

            type == null -> {
                component.showSelectionTypeSheet(uris)
            }
        }
    }

    val addImagesImagePicker = rememberImagePicker { uris: List<Uri> ->
        when (type) {
            is Screen.RecognizeText.Type.WriteToFile -> {
                component.updateType(
                    type = Screen.RecognizeText.Type.WriteToFile(
                        type.uris?.plus(uris)?.distinct()
                    ),
                    onImageSet = startRecognition
                )
            }

            is Screen.RecognizeText.Type.WriteToMetadata -> {
                component.updateType(
                    type = Screen.RecognizeText.Type.WriteToMetadata(
                        type.uris?.plus(uris)?.distinct()
                    ),
                    onImageSet = startRecognition
                )
            }

            else -> Unit
        }
    }

    AutoFilePicker(
        onAutoPick = multipleImagePicker::pickImage,
        isPickedAlready = component.initialType != null
    )

    val isPortrait by isPortraitOrientationAsState()

    val saveLauncher = rememberFileCreator(
        mimeType = MimeType.Txt,
        onSuccess = { uri ->
            component.saveContentToTxt(
                uri = uri,
                onResult = essentials::parseFileSaveResult
            )
        }
    )

    var showCropper by rememberSaveable { mutableStateOf(false) }

    AdaptiveLayoutScreen(
        shouldDisableBackHandler = true,
        title = {
            AnimatedContent(
                targetState = component.recognitionData to type
            ) { (data, type) ->
                TopAppBarTitle(
                    title = if (data == null) {
                        when (type) {
                            null -> stringResource(R.string.recognize_text)
                            else -> stringResource(type.title)
                        }
                    } else {
                        stringResource(
                            R.string.accuracy,
                            data.accuracy
                        )
                    },
                    input = type,
                    isLoading = component.isTextLoading,
                    size = null
                )
            }
        },
        onGoBack = component.onGoBack,
        topAppBarPersistentActions = {
            if (type == null) TopAppBarEmoji()

            if (type is Screen.RecognizeText.Type.Extraction) {
                var showZoomSheet by rememberSaveable { mutableStateOf(false) }

                ZoomButton(
                    onClick = { showZoomSheet = true },
                    visible = true
                )
                ZoomModalSheet(
                    data = type.uri,
                    visible = showZoomSheet,
                    onDismiss = {
                        showZoomSheet = false
                    },
                    transformations = component.getTransformations()
                )
            }
        },
        actions = {
            ShareButton(
                onShare = {
                    if (isExtraction) {
                        component.shareEditedText(
                            onComplete = showConfetti
                        )
                    } else {
                        component.shareData(
                            onComplete = showConfetti
                        )
                    }
                },
                enabled = isHaveText || !isExtraction
            )
            if (isExtraction) {
                EnhancedIconButton(
                    onClick = {
                        saveLauncher.make(component.generateTextFilename())
                    },
                    enabled = !component.text.isNullOrEmpty()
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Save,
                        contentDescription = null
                    )
                }
            }
        },
        imagePreview = {
            if (isExtraction) {
                Box(
                    modifier = Modifier
                        .container()
                        .padding(4.dp)
                        .animateContentSizeNoClip(
                            alignment = Alignment.Center
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Picture(
                        model = component.previewBitmap,
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier.aspectRatio(
                            component.previewBitmap?.safeAspectRatio ?: 1f
                        ),
                        transformations = component.getTransformations(),
                        shape = MaterialTheme.shapes.medium,
                        isLoadingFromDifferentPlace = component.isImageLoading
                    )
                }
            } else {
                UrisPreview(
                    modifier = Modifier
                        .then(
                            if (!isPortrait) {
                                Modifier
                                    .layout { measurable, constraints ->
                                        val placeable = measurable.measure(
                                            constraints = constraints.copy(
                                                maxHeight = constraints.maxHeight + 48.dp.roundToPx()
                                            )
                                        )
                                        layout(placeable.width, placeable.height) {
                                            placeable.place(0, 0)
                                        }
                                    }
                                    .verticalScroll(rememberScrollState())
                            } else Modifier
                        )
                        .padding(vertical = 24.dp),
                    uris = component.uris,
                    isPortrait = true,
                    onRemoveUri = component::removeUri,
                    onAddUris = addImagesImagePicker::pickImage
                )
            }
        },
        showImagePreviewAsStickyHeader = isExtraction,
        controls = {
            RecognizeTextControls(
                component = component,
                onShowCropper = { showCropper = true }
            )
        },
        buttons = { actions ->
            RecognizeTextButtons(
                component = component,
                multipleImagePicker = multipleImagePicker,
                actions = actions
            )
        },
        noDataControls = {
            RecognizeTextNoDataControls(component)
        },
        insetsForNoData = WindowInsets(0),
        contentPadding = animateDpAsState(
            if (component.type == null) 12.dp
            else 20.dp
        ).value,
        canShowScreenData = type != null
    )

    RecognizeTextDownloadDataDialog(component)

    LoadingDialog(
        visible = component.isExporting || component.isSaving,
        done = component.done,
        left = component.left,
        onCancelLoading = component::cancelSaving,
        canCancel = component.isSaving
    )

    CropEditOption(
        visible = showCropper,
        onDismiss = { showCropper = false },
        useScaffold = isPortrait,
        bitmap = component.previewBitmap,
        onGetBitmap = component::updateBitmap,
        cropProperties = component.cropProperties,
        setCropAspectRatio = component::setCropAspectRatio,
        setCropMask = component::setCropMask,
        selectedAspectRatio = component.selectedAspectRatio,
        loadImage = component::loadImage
    )
}