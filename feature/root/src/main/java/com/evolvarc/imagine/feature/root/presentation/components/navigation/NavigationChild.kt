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

package com.evolvarc.imagine.feature.root.presentation.components.navigation

import androidx.compose.runtime.Composable
import com.evolvarc.imagine.collage_maker.presentation.CollageMakerContent
import com.evolvarc.imagine.collage_maker.presentation.screenLogic.CollageMakerComponent
import com.evolvarc.imagine.color_tools.presentation.ColorToolsContent
import com.evolvarc.imagine.color_tools.presentation.screenLogic.ColorToolsComponent
import com.evolvarc.imagine.feature.apng_tools.presentation.ApngToolsContent
import com.evolvarc.imagine.feature.apng_tools.presentation.screenLogic.ApngToolsComponent
import com.evolvarc.imagine.feature.ascii_art.presentation.AsciiArtContent
import com.evolvarc.imagine.feature.ascii_art.presentation.screenLogic.AsciiArtComponent
import com.evolvarc.imagine.feature.audio_cover_extractor.ui.AudioCoverExtractorContent
import com.evolvarc.imagine.feature.audio_cover_extractor.ui.screenLogic.AudioCoverExtractorComponent
import com.evolvarc.imagine.feature.base64_tools.presentation.Base64ToolsContent
import com.evolvarc.imagine.feature.base64_tools.presentation.screenLogic.Base64ToolsComponent
import com.evolvarc.imagine.feature.checksum_tools.presentation.ChecksumToolsContent
import com.evolvarc.imagine.feature.checksum_tools.presentation.screenLogic.ChecksumToolsComponent
import com.evolvarc.imagine.feature.cipher.presentation.CipherContent
import com.evolvarc.imagine.feature.cipher.presentation.screenLogic.CipherComponent
import com.evolvarc.imagine.feature.compare.presentation.CompareContent
import com.evolvarc.imagine.feature.compare.presentation.screenLogic.CompareComponent
import com.evolvarc.imagine.feature.crop.presentation.CropContent
import com.evolvarc.imagine.feature.crop.presentation.screenLogic.CropComponent
import com.evolvarc.imagine.feature.delete_exif.presentation.DeleteExifContent
import com.evolvarc.imagine.feature.delete_exif.presentation.screenLogic.DeleteExifComponent
import com.evolvarc.imagine.feature.document_scanner.presentation.DocumentScannerContent
import com.evolvarc.imagine.feature.document_scanner.presentation.screenLogic.DocumentScannerComponent
import com.evolvarc.imagine.feature.draw.presentation.DrawContent
import com.evolvarc.imagine.feature.draw.presentation.screenLogic.DrawComponent
import com.evolvarc.imagine.feature.easter_egg.presentation.EasterEggContent
import com.evolvarc.imagine.feature.easter_egg.presentation.screenLogic.EasterEggComponent
import com.evolvarc.imagine.feature.edit_exif.presentation.EditExifContent
import com.evolvarc.imagine.feature.edit_exif.presentation.screenLogic.EditExifComponent
import com.evolvarc.imagine.feature.erase_background.presentation.EraseBackgroundContent
import com.evolvarc.imagine.feature.erase_background.presentation.screenLogic.EraseBackgroundComponent
import com.evolvarc.imagine.feature.filters.presentation.FiltersContent
import com.evolvarc.imagine.feature.filters.presentation.screenLogic.FiltersComponent
import com.evolvarc.imagine.feature.format_conversion.presentation.FormatConversionContent
import com.evolvarc.imagine.feature.format_conversion.presentation.screenLogic.FormatConversionComponent
import com.evolvarc.imagine.feature.gif_tools.presentation.GifToolsContent
import com.evolvarc.imagine.feature.gif_tools.presentation.screenLogic.GifToolsComponent
import com.evolvarc.imagine.feature.gradient_maker.presentation.GradientMakerContent
import com.evolvarc.imagine.feature.gradient_maker.presentation.screenLogic.GradientMakerComponent
import com.evolvarc.imagine.feature.image_preview.presentation.ImagePreviewContent
import com.evolvarc.imagine.feature.image_preview.presentation.screenLogic.ImagePreviewComponent
import com.evolvarc.imagine.feature.image_stacking.presentation.ImageStackingContent
import com.evolvarc.imagine.feature.image_stacking.presentation.screenLogic.ImageStackingComponent
import com.evolvarc.imagine.feature.image_stitch.presentation.ImageStitchingContent
import com.evolvarc.imagine.feature.image_stitch.presentation.screenLogic.ImageStitchingComponent
import com.evolvarc.imagine.feature.jxl_tools.presentation.JxlToolsContent
import com.evolvarc.imagine.feature.jxl_tools.presentation.screenLogic.JxlToolsComponent
import com.evolvarc.imagine.feature.libraries_info.presentation.LibrariesInfoContent
import com.evolvarc.imagine.feature.libraries_info.presentation.screenLogic.LibrariesInfoComponent
import com.evolvarc.imagine.feature.limits_resize.presentation.LimitsResizeContent
import com.evolvarc.imagine.feature.limits_resize.presentation.screenLogic.LimitsResizeComponent
import com.evolvarc.imagine.feature.load_net_image.presentation.LoadNetImageContent
import com.evolvarc.imagine.feature.load_net_image.presentation.screenLogic.LoadNetImageComponent
import com.evolvarc.imagine.feature.main.presentation.MainContent
import com.evolvarc.imagine.feature.main.presentation.screenLogic.MainComponent
import com.evolvarc.imagine.feature.markup_layers.presentation.MarkupLayersContent
import com.evolvarc.imagine.feature.markup_layers.presentation.screenLogic.MarkupLayersComponent
import com.evolvarc.imagine.feature.mesh_gradients.presentation.MeshGradientsContent
import com.evolvarc.imagine.feature.mesh_gradients.presentation.screenLogic.MeshGradientsComponent
import com.evolvarc.imagine.feature.palette_tools.presentation.PaletteToolsContent
import com.evolvarc.imagine.feature.palette_tools.presentation.screenLogic.PaletteToolsComponent
import com.evolvarc.imagine.feature.pdf_tools.presentation.PdfToolsContent
import com.evolvarc.imagine.feature.pdf_tools.presentation.screenLogic.PdfToolsComponent
import com.evolvarc.imagine.feature.pick_color.presentation.PickColorFromImageContent
import com.evolvarc.imagine.feature.pick_color.presentation.screenLogic.PickColorFromImageComponent
import com.evolvarc.imagine.feature.recognize.text.presentation.RecognizeTextContent
import com.evolvarc.imagine.feature.recognize.text.presentation.screenLogic.RecognizeTextComponent
import com.evolvarc.imagine.feature.resize_convert.presentation.ResizeAndConvertContent
import com.evolvarc.imagine.feature.resize_convert.presentation.screenLogic.ResizeAndConvertComponent
import com.evolvarc.imagine.feature.scan_qr_code.presentation.ScanQrCodeContent
import com.evolvarc.imagine.feature.scan_qr_code.presentation.screenLogic.ScanQrCodeComponent
import com.evolvarc.imagine.feature.settings.presentation.SettingsContent
import com.evolvarc.imagine.feature.settings.presentation.screenLogic.SettingsComponent
import com.evolvarc.imagine.feature.single_edit.presentation.SingleEditContent
import com.evolvarc.imagine.feature.single_edit.presentation.screenLogic.SingleEditComponent
import com.evolvarc.imagine.feature.svg_maker.presentation.SvgMakerContent
import com.evolvarc.imagine.feature.svg_maker.presentation.screenLogic.SvgMakerComponent
import com.evolvarc.imagine.feature.wallpapers_export.presentation.WallpapersExportContent
import com.evolvarc.imagine.feature.wallpapers_export.presentation.screenLogic.WallpapersExportComponent
import com.evolvarc.imagine.feature.watermarking.presentation.WatermarkingContent
import com.evolvarc.imagine.feature.watermarking.presentation.screenLogic.WatermarkingComponent
import com.evolvarc.imagine.feature.webp_tools.presentation.WebpToolsContent
import com.evolvarc.imagine.feature.webp_tools.presentation.screenLogic.WebpToolsComponent
import com.evolvarc.imagine.feature.weight_resize.presentation.WeightResizeContent
import com.evolvarc.imagine.feature.weight_resize.presentation.screenLogic.WeightResizeComponent
import com.evolvarc.imagine.feature.zip.presentation.ZipContent
import com.evolvarc.imagine.feature.zip.presentation.screenLogic.ZipComponent
import com.evolvarc.imagine.image_cutting.presentation.ImageCutterContent
import com.evolvarc.imagine.image_cutting.presentation.screenLogic.ImageCutterComponent
import com.evolvarc.imagine.image_splitting.presentation.ImageSplitterContent
import com.evolvarc.imagine.image_splitting.presentation.screenLogic.ImageSplitterComponent
import com.evolvarc.imagine.library_details.presentation.LibraryDetailsContent
import com.evolvarc.imagine.library_details.presentation.screenLogic.LibraryDetailsComponent
import com.evolvarc.imagine.noise_generation.presentation.NoiseGenerationContent
import com.evolvarc.imagine.noise_generation.presentation.screenLogic.NoiseGenerationComponent


