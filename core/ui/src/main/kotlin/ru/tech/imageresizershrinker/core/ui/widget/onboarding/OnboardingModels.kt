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

import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Represents a single step in an onboarding flow
 * 
 * @param id Unique identifier for this step
 * @param title Short title for the step
 * @param description Detailed explanation of the feature
 * @param icon Optional icon to represent the feature
 * @param targetTag Optional UI element tag to highlight
 * @param trigger When this step should be shown
 * @param isRequired Whether user must complete this step
 * @param priority Higher priority steps shown first (0 = highest)
 */
data class OnboardingStep(
    val id: String,
    val title: String,
    val description: String,
    val icon: ImageVector? = null,
    val targetTag: String? = null,
    val trigger: OnboardingTrigger = OnboardingTrigger.Manual,
    val isRequired: Boolean = false,
    val priority: Int = 0
)

/**
 * Defines when an onboarding step should be triggered
 */
sealed class OnboardingTrigger {
    /**
     * Show on first app launch
     */
    data object FirstLaunch : OnboardingTrigger()
    
    /**
     * Show when user reaches a specific screen
     */
    data class ScreenReached(val screenRoute: String) : OnboardingTrigger()
    
    /**
     * Show when user performs a specific action
     */
    data class ActionPerformed(val actionId: String) : OnboardingTrigger()
    
    /**
     * Show after a certain time delay (milliseconds)
     */
    data class TimeDelay(val delayMs: Long) : OnboardingTrigger()
    
    /**
     * Show when a feature is first encountered
     */
    data class FeatureDiscovered(val featureId: String) : OnboardingTrigger()
    
    /**
     * Manual trigger (shown programmatically)
     */
    data object Manual : OnboardingTrigger()
}

/**
 * Represents a complete onboarding flow with multiple steps
 * 
 * @param id Unique identifier for this flow
 * @param name Display name for the flow
 * @param steps List of steps in this flow
 * @param canSkip Whether user can skip the entire flow
 * @param showProgress Whether to show progress indicators
 */
data class OnboardingFlow(
    val id: String,
    val name: String,
    val steps: List<OnboardingStep>,
    val canSkip: Boolean = true,
    val showProgress: Boolean = true
) {
    /**
     * Get the next incomplete step
     */
    fun nextIncompleteStep(completedSteps: Set<String>): OnboardingStep? {
        return steps
            .filter { it.id !in completedSteps }
            .minByOrNull { it.priority }
    }
    
    /**
     * Check if this flow is complete
     */
    fun isComplete(completedSteps: Set<String>): Boolean {
        val requiredSteps = steps.filter { it.isRequired }
        return requiredSteps.all { it.id in completedSteps }
    }
    
    /**
     * Get completion progress (0.0 to 1.0)
     */
    fun getProgress(completedSteps: Set<String>): Float {
        if (steps.isEmpty()) return 1f
        val completed = steps.count { it.id in completedSteps }
        return completed.toFloat() / steps.size
    }
}

/**
 * Configuration for a feature discovery card
 * 
 * @param id Unique identifier
 * @param title Feature title
 * @param description Brief explanation
 * @param icon Feature icon
 * @param ctaText Call-to-action button text
 * @param onAction Callback when CTA is clicked
 * @param showOnce Whether to show only once
 */
data class FeatureDiscovery(
    val id: String,
    val title: String,
    val description: String,
    val icon: ImageVector? = null,
    val ctaText: String = "Try it",
    val onAction: (() -> Unit)? = null,
    val showOnce: Boolean = true
)

/**
 * Configuration for a spotlight overlay
 * 
 * @param targetTag Tag of the UI element to highlight
 * @param title Title shown in the spotlight
 * @param description Description shown in the spotlight
 * @param shape Shape of the spotlight
 * @param padding Padding around the highlighted element
 */
data class SpotlightConfig(
    val targetTag: String,
    val title: String,
    val description: String,
    val shape: SpotlightShape = SpotlightShape.Rectangle,
    val padding: Float = 16f
)

/**
 * Shape of the spotlight effect
 */
enum class SpotlightShape {
    /**
     * Circular spotlight (good for FABs, icons)
     */
    Circle,
    
    /**
     * Rounded rectangle spotlight (good for cards, buttons)
     */
    Rectangle,
    
    /**
     * Oval spotlight
     */
    Oval
}

/**
 * Configuration for a contextual tooltip
 * 
 * @param id Unique identifier
 * @param message Tooltip message
 * @param position Preferred position relative to anchor
 * @param autoDismissDelay Auto-dismiss after milliseconds (null = manual dismiss)
 * @param showArrow Whether to show pointing arrow
 */
data class TooltipConfig(
    val id: String,
    val message: String,
    val position: TooltipPosition = TooltipPosition.Auto,
    val autoDismissDelay: Long? = 3000,
    val showArrow: Boolean = true
)

/**
 * Position of tooltip relative to anchor element
 */
enum class TooltipPosition {
    /**
     * Automatically determine best position
     */
    Auto,
    
    /**
     * Above the anchor
     */
    Top,
    
    /**
     * Below the anchor
     */
    Bottom,
    
    /**
     * Left of the anchor
     */
    Start,
    
    /**
     * Right of the anchor
     */
    End
}

/**
 * State of an onboarding component
 */
sealed class OnboardingComponentState {
    /**
     * Component is not shown
     */
    data object Hidden : OnboardingComponentState()
    
    /**
     * Component is animating in
     */
    data object Appearing : OnboardingComponentState()
    
    /**
     * Component is fully visible
     */
    data object Visible : OnboardingComponentState()
    
    /**
     * Component is animating out
     */
    data object Disappearing : OnboardingComponentState()
}

/**
 * Event fired when user interacts with onboarding
 */
sealed class OnboardingEvent {
    /**
     * User completed a step
     */
    data class StepCompleted(val stepId: String) : OnboardingEvent()
    
    /**
     * User skipped a step
     */
    data class StepSkipped(val stepId: String) : OnboardingEvent()
    
    /**
     * User dismissed onboarding
     */
    data class FlowDismissed(val flowId: String) : OnboardingEvent()
    
    /**
     * User completed entire flow
     */
    data class FlowCompleted(val flowId: String) : OnboardingEvent()
    
    /**
     * User requested help/tutorial
     */
    data object HelpRequested : OnboardingEvent()
}
