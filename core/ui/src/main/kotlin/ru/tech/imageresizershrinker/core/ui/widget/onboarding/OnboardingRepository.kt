/*
 * ImageToolbox is an image editor for android
 * Copyright (c) 2024 T8RIN (Malik Mukhametzyanov)
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

package ru.tech.imageresizershrinker.core.ui.widget.onboarding

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository for managing onboarding state persistence using DataStore
 */
@Singleton
class OnboardingRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
        name = "onboarding_preferences"
    )

    companion object {
        private val IS_FIRST_LAUNCH = booleanPreferencesKey("is_first_launch")
        private val COMPLETED_STEPS = stringPreferencesKey("completed_steps")
        private val DISCOVERED_FEATURES = stringPreferencesKey("discovered_features")
        private val DISMISSED_TOOLTIPS = stringPreferencesKey("dismissed_tooltips")
        private val COMPLETED_FLOWS = stringPreferencesKey("completed_flows")
    }

    /**
     * Observe if this is the first launch
     */
    val isFirstLaunch: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[IS_FIRST_LAUNCH] ?: true
        }

    /**
     * Observe completed onboarding steps
     */
    val completedSteps: Flow<Set<String>> = context.dataStore.data
        .map { preferences ->
            preferences[COMPLETED_STEPS]
                ?.split(",")
                ?.filter { it.isNotBlank() }
                ?.toSet()
                ?: emptySet()
        }

    /**
     * Observe discovered features
     */
    val discoveredFeatures: Flow<Set<String>> = context.dataStore.data
        .map { preferences ->
            preferences[DISCOVERED_FEATURES]
                ?.split(",")
                ?.filter { it.isNotBlank() }
                ?.toSet()
                ?: emptySet()
        }

    /**
     * Observe dismissed tooltips
     */
    val dismissedTooltips: Flow<Set<String>> = context.dataStore.data
        .map { preferences ->
            preferences[DISMISSED_TOOLTIPS]
                ?.split(",")
                ?.filter { it.isNotBlank() }
                ?.toSet()
                ?: emptySet()
        }

    /**
     * Observe completed flows
     */
    val completedFlows: Flow<Set<String>> = context.dataStore.data
        .map { preferences ->
            preferences[COMPLETED_FLOWS]
                ?.split(",")
                ?.filter { it.isNotBlank() }
                ?.toSet()
                ?: emptySet()
        }

    /**
     * Mark first launch as complete
     */
    suspend fun completeFirstLaunch() {
        context.dataStore.edit { preferences ->
            preferences[IS_FIRST_LAUNCH] = false
        }
    }

    /**
     * Complete an onboarding step
     */
    suspend fun completeStep(stepId: String) {
        context.dataStore.edit { preferences ->
            val currentSteps = preferences[COMPLETED_STEPS]
                ?.split(",")
                ?.filter { it.isNotBlank() }
                ?.toMutableSet()
                ?: mutableSetOf()

            currentSteps.add(stepId)
            preferences[COMPLETED_STEPS] = currentSteps.joinToString(",")
        }
    }

    /**
     * Complete multiple onboarding steps
     */
    suspend fun completeSteps(stepIds: List<String>) {
        context.dataStore.edit { preferences ->
            val currentSteps = preferences[COMPLETED_STEPS]
                ?.split(",")
                ?.filter { it.isNotBlank() }
                ?.toMutableSet()
                ?: mutableSetOf()

            currentSteps.addAll(stepIds)
            preferences[COMPLETED_STEPS] = currentSteps.joinToString(",")
        }
    }

    /**
     * Discover a feature
     */
    suspend fun discoverFeature(featureId: String) {
        context.dataStore.edit { preferences ->
            val currentFeatures = preferences[DISCOVERED_FEATURES]
                ?.split(",")
                ?.filter { it.isNotBlank() }
                ?.toMutableSet()
                ?: mutableSetOf()

            currentFeatures.add(featureId)
            preferences[DISCOVERED_FEATURES] = currentFeatures.joinToString(",")
        }
    }

    /**
     * Dismiss a tooltip
     */
    suspend fun dismissTooltip(tooltipId: String) {
        context.dataStore.edit { preferences ->
            val currentTooltips = preferences[DISMISSED_TOOLTIPS]
                ?.split(",")
                ?.filter { it.isNotBlank() }
                ?.toMutableSet()
                ?: mutableSetOf()

            currentTooltips.add(tooltipId)
            preferences[DISMISSED_TOOLTIPS] = currentTooltips.joinToString(",")
        }
    }

    /**
     * Complete a flow
     */
    suspend fun completeFlow(flowId: String) {
        context.dataStore.edit { preferences ->
            val currentFlows = preferences[COMPLETED_FLOWS]
                ?.split(",")
                ?.filter { it.isNotBlank() }
                ?.toMutableSet()
                ?: mutableSetOf()

            currentFlows.add(flowId)
            preferences[COMPLETED_FLOWS] = currentFlows.joinToString(",")
        }
    }

    /**
     * Reset all onboarding data
     */
    suspend fun resetAll() {
        context.dataStore.edit { preferences ->
            preferences.clear()
            preferences[IS_FIRST_LAUNCH] = true
        }
    }

    /**
     * Reset a specific flow
     */
    suspend fun resetFlow(flowId: String) {
        context.dataStore.edit { preferences ->
            val currentFlows = preferences[COMPLETED_FLOWS]
                ?.split(",")
                ?.filter { it.isNotBlank() }
                ?.toMutableSet()
                ?: mutableSetOf()

            currentFlows.remove(flowId)
            preferences[COMPLETED_FLOWS] = currentFlows.joinToString(",")
        }
    }
}
