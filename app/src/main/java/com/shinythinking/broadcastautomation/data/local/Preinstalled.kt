package com.shinythinking.broadcastautomation.data.local

import com.shinythinking.broadcastautomation.data.local.entity.TemplateEntity
import com.shinythinking.broadcastautomation.data.local.entity.TemplateFieldEntity

object Preinstalled {
    fun getTemplates(): List<Pair<TemplateEntity, List<TemplateFieldEntity>>> {
        return listOf(
            createVillageMeetingTemplate(),
            createWeddingTemplate(),
            createEmergencyTemplate(),
            createEventTemplate(),
            createDeathNoticeTemplate(),
            createConstructionNoticeTemplate()
        )
    }

    private fun createVillageMeetingTemplate(): Pair<TemplateEntity, List<TemplateFieldEntity>> {
        val template = TemplateEntity(
            id = "template_village_meeting",
            name = "마을 회의 공지",
            icon = "📋",
            template = "주민 여러분께 알립니다. [날짜] [시간]에 [장소]에서 마을 회의가 있을 예정입니다. [안건]에 대해 논의할 예정이오니 많은 참석 부탁드립니다.",
            order = 1
        )

        val fields = listOf(
            TemplateFieldEntity(
                templateId = template.id,
                fieldId = "날짜",
                fieldName = "회의 날짜",
                fieldType = "DATE",
                placeholder = "2025-10-13",
                isRequired = true,
                order = 1
            ),
            TemplateFieldEntity(
                templateId = template.id,
                fieldId = "시간",
                fieldName = "회의 시간",
                fieldType = "TIME",
                placeholder = "14:00",
                isRequired = true,
                order = 2
            ),
            TemplateFieldEntity(
                templateId = template.id,
                fieldId = "장소",
                fieldName = "장소",
                fieldType = "LOCATION",
                placeholder = "마을회관",
                isRequired = true,
                order = 3
            ),
            TemplateFieldEntity(
                templateId = template.id,
                fieldId = "안건",
                fieldName = "회의 안건",
                fieldType = "TEXT",
                placeholder = "예: 마을 도로 정비에 관한 건",
                isRequired = false,
                order = 4
            )
        )

        return Pair(template, fields)
    }

    private fun createWeddingTemplate(): Pair<TemplateEntity, List<TemplateFieldEntity>> {
        val template = TemplateEntity(
            id = "template_wedding",
            name = "결혼식 안내",
            icon = "💒",
            template = "경사스러운 소식을 전해드립니다. [신랑_성함]씨와 [신부_성함]씨의 결혼식이 [날짜] [시간]에 [장소]에서 열립니다. 많은 축하 부탁드립니다.",
            order = 2
        )

        val fields = listOf(
            TemplateFieldEntity(
                templateId = template.id,
                fieldId = "신랑_성함",
                fieldName = "신랑 성함",
                fieldType = "TEXT",
                placeholder = "홍길동",
                isRequired = true,
                order = 1
            ),
            TemplateFieldEntity(
                templateId = template.id,
                fieldId = "신부_성함",
                fieldName = "신부 성함",
                fieldType = "TEXT",
                placeholder = "김영희",
                isRequired = true,
                order = 2
            ),
            TemplateFieldEntity(
                templateId = template.id,
                fieldId = "날짜",
                fieldName = "결혼식 날짜",
                fieldType = "DATE",
                isRequired = true,
                order = 3
            ),
            TemplateFieldEntity(
                templateId = template.id,
                fieldId = "시간",
                fieldName = "결혼식 시간",
                fieldType = "TIME",
                isRequired = true,
                order = 4
            ),
            TemplateFieldEntity(
                templateId = template.id,
                fieldId = "장소",
                fieldName = "장소",
                fieldType = "LOCATION",
                placeholder = "마을회관 대강당",
                isRequired = true,
                order = 5
            )
        )

        return Pair(template, fields)
    }

    private fun createEmergencyTemplate(): Pair<TemplateEntity, List<TemplateFieldEntity>> {
        val template = TemplateEntity(
            id = "template_emergency",
            name = "긴급 재난 안내",
            icon = "⚠️",
            template = "긴급 안내 방송입니다. [재난종류]로 인해 [지역] 일대에 [조치사항]이 시행됩니다. 주민 여러분께서는 안전에 유의해 주시기 바랍니다.",
            order = 3
        )

        val fields = listOf(
            TemplateFieldEntity(
                templateId = template.id,
                fieldId = "재난종류",
                fieldName = "재난 종류",
                fieldType = "TEXT",
                placeholder = "폭설, 태풍, 폭우 등",
                isRequired = true,
                order = 1
            ),
            TemplateFieldEntity(
                templateId = template.id,
                fieldId = "지역",
                fieldName = "해당 지역",
                fieldType = "LOCATION",
                placeholder = "마을 전체",
                isRequired = true,
                order = 2
            ),
            TemplateFieldEntity(
                templateId = template.id,
                fieldId = "조치사항",
                fieldName = "조치 사항",
                fieldType = "TEXT",
                placeholder = "차량 통행 제한",
                isRequired = true,
                order = 3
            )
        )

        return Pair(template, fields)
    }

