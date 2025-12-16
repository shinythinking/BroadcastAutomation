package com.solapi.sdk.message.model.kakao

import kotlinx.serialization.Serializable

@Serializable
data class KakaoOption(
    /**
     * 비즈니스 채널 ID
     */
    var pfId: String? = null,

    /**
     * 알림톡 템플릿 ID
     */
    var templateId: String? = null,

    /**
     * 변수처리를 위한 배열 값
     * 예) "#{변수1}": "1234"
     */
    @JvmField
    var variables: Map<String, String>? = null,

    /**
     * 대체 발송 여부
     * 기본값: 대체발송 허용(false)
     */
    var disableSms: Boolean = false,

    /**
     * 이미지 알림톡 발송을 위한 ImageId
     */
    var imageId: String? = null,

    /**
     * 광고 발송 여부
     */
    var adFlag: Boolean = false,

    /**
     * 친구톡 버튼
     */
    var buttons: List<KakaoButton>? = null,

    /**
     * Kakao 브랜드 메시지 용 파라미터
     */
    var bms: KakaoBmsOption? = null
) {
    init {
        // 변수 키 값을 자동으로 #{변수명} 형태로 변환
        variables?.let {
            variables = formatVariables(it)
        }
    }

    private fun formatVariables(originalVars: Map<String, String>): MutableMap<String, String> {
        val groupedByFormattedKey = originalVars.entries.groupBy {
            val key = it.key
            if (key.startsWith("#{") && key.endsWith("}")) key else "#{$key}"
        }

        return groupedByFormattedKey.mapValues { (_, entries) ->
            (entries.find { it.key.startsWith("#{") && it.key.endsWith("}") }
                ?: entries.first()).value
        }.toMutableMap()
    }

    fun setVariables(variables: HashMap<String, String>) {
        this.variables = formatVariables(variables)
    }

    fun setVariables(variables: Map<String, String>) {
        this.variables = formatVariables(variables)
    }
}
