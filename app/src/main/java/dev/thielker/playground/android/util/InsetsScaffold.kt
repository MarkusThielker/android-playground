package dev.thielker.playground.android.util

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

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
    content: @Composable (PaddingValues) -> Unit,
) {

    Scaffold(
        modifier = modifier,
        topBar = {
            Column {
                StatusBarInset(statusBarColor)
                topBar()
            }
        },
        bottomBar = {
            Column {
                bottomBar()
                NavigationBarInset(navigationBarColor)
            }
        },
        snackbarHost = snackbarHost,
        floatingActionButton = floatingActionButton,
        floatingActionButtonPosition = floatingActionButtonPosition,
        contentColor = contentColor,
        content = content
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
