@file:Suppress("functionName")

package dev.thielker.playground.android.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import dev.thielker.playground.android.R

@ExperimentalMaterial3Api
@Composable
fun PlaygroundApp() {

    Scaffold {

        Box(modifier = Modifier.fillMaxSize()) {

            Text(
                text = "Hello, ${stringResource(id = R.string.app_name)}!",
                modifier = Modifier.align(Alignment.Center),
            )
        }
    }
}
