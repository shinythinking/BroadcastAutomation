package com.solapi.sdk

import com.solapi.sdk.message.service.DefaultMessageService

object NurigoApp {
    @Deprecated(
        message = "This method will be removed in a future version",
        replaceWith = ReplaceWith("SolapiClient.createInstance(apiKey, apiSecretKey)"),
        level = DeprecationLevel.WARNING
    )
    fun initialize(apiKey: String, apiSecretKey: String, apiUrl: String): DefaultMessageService {
        return DefaultMessageService(apiKey, apiSecretKey, apiUrl)
    }
}