@file:Suppress("functionName")

package dev.thielker.playground.android.util

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

/**
 * This Composable extends the material3 Scaffold, by adding topBar and bottomBar height
 * as padding. This limits the content area to the space between the two bars.
 *
 * This is especially useful if the activity's DecorFitsSystemWindows flag was set to false
 * to make the navigation drawer overlap the systems status- and navigation-bar.
 *
 * Requires: androidx.compose.foundation:foundation:1.2.0 (alpha) and above
 *
 * @author Markus Thielker
 *
 * */
@ExperimentalMaterial3Api
@Composable
fun InsetsScaffold(
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    snackbarHost: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    floatingActionButtonPosition: FabPosition = FabPosition.End,
    statusBarColor: Color = MaterialTheme.colorScheme.surface,
    navigationBarColor: Color = MaterialTheme.colorScheme.surface,
    backgroundColor: Color = MaterialTheme.colorScheme.background,
    contentColor: Color = contentColorFor(backgroundColor),
    content: @Composable BoxScope.() -> Unit,
) {

    var topBarHeight by remember { mutableStateOf(0) }
    var bottomBarHeight by remember { mutableStateOf(0) }

    Scaffold(
        modifier = modifier,
        topBar = {
            Column(
                modifier = Modifier.onGloballyPositioned { topBarHeight = it.size.height }
            ) {
                // places the topBar below the status bar
                StatusBarInset(statusBarColor)
                topBar()
            }
        },
        bottomBar = {
            Column(
                modifier = Modifier.onGloballyPositioned { bottomBarHeight = it.size.height }
            ) {
                // places the bottomBar above the navigation bar
                bottomBar()
                NavigationBarInset(navigationBarColor)
            }
        },
        snackbarHost = snackbarHost,
        floatingActionButton = floatingActionButton,
        floatingActionButtonPosition = floatingActionButtonPosition,
        contentColor = contentColor,
        content = {
            // limit content area between top bar and bottom bar
            // necessary because DecorFitsSystemWindows is set to false
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = with(LocalDensity.current) { topBarHeight.toDp() },
                        bottom = with(LocalDensity.current) { bottomBarHeight.toDp() },
                    ),
                content = content,
            )
        }
    )
}

@Composable
private fun StatusBarInset(color: Color) {

    val insets = WindowInsets.statusBars.union(WindowInsets(top = 10.dp))

    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .windowInsetsPadding(insets)
            .background(color)
    )
}

@Composable
private fun NavigationBarInset(color: Color) {

    val insets = WindowInsets.navigationBars.union(WindowInsets(top = 10.dp))

    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .windowInsetsPadding(insets)
            .background(color)
    )
}
