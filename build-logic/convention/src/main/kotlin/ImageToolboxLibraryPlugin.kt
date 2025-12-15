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

import com.android.build.api.dsl.LibraryExtension
import com.evolvarc.imagine.configureDetekt
import com.evolvarc.imagine.configureKotlinAndroid
import com.evolvarc.imagine.implementation
import com.evolvarc.imagine.libs
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

@Suppress("UNUSED")
class ImagineLibraryPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
                apply("kotlin-parcelize")
                apply("kotlinx-serialization")
                apply(libs.detekt.gradle.get().group)
            }

            configureDetekt(extensions.getByType<DetektExtension>())

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)
                defaultConfig.minSdk = libs.versions.androidMinSdk.get().toIntOrNull()
            }

            dependencies {
                implementation(libs.androidxCore)
            }
        }
    }
}