internal sealed interface NavigationChild {

    @Composable
    fun Content()


    class ApngTools(private val component: ApngToolsComponent) : NavigationChild {
        @Composable
        override fun Content() = ApngToolsContent(component)
    }

    class Cipher(private val component: CipherComponent) : NavigationChild {
        @Composable
        override fun Content() = CipherContent(component)
    }

    class CollageMaker(private val component: CollageMakerComponent) : NavigationChild {
        @Composable
        override fun Content() = CollageMakerContent(component)
    }

    class ColorTools(private val component: ColorToolsComponent) : NavigationChild {
        @Composable
        override fun Content() = ColorToolsContent(component)
    }

    class Compare(private val component: CompareComponent) : NavigationChild {
        @Composable
        override fun Content() = CompareContent(component)
    }

    class Crop(private val component: CropComponent) : NavigationChild {
        @Composable
        override fun Content() = CropContent(component)
    }

    class DeleteExif(private val component: DeleteExifComponent) : NavigationChild {
        @Composable
        override fun Content() = DeleteExifContent(component)
    }

    class DocumentScanner(private val component: DocumentScannerComponent) : NavigationChild {
        @Composable
        override fun Content() = DocumentScannerContent(component)
    }

    class Draw(private val component: DrawComponent) : NavigationChild {
        @Composable
        override fun Content() = DrawContent(component)
    }

