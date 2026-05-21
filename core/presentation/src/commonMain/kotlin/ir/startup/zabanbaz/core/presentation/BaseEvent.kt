package ir.startup.zabanbaz.core.presentation

sealed interface BaseEvent

/** Dispatched by the shell when the user taps Retry on the no-internet dialog. */
data object ConnectivityRetryRequested : BaseEvent
