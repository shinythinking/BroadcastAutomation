package com.solapi.sdk.message.model

interface CommonMessageProperty {
    /**
     * 수신번호
     */
    var to: String?

    /**
     * 발신번호
     */
    var from: String?
}