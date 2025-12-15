package com.evolvarc.imagine.library_details.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.evolvarc.imagine.core.ui.widget.enhanced.EnhancedIconButton
import com.evolvarc.imagine.core.ui.widget.enhanced.EnhancedTopAppBar
import com.evolvarc.imagine.core.ui.widget.enhanced.EnhancedTopAppBarType
import com.evolvarc.imagine.core.ui.widget.modifier.container
import com.evolvarc.imagine.core.ui.widget.other.TopAppBarEmoji
import com.evolvarc.imagine.core.ui.widget.text.HtmlText
import com.evolvarc.imagine.core.ui.widget.text.marquee
import com.evolvarc.imagine.library_details.presentation.screenLogic.LibraryDetailsComponent

@Composable
fun LibraryDetailsContent(
    component: LibraryDetailsComponent
) {
    val childScrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Surface(
        color = MaterialTheme.colorScheme.surface,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(childScrollBehavior.nestedScrollConnection)
        ) {
            EnhancedTopAppBar(
                title = {
                    Text(
                        text = component.libraryName,
                        modifier = Modifier.marquee()
                    )
                },
                navigationIcon = {
                    EnhancedIconButton(
                        onClick = component.onGoBack
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                actions = {
                    TopAppBarEmoji()
                },
                type = EnhancedTopAppBarType.Large,
                scrollBehavior = childScrollBehavior
            )
            SelectionContainer {
                HtmlText(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .navigationBarsPadding()
                        .padding(
                            WindowInsets.displayCutout
                                .only(
                                    WindowInsetsSides.Horizontal
                                )
                                .asPaddingValues()
                        )
                        .padding(12.dp)
                        .container(
                            resultPadding = 12.dp
                        ),
                    html = component.libraryDescription
                )
            }
        }
    }
}