package ir.startup.zabanbaz.composeapp.presentation

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import ir.startup.zabanbaz.composeapp.l10n.AppStrings
import ir.startup.zabanbaz.core.presentation.AppOperationError
import ir.startup.zabanbaz.core.presentation.BaseUiState
import ir.startup.zabanbaz.core.presentation.BaseViewModel
import ir.startup.zabanbaz.core.presentation.ConnectivityRetryRequested

@Composable
fun OperationErrorEffects(
    state: BaseUiState,
    viewModel: BaseViewModel<*>,
    snackbarHostState: SnackbarHostState,
) {
    var lastToastSeq by remember { mutableIntStateOf(0) }
    var showNoInternet by remember { mutableStateOf(false) }
    var lastConnectivitySeq by remember { mutableIntStateOf(0) }

    val operationError = state.operationError

    LaunchedEffect(operationError) {
        if (operationError.shouldShowErrorToast && operationError.emissionSeq > lastToastSeq) {
            lastToastSeq = operationError.emissionSeq
            val message = when {
                operationError.isServerDownError -> "Server is unavailable. Please try again later."
                operationError.isUnknownError -> operationError.message ?: "Something went wrong."
                else -> return@LaunchedEffect
            }
            snackbarHostState.showSnackbar(message)
        }
    }

    LaunchedEffect(operationError.emissionSeq, operationError.kind) {
        if (operationError.isConnectivityError) {
            if (operationError.emissionSeq > lastConnectivitySeq) {
                lastConnectivitySeq = operationError.emissionSeq
                showNoInternet = true
            }
        } else if (showNoInternet) {
            showNoInternet = false
            lastConnectivitySeq = 0
        }
    }

    if (showNoInternet) {
        AlertDialog(
            onDismissRequest = { },
            title = { Text(AppStrings.noInternetTitle) },
            text = { Text(AppStrings.noInternetMessage) },
            confirmButton = {
                TextButton(
                    onClick = { viewModel.onEvent(ConnectivityRetryRequested) },
                ) {
                    Text(AppStrings.retry)
                }
            },
        )
    }
}
