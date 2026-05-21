package ir.startup.zabanbaz.feature.auth.presentation

import ir.startup.zabanbaz.core.errors.ClientErrorException
import ir.startup.zabanbaz.core.presentation.AppOperationError
import ir.startup.zabanbaz.core.presentation.BaseViewModel
import ir.startup.zabanbaz.feature.auth.domain.RequestVerificationCodeUseCase
import ir.startup.zabanbaz.feature.auth.domain.VerifyCodeUseCase
class AuthViewModel(
    private val requestVerificationCodeUseCase: RequestVerificationCodeUseCase,
    private val verifyCodeUseCase: VerifyCodeUseCase,
) : BaseViewModel<AuthUiState>(AuthUiState()) {

    fun onMobileChanged(mobile: String) {
        updateState { copy(mobile = mobile, fieldError = null) }
    }

    fun onCodeChanged(code: String) {
        updateState { copy(code = code, fieldError = null) }
    }

    fun onRequestCode() {
        val mobile = currentState.mobile.trim()
        if (mobile.length < 11) {
            updateState { copy(fieldError = "Enter a valid Iranian mobile (09xxxxxxxxx)") }
            return
        }
        launchSafeCall(
            action = {
                updateState { copy(isLoading = true, fieldError = null) }
                requestVerificationCodeUseCase(mobile)
            },
            onSuccess = {
                updateState {
                    copy(
                        isLoading = false,
                        step = AuthStep.EnterCode,
                        fieldError = null,
                    )
                }
            },
            onError = { error ->
                updateState {
                    copy(
                        isLoading = false,
                        fieldError = clientMessage(error),
                        operationError = AppOperationError.None,
                    )
                }
            },
        )
    }

    fun onVerifyCode() {
        val mobile = currentState.mobile.trim()
        val code = currentState.code.trim()
        if (code.length < 4) {
            updateState { copy(fieldError = "Enter the verification code") }
            return
        }
        launchSafeCall(
            action = {
                updateState { copy(isLoading = true, fieldError = null) }
                verifyCodeUseCase(mobile, code)
            },
            onSuccess = {
                updateState {
                    copy(
                        isLoading = false,
                        status = AuthStatus.Authenticated,
                        fieldError = null,
                    )
                }
            },
            onError = { error ->
                updateState {
                    copy(
                        isLoading = false,
                        fieldError = clientMessage(error),
                        operationError = AppOperationError.None,
                    )
                }
            },
        )
    }

    fun onBackToMobile() {
        updateState {
            copy(
                step = AuthStep.EnterMobile,
                code = "",
                fieldError = null,
            )
        }
    }

    private fun clientMessage(error: Throwable): String =
        when (error) {
            is ClientErrorException -> error.message ?: "Request failed"
            else -> error.message ?: "Something went wrong"
        }
}
