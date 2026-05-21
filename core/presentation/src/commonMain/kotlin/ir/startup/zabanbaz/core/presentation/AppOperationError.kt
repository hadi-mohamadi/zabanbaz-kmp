package ir.startup.zabanbaz.core.presentation

enum class AppOperationErrorKind {
    None,
    Connectivity,
    ServerDown,
    Unknown,
}

data class AppOperationError(
    val kind: AppOperationErrorKind = AppOperationErrorKind.None,
    val emissionSeq: Int = 0,
    val message: String? = null,
) {
    val hasError: Boolean get() = kind != AppOperationErrorKind.None
    val isConnectivityError: Boolean get() = kind == AppOperationErrorKind.Connectivity
    val isServerDownError: Boolean get() = kind == AppOperationErrorKind.ServerDown
    val isUnknownError: Boolean get() = kind == AppOperationErrorKind.Unknown

    /** Shown as toast/snackbar by the shell (not connectivity — that uses NoInternet UI). */
    val shouldShowErrorToast: Boolean get() = isServerDownError || isUnknownError

    companion object {
        val None = AppOperationError()

        fun connectivity(emissionSeq: Int) = AppOperationError(
            kind = AppOperationErrorKind.Connectivity,
            emissionSeq = emissionSeq,
        )

        fun serverDown(emissionSeq: Int, message: String? = null) = AppOperationError(
            kind = AppOperationErrorKind.ServerDown,
            emissionSeq = emissionSeq,
            message = message,
        )

        fun unknown(emissionSeq: Int, message: String? = null) = AppOperationError(
            kind = AppOperationErrorKind.Unknown,
            emissionSeq = emissionSeq,
            message = message,
        )
    }
}
