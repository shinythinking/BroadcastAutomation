package com.solapi.sdk.message.lib

import com.solapi.sdk.message.exception.SolapiApiKeyException
import org.apache.commons.codec.binary.Hex
import java.nio.charset.StandardCharsets
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.UUID
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

internal class Authenticator(
    private val apiKey: String,
    private val apiSecretKey: String
) {

    companion object {
        private const val ENCRYPTION_ALGORITHM = "HmacSHA256"
    }

    @Throws
    fun generateAuthInfo(): String {
        if (apiKey == "" || apiSecretKey == "") {
            throw SolapiApiKeyException("유효한 API Key or API Secret Key를 입력하셔야 합니다.")
        }

        val salt = UUID.randomUUID().toString().replace(Regex("-"), "")
        val date = ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toString().split(Regex("\\["))
            .toTypedArray()[0]

        val encryptionInstance = Mac.getInstance(ENCRYPTION_ALGORITHM)
        val secretKey =
            SecretKeySpec(apiSecretKey.toByteArray(StandardCharsets.UTF_8), ENCRYPTION_ALGORITHM)

        encryptionInstance.init(secretKey)
        val signature = String(
            (date + salt).toByteArray(StandardCharsets.UTF_8)
                .let { encryptionInstance.doFinal(it) }
                .let { Hex.encodeHex(it) }
        )
        return "HMAC-SHA256 Apikey=${apiKey}, Date=${date}, salt=${salt}, signature=${signature}"
    }
}