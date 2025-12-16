package com.solapi.sdk.message.dto.request

import com.solapi.sdk.message.model.StorageType
import kotlinx.serialization.Serializable

@Serializable
data class FileUploadRequest(
    var file: String? = null,
    var type: StorageType? = null,
    var name: String? = null,
    var link: String? = null,
)
