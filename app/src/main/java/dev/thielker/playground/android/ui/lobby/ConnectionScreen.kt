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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.thielker.playground.android.R
import kotlinx.coroutines.delay

@ExperimentalMaterial3Api
@Composable
fun ConnectionScreen(viewModel: ConnectionViewModel = viewModel()) {

    var lobbyInputVisible by remember { mutableStateOf(false) }

    LazyColumn(
        contentPadding = PaddingValues(16.dp)
    ) {

        item {

            Button(
                onClick = { lobbyInputVisible = true },
                content = { Text(text = stringResource(id = R.string.connection_create)) },
                modifier = Modifier.fillMaxWidth(),
            )
        }

        item {

            if (viewModel.services.isNotEmpty()) {

                Text(
                    text = stringResource(id = R.string.connection_available_lobbies),
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.padding(top = 16.dp),
                )
            }
        }

        items(viewModel.services) {

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

                Text(text = stringResource(id = R.string.connection_searching_lobbies))
                LinearProgressIndicator(modifier = Modifier.padding(16.dp))
            }
        }
    }

    if (lobbyInputVisible) {

        val inputValid = viewModel.mServiceName.isNotBlank() &&
                viewModel.mServiceName.length < viewModel.maxServiceNameLength

        AlertDialog(
            onDismissRequest = { lobbyInputVisible = false },
            title = { Text(text = stringResource(id = R.string.connection_create)) },
            text = {

                val requester = remember { FocusRequester() }

                val direction = LocalLayoutDirection.current
                val selection = if (direction == LayoutDirection.Ltr)
                    TextRange(viewModel.mServiceName.length) else TextRange.Zero

                Column {

                    Text(text = stringResource(id = R.string.connection_dialog_text))

                    Spacer(modifier = Modifier.height(16.dp))

                    TextField(
                        value = TextFieldValue(
                            text = viewModel.mServiceName,
                            selection = selection
                        ),
                        onValueChange = { viewModel.mServiceName = it.text },
                        label = { Text(text = stringResource(id = R.string.connection_dialog_label_lobby)) },
                        modifier = Modifier.focusRequester(requester),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                if (inputValid) {
                                    viewModel.registerService()
                                    lobbyInputVisible = false
                                }
                            }
                        ),
                    )
                }

                LaunchedEffect(Unit) {
                    delay(10) // required to make keyboard show up
                    requester.requestFocus()
                }
            },
            dismissButton = {
                Button(
                    onClick = { lobbyInputVisible = false },
                    content = { Text(text = stringResource(id = R.string.string_cancel)) }
                )
            },
            confirmButton = {
                Button(
                    onClick = { viewModel.registerService(); lobbyInputVisible = false },
                    enabled = inputValid,
                    content = { Text(text = stringResource(id = R.string.connection_create)) }
                )
            }
        )
    }
}
