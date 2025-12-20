package com.shinythinking.broadcastautomation.domain.usecase

import android.util.Log
import com.shinythinking.broadcastautomation.domain.model.AIGenerationOptions
import com.shinythinking.broadcastautomation.domain.model.Script
import com.shinythinking.broadcastautomation.domain.repository.LLMRepository
import com.shinythinking.broadcastautomation.domain.repository.LocalDataRepository
import java.time.LocalDateTime
import java.util.UUID
import javax.inject.Inject

class GenerateScriptFromAIUseCase @Inject constructor(
    private val lLMRepository: LLMRepository,
    private val localDataRepository: LocalDataRepository
) {
    suspend operator fun invoke(
        keywords: String,
        options: AIGenerationOptions
    ): Result<Script> {
        if (keywords.isBlank()) {
            return Result.failure(IllegalArgumentException("키워드를 입력해주세요"))
        }

        val prompt = """
            # Role
            당신은 '이전리' 마을의 친근하고 매너 있는 '이장님'입니다. 마을 주민들에게 안내 방송을 하는 상황입니다.

            # Task
            사용자가 제공하는 [키워드]들을 조합하여 자연스러운 마을 안내 방송 대본을 작성해주세요.
            
            # Tone & Manner
            - 말투: 정중하면서도 친근한 말투 (예: "~하시기 바랍니다.", "~있겠습니다.")
            - 문체: 방송용 마이크를 잡고 말하는 듯한 명확한 문장
            - 분위기: 따뜻함, 배려심, 명확한 정보 전달
            
            # Format (필수 준수)
            대본은 반드시 아래의 순서와 형식을 지켜야 합니다.

            1. **시작 인사**: "아~ 아~, 안녕하세요. 이전리 마을 방송실입니다." (반드시 이 문구로 시작)
            2. **본문 1**: 전달받은 키워드(날짜, 시간, 장소, 내용 등)를 바탕으로 주민들에게 알리는 상세 내용을 자연스럽게 서술.
            3. **중간 멘트**: "다시 알려드립니다." (본문 1이 끝난 후 반드시 삽입)
            4. **본문 2**: 본문 1의 내용을 한 번 더 반복 
            5. **맺음말**: 계절이나 날씨, 안부와 관련된 가벼운 덕담으로 마무리.
            
            # Extra Instruction
            ${options.toPrompt()}
            
            # Keywords
            
            $keywords
        """.trimIndent()

        return lLMRepository.generateScript(prompt)
            .mapCatching { generatedContent ->
                val script = Script(
                    id = UUID.randomUUID().toString(),
                    title = "AI 생성 방송",
                    content = generatedContent,
                    createdAt = LocalDateTime.now(),
                    templateId = "AI"
                )

                localDataRepository.saveScript(script)

                script
            }
            .onFailure { e ->
                Log.e("GenerateScriptFromAIUseCase", "Error generating script", e)
            }
    }
}