package com.swrlz.keyboard.clientbridge

sealed interface ClientBridgeState {
    data object Unavailable : ClientBridgeState
    data object NotEnrolled : ClientBridgeState
    data object Ready : ClientBridgeState
}

interface ClientBridge {
    suspend fun state(): ClientBridgeState
    suspend fun cancel(operationId: String)
}
