package com.solapi.sdk.message.dto.request

import com.solapi.sdk.Version
import kotlinx.serialization.Serializable

@Serializable
data class DefaultAgent(
    val sdkVersion: String = "kotlin/${Version.SDK_VERSION}",
    val osPlatform: String = "${System.getProperty("os.name")} | ${System.getProperty("java.version")} | ${
        System.getProperty(
            "os.arch"
        )
    }"
)
