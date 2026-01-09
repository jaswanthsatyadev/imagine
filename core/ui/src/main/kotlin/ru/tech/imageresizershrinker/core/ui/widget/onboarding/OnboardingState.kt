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

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * State holder for onboarding system
 * 
 * Tracks which steps have been completed, which features have been discovered,
 * and provides methods to query onboarding status.
 * 
 * In a real implementation, this would persist to SharedPreferences or DataStore.
 * This in-memory version serves as a foundation that can be enhanced.
 */
class OnboardingState {
    private val _completedSteps = MutableStateFlow<Set<String>>(emptySet())
    val completedSteps: StateFlow<Set<String>> = _completedSteps.asStateFlow()
    
    private val _discoveredFeatures = MutableStateFlow<Set<String>>(emptySet())
    val discoveredFeatures: StateFlow<Set<String>> = _discoveredFeatures.asStateFlow()
    
    private val _dismissedTooltips = MutableStateFlow<Set<String>>(emptySet())
    val dismissedTooltips: StateFlow<Set<String>> = _dismissedTooltips.asStateFlow()
    
    private val _completedFlows = MutableStateFlow<Set<String>>(emptySet())
    val completedFlows: StateFlow<Set<String>> = _completedFlows.asStateFlow()
    
    private val _isFirstLaunch = MutableStateFlow(true)
    val isFirstLaunch: StateFlow<Boolean> = _isFirstLaunch.asStateFlow()
    
    /**
     * Mark a step as completed
     */
    fun completeStep(stepId: String) {
        _completedSteps.value = _completedSteps.value + stepId
    }
    
    /**
     * Mark multiple steps as completed
     */
    fun completeSteps(stepIds: List<String>) {
        _completedSteps.value = _completedSteps.value + stepIds.toSet()
    }
    
    /**
     * Check if a step is completed
     */
    fun isStepCompleted(stepId: String): Boolean {
        return stepId in _completedSteps.value
    }
    
    /**
     * Mark a feature as discovered
     */
    fun discoverFeature(featureId: String) {
        _discoveredFeatures.value = _discoveredFeatures.value + featureId
    }
    
    /**
     * Check if a feature has been discovered
     */
    fun isFeatureDiscovered(featureId: String): Boolean {
        return featureId in _discoveredFeatures.value
    }
    
    /**
     * Mark a tooltip as dismissed
     */
    fun dismissTooltip(tooltipId: String) {
        _dismissedTooltips.value = _dismissedTooltips.value + tooltipId
    }
    
    /**
     * Check if a tooltip has been dismissed
     */
    fun isTooltipDismissed(tooltipId: String): Boolean {
        return tooltipId in _dismissedTooltips.value
    }
    
    /**
     * Mark a flow as completed
     */
    fun completeFlow(flowId: String) {
        _completedFlows.value = _completedFlows.value + flowId
    }
    
    /**
     * Check if a flow is completed
     */
    fun isFlowCompleted(flowId: String): Boolean {
        return flowId in _completedFlows.value
    }
    
    /**
     * Check if a flow should be shown
     */
    fun shouldShowFlow(flow: OnboardingFlow): Boolean {
        // Don't show if already completed
        if (isFlowCompleted(flow.id)) return false
        
        // Don't show if all required steps are complete
        if (flow.isComplete(_completedSteps.value)) return false
        
        return true
    }
    
    /**
     * Mark first launch as completed
     */
    fun completeFirstLaunch() {
        _isFirstLaunch.value = false
    }
    
    /**
     * Reset all onboarding state (for testing or settings)
     */
    fun resetAll() {
        _completedSteps.value = emptySet()
        _discoveredFeatures.value = emptySet()
        _dismissedTooltips.value = emptySet()
        _completedFlows.value = emptySet()
        _isFirstLaunch.value = true
    }
    
    /**
     * Reset a specific flow (replay tutorial)
     */
    fun resetFlow(flowId: String) {
        _completedFlows.value = _completedFlows.value - flowId
    }
    
    /**
     * Get completion statistics
     */
    fun getStatistics(): OnboardingStatistics {
        return OnboardingStatistics(
            completedStepsCount = _completedSteps.value.size,
            discoveredFeaturesCount = _discoveredFeatures.value.size,
            completedFlowsCount = _completedFlows.value.size,
            isFirstLaunch = _isFirstLaunch.value
        )
    }
}

/**
 * Statistics about onboarding progress
 */
data class OnboardingStatistics(
    val completedStepsCount: Int,
    val discoveredFeaturesCount: Int,
    val completedFlowsCount: Int,
    val isFirstLaunch: Boolean
)

/**
 * Remember an OnboardingState instance
 * 
 * In a real app, this would be provided via DI (Hilt/Koin)
 * and backed by persistent storage (DataStore/SharedPreferences)
 */
@Composable
fun rememberOnboardingState(): OnboardingState {
    return remember { OnboardingState() }
}
