package ir.startup.zabanbaz.core.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.startup.zabanbaz.core.errors.ConnectivityException
import ir.startup.zabanbaz.core.errors.ServerException
import ir.startup.zabanbaz.core.errors.UnKnownException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Shared presentation base (parity with live_app [BaseBloc]).
 * Feature ViewModels extend this and run network work through [safeCall].
 */
abstract class BaseViewModel<S : BaseUiState>(
    initialState: S,
) : ViewModel() {
    protected val scope: CoroutineScope get() = viewModelScope
    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<S> = _state.asStateFlow()

    protected val currentState: S get() = _state.value

    protected fun updateState(reducer: S.() -> S) {
        _state.update(reducer)
    }

    protected fun setState(newState: S) {
        _state.value = newState
    }

    private var pendingRetryAction: (suspend () -> Unit)? = null
    private var operationErrorSeq = 0
    private var activeSafeCallEmit: ((S) -> Unit)? = null

    fun onEvent(event: BaseEvent) {
        when (event) {
            ConnectivityRetryRequested -> onConnectivityRetryRequested()
        }
    }

    /** Emit mid-flight updates during an active [safeCall] (e.g. upload progress). */
    protected fun emitFromActiveSafeCall(reducer: S.() -> S) {
        activeSafeCallEmit?.invoke(reducer(currentState))
    }

    private fun onConnectivityRetryRequested() {
        scope.launch {
            val action = pendingRetryAction
            if (action == null) {
                if (currentState.operationError.isConnectivityError) {
                    emitOperationError(AppOperationError.connectivity(++operationErrorSeq))
                }
                return@launch
            }
            pendingRetryAction = null
            action()
        }
    }

    protected suspend fun <T> safeCall(
        action: suspend () -> T,
        onSuccess: suspend (T) -> Unit,
        onError: (suspend (Throwable) -> Unit)? = null,
    ) {
        val previousEmit = activeSafeCallEmit
        activeSafeCallEmit = { setState(it) }
        try {
            try {
                val result = action()
                emitOperationError(AppOperationError.None)
                onSuccess(result)
            } catch (_: ConnectivityException) {
                pendingRetryAction = {
                    safeCall(action = action, onSuccess = onSuccess, onError = onError)
                }
                emitOperationError(AppOperationError.connectivity(++operationErrorSeq))
            } catch (e: ServerException) {
                emitOperationError(
                    AppOperationError.serverDown(++operationErrorSeq, message = e.message),
                )
            } catch (e: UnKnownException) {
                emitOperationError(
                    AppOperationError.unknown(++operationErrorSeq, message = e.message),
                )
            } catch (e: Throwable) {
                onError?.invoke(e)
            }
        } finally {
            activeSafeCallEmit = previousEmit
        }
    }

  protected fun launchSafeCall(
        action: suspend () -> Unit,
        onSuccess: suspend () -> Unit = {},
        onError: (suspend (Throwable) -> Unit)? = null,
    ) {
        scope.launch {
            safeCall(
                action = {
                    action()
                    Unit
                },
                onSuccess = { onSuccess() },
                onError = onError,
            )
        }
    }

    private fun emitOperationError(operationError: AppOperationError) {
        val current = currentState
        if (current is OperationErrorState) {
            @Suppress("UNCHECKED_CAST")
            setState(current.copyWithOperationError(operationError) as S)
        }
    }
}
