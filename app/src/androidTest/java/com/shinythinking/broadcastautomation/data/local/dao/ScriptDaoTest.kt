package com.shinythinking.broadcastautomation.data.local.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.shinythinking.broadcastautomation.data.local.BroadcastDatabase
import com.shinythinking.broadcastautomation.data.local.entity.ScriptEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ScriptDaoTest {
    private lateinit var database: BroadcastDatabase
    private lateinit var scriptDao: ScriptDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(
            context,
            BroadcastDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()

        scriptDao = database.scriptDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertScript_andGetById_returnsCorrectScript() = runTest {
        // Given: 스크립트 생성
        val script = createTestScript(id = "script1")

        // When: 삽입 후 조회
        scriptDao.insertScript(script)
        val retrieved = scriptDao.getScript("script1")

        // Then: 정확히 일치
        Assert.assertNotNull(retrieved)
        Assert.assertEquals(script.id, retrieved?.id)
        Assert.assertEquals(script.title, retrieved?.title)
        Assert.assertEquals(script.content, retrieved?.content)
        Assert.assertEquals(script.templateId, retrieved?.templateId)
    }

    @Test
    fun insertScript_withConflict_replacesExisting() = runTest {
        // Given: 같은 ID의 스크립트 2개
        val script1 = createTestScript(id = "script1", title = "Original")
        val script2 = createTestScript(id = "script1", title = "Updated")

        // When: 순차 삽입
        scriptDao.insertScript(script1)
        scriptDao.insertScript(script2)

        // Then: 교체됨
        val retrieved = scriptDao.getScript("script1")
        Assert.assertEquals("Updated", retrieved?.title)
    }

    @Test
    fun updateScript_modifiesExistingScript() = runTest {
        // Given: 스크립트 삽입
        val script = createTestScript(id = "script1", content = "Original")
        scriptDao.insertScript(script)

        // When: 내용 수정
        val updated = script.copy(content = "Modified", updatedAt = System.currentTimeMillis())
        scriptDao.updateScript(updated)

        // Then: 변경 확인
        val retrieved = scriptDao.getScript("script1")
        Assert.assertEquals("Modified", retrieved?.content)
    }

    @Test
    fun deleteScript_removesFromDatabase() = runTest {
        // Given: 스크립트 삽입
        val script = createTestScript(id = "script1")
        scriptDao.insertScript(script)

        // When: 삭제
        scriptDao.deleteScript(script)

        // Then: null 반환
        val retrieved = scriptDao.getScript("script1")
        Assert.assertNull(retrieved)
    }

    @Test
    fun updateFavoriteStatus_changesIsFavoriteFlag() = runTest {
        // Given: 즐겨찾기 아닌 스크립트
        val script = createTestScript(id = "script1", isFavorite = false)
        scriptDao.insertScript(script)

        // When: 즐겨찾기로 변경
        scriptDao.updateFavoriteStatus("script1", true)

        // Then: 상태 변경 확인
        val retrieved = scriptDao.getScript("script1")
        Assert.assertTrue(retrieved?.isFavorite == true)
    }

    @Test
    fun updateFavoriteStatus_togglesCorrectly() = runTest {
        // Given: 즐겨찾기 스크립트
        val script = createTestScript(id = "script1", isFavorite = true)
        scriptDao.insertScript(script)

        // When: 즐겨찾기 해제
        scriptDao.updateFavoriteStatus("script1", false)

        // Then: 해제 확인
        val retrieved = scriptDao.getScript("script1")
        Assert.assertFalse(retrieved?.isFavorite == true)
    }

    @Test
    fun getScripts_returnsOrderedByCreatedAtDesc() = runTest {
        // Given: 시간차를 두고 스크립트 삽입
        val script1 = createTestScript(id = "script1", createdAt = 1000L)
        val script2 = createTestScript(id = "script2", createdAt = 3000L)
        val script3 = createTestScript(id = "script3", createdAt = 2000L)

        scriptDao.insertScript(script1)
        scriptDao.insertScript(script2)
        scriptDao.insertScript(script3)

        // When: 조회
        val scripts = scriptDao.getScripts().first()

        // Then: 최신순 정렬 (DESC)
        Assert.assertEquals(3, scripts.size)
        Assert.assertEquals("script2", scripts[0].id) // 3000L
        Assert.assertEquals("script3", scripts[1].id) // 2000L
        Assert.assertEquals("script1", scripts[2].id) // 1000L
    }

    @Test
    fun getScripts_emptyDatabase_returnsEmptyList() = runTest {
        // When: 빈 데이터베이스에서 조회
        val scripts = scriptDao.getScripts().first()

        // Then: 빈 리스트 반환
        Assert.assertTrue(scripts.isEmpty())
    }

    @Test
    fun searchScripts_byTitle_returnsMatchingScripts() = runTest {
        // Given: 다양한 제목의 스크립트
        val script1 = createTestScript(id = "script1", title = "긴급 공지사항")
        val script2 = createTestScript(id = "script2", title = "일반 안내")
        val script3 = createTestScript(id = "script3", title = "긴급 점검")

        scriptDao.insertScript(script1)
        scriptDao.insertScript(script2)
        scriptDao.insertScript(script3)

        // When: '긴급' 검색
        val results = scriptDao.searchScripts("긴급").first()

        // Then: 해당 스크립트만 반환
        Assert.assertEquals(2, results.size)
        Assert.assertTrue(results.any { it.id == "script1" })
        Assert.assertTrue(results.any { it.id == "script3" })
    }

    @Test
    fun searchScripts_byContent_returnsMatchingScripts() = runTest {
        // Given: 다양한 내용의 스크립트
        val script1 = createTestScript(id = "script1", content = "안녕하세요 주민 여러분")
        val script2 = createTestScript(id = "script2", content = "오늘 날씨가 좋습니다")
        val script3 = createTestScript(id = "script3", content = "주민 회의 안내")

        scriptDao.insertScript(script1)
        scriptDao.insertScript(script2)
        scriptDao.insertScript(script3)

        // When: '주민' 검색
        val results = scriptDao.searchScripts("주민").first()

        // Then: 해당 스크립트만 반환
        Assert.assertEquals(2, results.size)
        Assert.assertTrue(results.any { it.id == "script1" })
        Assert.assertTrue(results.any { it.id == "script3" })
    }

    @Test
    fun searchScripts_caseInsensitive_findsMatches() = runTest {
        // Given: 영문 스크립트
        val script = createTestScript(
            id = "script1",
            title = "EMERGENCY Notice",
            content = "Important information"
        )
        scriptDao.insertScript(script)

        // When: 소문자로 검색
        val results = scriptDao.searchScripts("emergency").first()

        // Then: 대소문자 구분 없이 검색됨
        Assert.assertEquals(1, results.size)
        Assert.assertEquals("script1", results[0].id)
    }

    @Test
    fun searchScripts_partialMatch_findsResults() = runTest {
        // Given: 스크립트 삽입
        val script = createTestScript(id = "script1", title = "공지사항")
        scriptDao.insertScript(script)

        // When: 부분 문자열로 검색
        val results = scriptDao.searchScripts("공지").first()

        // Then: 부분 일치로 찾아짐
        Assert.assertEquals(1, results.size)
        Assert.assertEquals("script1", results[0].id)
    }

    @Test
    fun searchScripts_noMatch_returnsEmptyList() = runTest {
        // Given: 스크립트 삽입
        val script = createTestScript(id = "script1", title = "공지사항")
        scriptDao.insertScript(script)

        // When: 일치하지 않는 검색어
        val results = scriptDao.searchScripts("존재하지않는단어").first()

        // Then: 빈 리스트
        Assert.assertTrue(results.isEmpty())
    }

    @Test
    fun searchScripts_emptyQuery_returnsAllScripts() = runTest {
        // Given: 여러 스크립트
        scriptDao.insertScript(createTestScript(id = "script1", createdAt = 1000L))
        scriptDao.insertScript(createTestScript(id = "script2", createdAt = 2000L))

        // When: 빈 문자열 검색
        val results = scriptDao.searchScripts("").first()

        // Then: 모든 스크립트 반환 (최신순)
        Assert.assertEquals(2, results.size)
        Assert.assertEquals("script2", results[0].id)
    }

    @Test
    fun getScripts_flowUpdates_whenDataChanges() = runTest {
        // Given: 초기 데이터
        val script1 = createTestScript(id = "script1", createdAt = 1000L)
        scriptDao.insertScript(script1)

        // When: 첫 값 확인
        val initial = scriptDao.getScripts().first()
        Assert.assertEquals(1, initial.size)

        // When: 새 데이터 추가
        val script2 = createTestScript(id = "script2", createdAt = 2000L)
        scriptDao.insertScript(script2)

        // Then: Flow가 업데이트됨
        val updated = scriptDao.getScripts().first()
        Assert.assertEquals(2, updated.size)
    }

    @Test
    fun insertMultipleScripts_performsCorrectly() = runTest {
        // Given: 100개의 스크립트
        val scripts = (1..100).map { i ->
            createTestScript(
                id = "script$i",
                title = "Script $i",
                createdAt = i.toLong() * 1000
            )
        }

        // When: 모두 삽입
        scripts.forEach { scriptDao.insertScript(it) }

        // Then: 모두 저장됨
        val retrieved = scriptDao.getScripts().first()
        Assert.assertEquals(100, retrieved.size)

        // 최신순 정렬 확인 (가장 큰 createdAt이 첫 번째)
        Assert.assertEquals("script100", retrieved[0].id)
        Assert.assertEquals("script99", retrieved[1].id)
    }

    @Test
    fun getScript_nonExistentId_returnsNull() = runTest {
        // When: 존재하지 않는 ID
        val result = scriptDao.getScript("nonexistent")

        // Then: null
        Assert.assertNull(result)
    }

    @Test
    fun updateFavoriteStatus_nonExistentId_doesNothing() = runTest {
        // When: 존재하지 않는 ID 업데이트 시도
        scriptDao.updateFavoriteStatus("nonexistent", true)

        // Then: 에러 없이 처리 (아무 변화 없음)
        val result = scriptDao.getScript("nonexistent")
        Assert.assertNull(result)
    }

    @Test
    fun searchScripts_specialCharacters_handlesCorrectly() = runTest {
        // Given: 특수문자 포함 스크립트
        val script = createTestScript(
            id = "script1",
            title = "공지: 100% 할인!",
            content = "특별한 이벤트 (50% + 50%)"
        )
        scriptDao.insertScript(script)

        // When: 특수문자로 검색
        val results1 = scriptDao.searchScripts("100%").first()
        val results2 = scriptDao.searchScripts("(50%").first()

        // Then: 정상 검색됨
        Assert.assertEquals(1, results1.size)
        Assert.assertEquals(1, results2.size)
    }

    private fun createTestScript(
        id: String,
        title: String = "Test Script",
        content: String = "안녕하세요 나는야 홍사인",
        templateId: String = "template1",
        createdAt: Long = System.currentTimeMillis(),
        isFavorite: Boolean = false
    ) = ScriptEntity(
        id = id,
        title = title,
        content = content,
        templateId = templateId,
        createdAt = createdAt,
        updatedAt = createdAt,
        isFavorite = isFavorite
    )
}