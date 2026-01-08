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

package com.evolvarc.imagine.core.settings.presentation.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Build
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Security
import androidx.compose.ui.graphics.vector.ImageVector
import com.evolvarc.imagine.core.resources.BuildConfig
import com.evolvarc.imagine.core.resources.R
import com.evolvarc.imagine.core.resources.icons.DesignServices
import com.evolvarc.imagine.core.resources.icons.FolderOpened

/**
 * Consolidated settings organization - Groups 26 settings into 5 intuitive categories
 * for better discoverability and reduced cognitive load
 */
sealed class SettingsCategory(
    val id: Int,
    val titleId: Int,
    val subtitleId: Int,
    val icon: ImageVector,
    val groups: List<SettingsGroup>,
    val initialState: Boolean = true
) {
    /**
     * APPEARANCE - All visual customization
     * Includes: Theme, Colors, Fonts, Layout, Shadows, Emojis, Confetti
     */
    data object Appearance : SettingsCategory(
        id = 0,
        icon = Icons.Rounded.DesignServices,
        titleId = R.string.appearance,
        subtitleId = R.string.appearance_subtitle,
        groups = listOf(
            SettingsGroup.PrimaryCustomization,
            SettingsGroup.SecondaryCustomization,
            SettingsGroup.NightMode,
            SettingsGroup.Layout,
            SettingsGroup.Font,
            SettingsGroup.Emoji,
            SettingsGroup.Confetti,
            SettingsGroup.Shadows
        )
    )

    /**
     * TOOLS - Feature-specific settings
     * Includes: Draw, EXIF, Presets, Default Values, Tools Arrangement
     */
    data object Tools : SettingsCategory(
        id = 1,
        icon = Icons.Rounded.Build,
        titleId = R.string.tools,
        subtitleId = R.string.tools_subtitle,
        groups = listOf(
            SettingsGroup.ToolsArrangement,
            SettingsGroup.Presets,
            SettingsGroup.DefaultValues,
            SettingsGroup.Draw,
            SettingsGroup.Exif
        )
    )

    /**
     * FILES - Everything file-related
     * Includes: Folders, Filename, Image Source, Clipboard
     */
    data object Files : SettingsCategory(
        id = 2,
        icon = Icons.Rounded.FolderOpened,
        titleId = R.string.files,
        subtitleId = R.string.files_subtitle,
        groups = listOf(
            SettingsGroup.Folder,
            SettingsGroup.Filename,
            SettingsGroup.ImageSource,
            SettingsGroup.Clipboard
        )
    )

    /**
     * PRIVACY & PERFORMANCE - Security, cache, behavior
     * Includes: Cache, Secure Mode, Screen, Haptics, Behavior
     */
    data object PrivacyAndPerformance : SettingsCategory(
        id = 3,
        icon = Icons.Rounded.Security,
        titleId = R.string.privacy_and_performance,
        subtitleId = R.string.privacy_and_performance_subtitle,
        groups = listOf(
            SettingsGroup.Screen,
            SettingsGroup.Haptics,
            SettingsGroup.Cache,
            SettingsGroup.Behavior
        )
    )

    /**
     * ABOUT & SUPPORT - App info, updates, backup
     * Includes: Contact, Updates, Firebase, Backup/Restore, About
     */
    data object AboutAndSupport : SettingsCategory(
        id = 4,
        icon = Icons.Rounded.Info,
        titleId = R.string.about_and_support,
        subtitleId = R.string.about_and_support_subtitle,
        groups = listOf(
            SettingsGroup.ContactMe,
            SettingsGroup.AboutApp,
            SettingsGroup.Updates,
            SettingsGroup.BackupRestore,
            SettingsGroup.Firebase
        ),
        initialState = true
    )

    companion object {
        val entries: List<SettingsCategory> by lazy {
            listOf(
                Appearance,
                Tools,
                Files,
                PrivacyAndPerformance,
                AboutAndSupport
            ).map { category ->
                if (category is AboutAndSupport) {
                    AboutAndSupport.copy(
                        groups = category.groups.filter { group ->
                            !(group is SettingsGroup.Firebase && BuildConfig.FLAVOR == "foss")
                        }
                    )
                } else category
            }
        }
        
        /**
         * Get all settings in flat list for search functionality
         */
        val allSettings: List<Pair<SettingsGroup, Setting>> by lazy {
            entries.flatMap { category ->
                category.groups.flatMap { group ->
                    group.settingsList.map { setting ->
                        group to setting
                    }
                }
            }
        }
    }
}

// Helper function to copy data objects
private fun SettingsCategory.AboutAndSupport.copy(
    groups: List<SettingsGroup>
): SettingsCategory.AboutAndSupport = SettingsCategory.AboutAndSupport
