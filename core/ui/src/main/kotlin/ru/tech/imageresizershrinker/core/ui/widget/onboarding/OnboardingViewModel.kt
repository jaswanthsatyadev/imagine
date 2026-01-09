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

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for managing onboarding state with DataStore persistence
 */
@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val repository: OnboardingRepository
) : ViewModel() {

    /**
     * Is this the first launch of the app
     */
    val isFirstLaunch: StateFlow<Boolean> = repository.isFirstLaunch
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = true
        )

    /**
     * Set of completed onboarding step IDs
     */
    val completedSteps: StateFlow<Set<String>> = repository.completedSteps
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptySet()
        )

    /**
     * Set of discovered feature IDs
     */
    val discoveredFeatures: StateFlow<Set<String>> = repository.discoveredFeatures
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptySet()
        )

    /**
     * Set of dismissed tooltip IDs
     */
    val dismissedTooltips: StateFlow<Set<String>> = repository.dismissedTooltips
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptySet()
        )

    /**
     * Set of completed flow IDs
     */
    val completedFlows: StateFlow<Set<String>> = repository.completedFlows
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptySet()
        )

    private val _onboardingEvents = MutableStateFlow<String?>(null)
    val onboardingEvents = _onboardingEvents.asStateFlow()

    /**
     * Mark first launch as complete
     */
    fun completeFirstLaunch() {
        viewModelScope.launch {
            repository.completeFirstLaunch()
            _onboardingEvents.value = "FirstLaunchCompleted"
        }
    }

    /**
     * Complete an onboarding step
     */
    fun completeStep(stepId: String) {
        viewModelScope.launch {
            repository.completeStep(stepId)
            _onboardingEvents.value = "StepCompleted:$stepId"
        }
    }

    /**
     * Complete multiple onboarding steps
     */
    fun completeSteps(stepIds: List<String>) {
        viewModelScope.launch {
            repository.completeSteps(stepIds)
            _onboardingEvents.value = "MultipleStepsCompleted:${stepIds.size}"
        }
    }

    /**
     * Check if a step is completed
     */
    fun isStepCompleted(stepId: String): Boolean {
        return stepId in completedSteps.value
    }

    /**
     * Discover a feature
     */
    fun discoverFeature(featureId: String) {
        viewModelScope.launch {
            repository.discoverFeature(featureId)
            _onboardingEvents.value = "FeatureDiscovered:$featureId"
        }
    }

    /**
     * Check if a feature is discovered
     */
    fun isFeatureDiscovered(featureId: String): Boolean {
        return featureId in discoveredFeatures.value
    }

    /**
     * Dismiss a tooltip
     */
    fun dismissTooltip(tooltipId: String) {
        viewModelScope.launch {
            repository.dismissTooltip(tooltipId)
            _onboardingEvents.value = "TooltipDismissed:$tooltipId"
        }
    }

    /**
     * Check if a tooltip is dismissed
     */
    fun isTooltipDismissed(tooltipId: String): Boolean {
        return tooltipId in dismissedTooltips.value
    }

    /**
     * Complete an onboarding flow
     */
    fun completeFlow(flowId: String) {
        viewModelScope.launch {
            repository.completeFlow(flowId)
            _onboardingEvents.value = "FlowCompleted:$flowId"
        }
    }

    /**
     * Check if a flow is completed
     */
    fun isFlowCompleted(flowId: String): Boolean {
        return flowId in completedFlows.value
    }

    /**
     * Check if a flow should be shown
     */
    fun shouldShowFlow(flowId: String): Boolean {
        return !isFlowCompleted(flowId)
    }

    /**
     * Reset all onboarding data
     */
    fun resetAll() {
        viewModelScope.launch {
            repository.resetAll()
            _onboardingEvents.value = "Reset"
        }
    }

    /**
     * Reset a specific flow
     */
    fun resetFlow(flowId: String) {
        viewModelScope.launch {
            repository.resetFlow(flowId)
        }
    }

    /**
     * Get onboarding statistics
     */
    fun getStatistics(): OnboardingStatistics {
        return OnboardingStatistics(
            completedStepsCount = completedSteps.value.size,
            discoveredFeaturesCount = discoveredFeatures.value.size,
            completedFlowsCount = completedFlows.value.size,
            isFirstLaunch = isFirstLaunch.value
        )
    }

    /**
     * Clear the current event
     */
    fun clearEvent() {
        _onboardingEvents.value = null
    }
}
