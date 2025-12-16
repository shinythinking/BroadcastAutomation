package com.solapi.sdk.message.lib

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import java.time.Instant
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

/**
 * 중앙 집중식 Json 및 직렬화 설정.
 * LocalDateTime 은 ISO_LOCAL_DATE_TIME 문자열로 직렬화/역직렬화합니다.
 * Instant 는 ISO_INSTANT(Z) 문자열로 직렬화/역직렬화합니다.
 */
object JsonSupport {
    private object InstantIsoSerializer : KSerializer<Instant> {
        private val formatter: DateTimeFormatter = DateTimeFormatter.ISO_INSTANT

        override val descriptor: SerialDescriptor =
            PrimitiveSerialDescriptor("Instant", PrimitiveKind.STRING)

        override fun serialize(encoder: Encoder, value: Instant) {
            encoder.encodeString(formatter.format(value))
        }

        override fun deserialize(decoder: Decoder): Instant {
            val text = decoder.decodeString()
            // Lenient parsing: accept ISO_INSTANT, ISO_OFFSET_DATE_TIME, ISO_ZONED_DATE_TIME, ISO_LOCAL_DATE_TIME(UTC)
            return runCatching { Instant.parse(text) }
                .recoverCatching {
                    OffsetDateTime.parse(
                        text,
                        DateTimeFormatter.ISO_OFFSET_DATE_TIME
                    ).toInstant()
                }
                .recoverCatching {
                    ZonedDateTime.parse(text, DateTimeFormatter.ISO_ZONED_DATE_TIME).toInstant()
                }
                .recoverCatching {
                    LocalDateTime.parse(text, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                        .toInstant(ZoneOffset.UTC)
                }
                .getOrElse { throw it }
        }
    }

    private object LocalDateTimeIsoSerializer : KSerializer<LocalDateTime> {
        private val formatter: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

        override val descriptor: SerialDescriptor =
            PrimitiveSerialDescriptor("LocalDateTime", PrimitiveKind.STRING)

        override fun serialize(encoder: Encoder, value: LocalDateTime) {
            encoder.encodeString(value.format(formatter))
        }

        override fun deserialize(decoder: Decoder): LocalDateTime {
            val text = decoder.decodeString()
            // Lenient parsing: accept ISO_LOCAL_DATE_TIME, ISO_OFFSET_DATE_TIME, ISO_ZONED_DATE_TIME, ISO_INSTANT
            return runCatching { LocalDateTime.parse(text, formatter) }
                .recoverCatching {
                    OffsetDateTime.parse(
                        text,
                        DateTimeFormatter.ISO_OFFSET_DATE_TIME
                    ).toLocalDateTime()
                }
                .recoverCatching {
                    ZonedDateTime.parse(text, DateTimeFormatter.ISO_ZONED_DATE_TIME)
                        .toLocalDateTime()
                }
                .recoverCatching { LocalDateTime.ofInstant(Instant.parse(text), ZoneOffset.UTC) }
                .getOrElse { throw it }
        }
    }

    val serializersModule: SerializersModule = SerializersModule {
        contextual(LocalDateTime::class, LocalDateTimeIsoSerializer)
        contextual(Instant::class, InstantIsoSerializer)
    }

    val json: Json = Json {
        coerceInputValues = true
        explicitNulls = false
        encodeDefaults = true
        ignoreUnknownKeys = true
        serializersModule = JsonSupport.serializersModule
    }
}