    class EasterEgg(private val component: EasterEggComponent) : NavigationChild {
        @Composable
        override fun Content() = EasterEggContent(component)
    }

    class EraseBackground(private val component: EraseBackgroundComponent) : NavigationChild {
        @Composable
        override fun Content() = EraseBackgroundContent(component)
    }

    class Filter(private val component: FiltersComponent) : NavigationChild {
        @Composable
        override fun Content() = FiltersContent(component)
    }

    class FormatConversion(private val component: FormatConversionComponent) : NavigationChild {
        @Composable
        override fun Content() = FormatConversionContent(component)
    }

    class PaletteTools(private val component: PaletteToolsComponent) : NavigationChild {
        @Composable
        override fun Content() = PaletteToolsContent(component)
    }

    class GifTools(private val component: GifToolsComponent) : NavigationChild {
        @Composable
        override fun Content() = GifToolsContent(component)
    }

    class GradientMaker(private val component: GradientMakerComponent) : NavigationChild {
        @Composable
        override fun Content() = GradientMakerContent(component)
    }

    class ImagePreview(private val component: ImagePreviewComponent) : NavigationChild {
        @Composable
        override fun Content() = ImagePreviewContent(component)
    }

    class ImageSplitting(private val component: ImageSplitterComponent) : NavigationChild {
        @Composable
        override fun Content() = ImageSplitterContent(component)
    }

    class ImageStacking(private val component: ImageStackingComponent) : NavigationChild {
        @Composable
        override fun Content() = ImageStackingContent(component)
    }

    class ImageStitching(private val component: ImageStitchingComponent) : NavigationChild {
        @Composable
        override fun Content() = ImageStitchingContent(component)
    }

    class JxlTools(private val component: JxlToolsComponent) : NavigationChild {
        @Composable
        override fun Content() = JxlToolsContent(component)
    }