    private fun createEventTemplate(): Pair<TemplateEntity, List<TemplateFieldEntity>> {
        val template = TemplateEntity(
            id = "template_event",
            name = "행사 안내",
            icon = "🎉",
            template = "주민 여러분께 안내 말씀드립니다. [날짜] [시간]에 [장소]에서 [행사명]이 개최됩니다. [추가내용] 많은 참여 부탁드립니다.",
            order = 4
        )

        val fields = listOf(
            TemplateFieldEntity(
                templateId = template.id,
                fieldId = "행사명",
                fieldName = "행사명",
                fieldType = "TEXT",
                placeholder = "추석맞이 윷놀이 대회",
                isRequired = true,
                order = 1
            ),
            TemplateFieldEntity(
                templateId = template.id,
                fieldId = "날짜",
                fieldName = "날짜",
                fieldType = "DATE",
                isRequired = true,
                order = 2
            ),
            TemplateFieldEntity(
                templateId = template.id,
                fieldId = "시간",
                fieldName = "시간",
                fieldType = "TIME",
                isRequired = true,
                order = 3
            ),
            TemplateFieldEntity(
                templateId = template.id,
                fieldId = "장소",
                fieldName = "장소",
                fieldType = "LOCATION",
                isRequired = true,
                order = 4
            ),
            TemplateFieldEntity(
                templateId = template.id,
                fieldId = "추가내용",
                fieldName = "추가 내용",
                fieldType = "TEXT",
                placeholder = "참가비는 무료입니다.",
                isRequired = false,
                order = 5
            )
        )

        return Pair(template, fields)
    }

    private fun createDeathNoticeTemplate(): Pair<TemplateEntity, List<TemplateFieldEntity>> {
        val template = TemplateEntity(
            id = "template_death_notice",
            name = "부고 안내",
            icon = "🕯️",
            template = "삼가 고인의 명복을 빕니다. [고인_성함]님께서 [날짜]에 별세하셨습니다. 빈소는 [장소]에 마련되어 있으며, 발인은 [발인일시]입니다.",
            order = 5
        )

        val fields = listOf(
            TemplateFieldEntity(
                templateId = template.id,
                fieldId = "고인_성함",
                fieldName = "고인 성함",
                fieldType = "TEXT",
                isRequired = true,
                order = 1
            ),
            TemplateFieldEntity(
                templateId = template.id,
                fieldId = "날짜",
                fieldName = "별세 날짜",
                fieldType = "DATE",
                isRequired = true,
                order = 2
            ),
            TemplateFieldEntity(
                templateId = template.id,
                fieldId = "장소",
                fieldName = "빈소 위치",
                fieldType = "LOCATION",
                isRequired = true,
                order = 3
            ),
            TemplateFieldEntity(
                templateId = template.id,
                fieldId = "발인일시",
                fieldName = "발인 일시",
                fieldType = "TEXT",
                placeholder = "10월 15일 오전 9시",
                isRequired = true,
                order = 4
            )
        )

        return Pair(template, fields)
    }

    private fun createConstructionNoticeTemplate(): Pair<TemplateEntity, List<TemplateFieldEntity>> {
        val template = TemplateEntity(
            id = "template_construction",
            name = "공사 안내",
            icon = "🚧",
            template = "주민 여러분께 안내드립니다. [시작일]부터 [종료일]까지 [위치]에서 [공사내용] 공사가 진행됩니다. 공사 기간 중 [주의사항] 협조 부탁드립니다.",
            order = 6
        )

        val fields = listOf(
            TemplateFieldEntity(
                templateId = template.id,
                fieldId = "시작일",
                fieldName = "공사 시작일",
                fieldType = "DATE",
                isRequired = true,
                order = 1
            ),
            TemplateFieldEntity(
                templateId = template.id,
                fieldId = "종료일",
                fieldName = "공사 종료일",
                fieldType = "DATE",
                isRequired = true,
                order = 2
            ),
            TemplateFieldEntity(
                templateId = template.id,
                fieldId = "위치",
                fieldName = "공사 위치",
                fieldType = "LOCATION",
                isRequired = true,
                order = 3
            ),
            TemplateFieldEntity(
                templateId = template.id,
                fieldId = "공사내용",
                fieldName = "공사 내용",
                fieldType = "TEXT",
                placeholder = "도로 포장",
                isRequired = true,
                order = 4
            ),
            TemplateFieldEntity(
                templateId = template.id,
                fieldId = "주의사항",
                fieldName = "주의 사항",
                fieldType = "TEXT",
                placeholder = "우회 도로를 이용해 주시기 바랍니다.",
                isRequired = false,
                order = 5
            )
        )

        return Pair(template, fields)
    }
}