package com.solapi.sdk

import com.solapi.sdk.message.service.DefaultMessageService

object SolapiClient {

    @JvmOverloads
    fun createInstance(
        apiKey: String,
        apiSecretKey: String,
        useStaticIP: Boolean = false
    ): DefaultMessageService {
        var apiUrl = "https://api.solapi.com"
        if (useStaticIP) {
            apiUrl = "https://api-static.solapi.com"
        }
        return DefaultMessageService(apiKey, apiSecretKey, apiUrl)
    }

}