    class LimitResize(private val component: LimitsResizeComponent) : NavigationChild {
        @Composable
        override fun Content() = LimitsResizeContent(component)
    }

    class LoadNetImage(private val component: LoadNetImageComponent) : NavigationChild {
        @Composable
        override fun Content() = LoadNetImageContent(component)
    }

    class Main(private val component: MainComponent) : NavigationChild {
        @Composable
        override fun Content() = MainContent(component)
    }

    class NoiseGeneration(private val component: NoiseGenerationComponent) : NavigationChild {
        @Composable
        override fun Content() = NoiseGenerationContent(component)
    }

    class PdfTools(private val component: PdfToolsComponent) : NavigationChild {
        @Composable
        override fun Content() = PdfToolsContent(component)
    }

    class PickColorFromImage(private val component: PickColorFromImageComponent) : NavigationChild {
        @Composable
        override fun Content() = PickColorFromImageContent(component)
    }

    class RecognizeText(private val component: RecognizeTextComponent) : NavigationChild {
        @Composable
        override fun Content() = RecognizeTextContent(component)
    }

    class ResizeAndConvert(private val component: ResizeAndConvertComponent) : NavigationChild {
        @Composable
        override fun Content() = ResizeAndConvertContent(component)
    }

    class ScanQrCode(private val component: ScanQrCodeComponent) : NavigationChild {
        @Composable
        override fun Content() = ScanQrCodeContent(component)
    }

    class Settings(private val component: SettingsComponent) : NavigationChild {
        @Composable
        override fun Content() = SettingsContent(component)
    }

    class SingleEdit(private val component: SingleEditComponent) : NavigationChild {
        @Composable
        override fun Content() = SingleEditContent(component)
    }

    class SvgMaker(private val component: SvgMakerComponent) : NavigationChild {
        @Composable
        override fun Content() = SvgMakerContent(component)
    }

    class Watermarking(private val component: WatermarkingComponent) : NavigationChild {
        @Composable
        override fun Content() = WatermarkingContent(component)
    }

    class WebpTools(private val component: WebpToolsComponent) : NavigationChild {
        @Composable
        override fun Content() = WebpToolsContent(component)
    }

    class WeightResize(private val component: WeightResizeComponent) : NavigationChild {
        @Composable
        override fun Content() = WeightResizeContent(component)
    }

    class Zip(private val component: ZipComponent) : NavigationChild {
        @Composable
        override fun Content() = ZipContent(component)
    }

    class LibrariesInfo(private val component: LibrariesInfoComponent) : NavigationChild {
        @Composable
        override fun Content() = LibrariesInfoContent(component)
    }

    class MarkupLayers(private val component: MarkupLayersComponent) : NavigationChild {
        @Composable
        override fun Content() = MarkupLayersContent(component)
    }

    class Base64Tools(private val component: Base64ToolsComponent) : NavigationChild {
        @Composable
        override fun Content() = Base64ToolsContent(component)
    }

    class ChecksumTools(private val component: ChecksumToolsComponent) : NavigationChild {
        @Composable
        override fun Content() = ChecksumToolsContent(component)
    }

    class MeshGradients(private val component: MeshGradientsComponent) : NavigationChild {
        @Composable
        override fun Content() = MeshGradientsContent(component)
    }

    class EditExif(private val component: EditExifComponent) : NavigationChild {
        @Composable
        override fun Content() = EditExifContent(component)
    }

    class ImageCutter(private val component: ImageCutterComponent) : NavigationChild {
        @Composable
        override fun Content() = ImageCutterContent(component)
    }

    class AudioCoverExtractor(
        private val component: AudioCoverExtractorComponent
    ) : NavigationChild {
        @Composable
        override fun Content() = AudioCoverExtractorContent(component)
    }

    class LibraryDetails(private val component: LibraryDetailsComponent) : NavigationChild {
        @Composable
        override fun Content() = LibraryDetailsContent(component)
    }

    class WallpapersExport(private val component: WallpapersExportComponent) : NavigationChild {
        @Composable
        override fun Content() = WallpapersExportContent(component)
    }

    class AsciiArt(private val component: AsciiArtComponent) : NavigationChild {
        @Composable
        override fun Content() = AsciiArtContent(component)
    }
}