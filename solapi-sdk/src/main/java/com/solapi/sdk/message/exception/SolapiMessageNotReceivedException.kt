package com.solapi.sdk.message.exception

import com.solapi.sdk.message.model.FailedMessage

class SolapiMessageNotReceivedException(message: String) : SolapiException,
    Exception(message) {
    var failedMessageList: List<FailedMessage> = emptyList()

}