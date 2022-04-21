@file:Suppress("functionName")

package dev.thielker.playground.android.ui.lobby

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.thielker.playground.android.R

@ExperimentalMaterial3Api
@Composable
fun LobbyScreen(viewModel: ConnectionViewModel = viewModel()) {

    var confirmationDialogVisible by remember { mutableStateOf(false) }

    LazyColumn(
        contentPadding = PaddingValues(16.dp)
    ) {

        item {

            Button(
                onClick = { confirmationDialogVisible = true },
                content = { Text(text = stringResource(id = R.string.lobby_close)) },
                modifier = Modifier.fillMaxWidth(),
            )
        }

        item {

            if (viewModel.services.isNotEmpty()) {

                Text(
                    text = stringResource(id = R.string.lobby_joined_clients),
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.padding(top = 16.dp),
                )
            }
        }

        items(viewModel.clients) {

            Card(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .fillMaxWidth(),
            ) {

                Text(
                    text = it,
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.headlineSmall,
                )
            }
        }

        item {

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                Spacer(modifier = Modifier.height(48.dp))

                Text(text = stringResource(id = R.string.lobby_waiting_clients))
                LinearProgressIndicator(modifier = Modifier.padding(16.dp))
            }
        }
    }

    if (confirmationDialogVisible) {

        AlertDialog(
            onDismissRequest = { confirmationDialogVisible = false },
            title = { Text(text = stringResource(id = R.string.lobby_close)) },
            text = {
                Text(text = stringResource(id = R.string.lobby_dialog_text))
            },
            dismissButton = {
                Button(
                    onClick = { confirmationDialogVisible = false },
                    content = { Text(text = stringResource(id = R.string.string_cancel)) }
                )
            },
            confirmButton = {
                Button(
                    onClick = { viewModel.unregisterService(); confirmationDialogVisible = false },
                    content = { Text(text = stringResource(id = R.string.lobby_close)) }
                )
            }
        )
    }
}
