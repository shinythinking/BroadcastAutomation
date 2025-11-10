package com.shinythinking.broadcastautomation.data.local.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.shinythinking.broadcastautomation.data.local.BroadcastDatabase
import com.shinythinking.broadcastautomation.data.local.entity.TemplateEntity
import com.shinythinking.broadcastautomation.data.local.entity.TemplateFieldEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TemplateDaoTest {

    private lateinit var database: BroadcastDatabase
    private lateinit var templateDao: TemplateDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(
            context,
            BroadcastDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()

        templateDao = database.templateDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertTemplate_andGetById_returnsCorrectTemplate() = runTest {
        // Given: 템플릿 생성
        val template = createTestTemplate(id = "template1")

        // When: 템플릿 삽입 후 조회
        templateDao.insertTemplate(template)
        val retrieved = templateDao.getTemplate("template1")

        // Then: 정확히 일치하는지 확인
        assertNotNull(retrieved)
        assertEquals(template.id, retrieved?.id)
        assertEquals(template.name, retrieved?.name)
        assertEquals(template.icon, retrieved?.icon)
        assertEquals(template.template, retrieved?.template)
    }

    @Test
    fun insertTemplate_withConflict_replacesExisting() = runTest {
        // Given: 같은 ID의 템플릿 2개
        val template1 = createTestTemplate(id = "template1", name = "Original")
        val template2 = createTestTemplate(id = "template1", name = "Updated")

        // When: 순차적으로 삽입
        templateDao.insertTemplate(template1)
        templateDao.insertTemplate(template2)

        // Then: 두 번째 템플릿으로 교체됨
        val retrieved = templateDao.getTemplate("template1")
        assertEquals("Updated", retrieved?.name)
    }

    @Test
    fun updateTemplate_modifiesExistingTemplate() = runTest {
        // Given: 템플릿 삽입
        val template = createTestTemplate(id = "template1", name = "Original")
        templateDao.insertTemplate(template)

        // When: 템플릿 수정
        val updated = template.copy(name = "Modified", updatedAt = System.currentTimeMillis())
        templateDao.updateTemplate(updated)

        // Then: 변경사항 확인
        val retrieved = templateDao.getTemplate("template1")
        assertEquals("Modified", retrieved?.name)
    }

    @Test
    fun deleteTemplate_removesFromDatabase() = runTest {
        // Given: 템플릿 삽입
        val template = createTestTemplate(id = "template1")
        templateDao.insertTemplate(template)

        // When: 템플릿 삭제
        templateDao.deleteTemplate(template)

        // Then: 조회 시 null 반환
        val retrieved = templateDao.getTemplate("template1")
        assertNull(retrieved)
    }

    @Test
    fun deleteTemplateById_removesFromDatabase() = runTest {
        // Given: 템플릿 삽입
        val template = createTestTemplate(id = "template1")
        templateDao.insertTemplate(template)

        // When: ID로 삭제
        templateDao.deleteTemplateById("template1")

        // Then: 조회 시 null 반환
        val retrieved = templateDao.getTemplate("template1")
        assertNull(retrieved)
    }

    @Test
    fun insertTemplateWithFields_insertsAllData() = runTest {
        // Given: 템플릿과 필드 생성
        val template = createTestTemplate(id = "template1")
        val fields = listOf(
            createTestField(templateId = "template1", fieldId = "field1", order = 0),
            createTestField(templateId = "template1", fieldId = "field2", order = 1),
            createTestField(templateId = "template1", fieldId = "field3", order = 2)
        )

        // When: Transaction으로 삽입
        templateDao.insertTemplateWithFields(template, fields)

        // Then: 템플릿과 모든 필드가 저장됨
        val retrieved = templateDao.getTemplateWithFields("template1")
        assertNotNull(retrieved)
        assertEquals(template.id, retrieved?.template?.id)
        assertEquals(3, retrieved?.fields?.size)
    }

    @Test
    fun getTemplateWithFields_returnsCorrectRelation() = runTest {
        // Given: 템플릿과 필드 삽입
        val template = createTestTemplate(id = "template1", name = "Test Template")
        val fields = listOf(
            createTestField(
                templateId = "template1",
                fieldId = "name",
                fieldName = "이름",
                order = 0
            ),
            createTestField(
                templateId = "template1",
                fieldId = "phone",
                fieldName = "전화번호",
                order = 1
            )
        )
        templateDao.insertTemplateWithFields(template, fields)

        // When: 관계 조회
        val result = templateDao.getTemplateWithFields("template1")

        // Then: 템플릿과 필드가 올바르게 연결됨
        assertNotNull(result)
        assertEquals("Test Template", result?.template?.name)
        assertEquals(2, result?.fields?.size)
        assertEquals("이름", result?.fields?.get(0)?.fieldName)
        assertEquals("전화번호", result?.fields?.get(1)?.fieldName)
    }

    @Test
    fun getFieldsForTemplate_returnsOrderedFields() = runTest {
        // Given: 순서가 섞인 필드 삽입
        val template = createTestTemplate(id = "template1")
        val fields = listOf(
            createTestField(templateId = "template1", fieldId = "field3", order = 2),
            createTestField(templateId = "template1", fieldId = "field1", order = 0),
            createTestField(templateId = "template1", fieldId = "field2", order = 1)
        )
        templateDao.insertTemplateWithFields(template, fields)

        // When: 필드 조회
        val retrievedFields = templateDao.getFieldsForTemplate("template1")

        // Then: order 순서대로 정렬됨
        assertEquals(3, retrievedFields.size)
        assertEquals("field1", retrievedFields[0].fieldId)
        assertEquals("field2", retrievedFields[1].fieldId)
        assertEquals("field3", retrievedFields[2].fieldId)
    }

    @Test
    fun deleteTemplate_cascadesDeleteToFields() = runTest {
        // Given: 템플릿과 필드 삽입
        val template = createTestTemplate(id = "template1")
        val fields = listOf(
            createTestField(templateId = "template1", fieldId = "field1", order = 0),
            createTestField(templateId = "template1", fieldId = "field2", order = 1)
        )
        templateDao.insertTemplateWithFields(template, fields)

        // When: 템플릿 삭제
        templateDao.deleteTemplate(template)

        // Then: 연결된 필드도 자동 삭제됨 (CASCADE)
        val retrievedFields = templateDao.getFieldsForTemplate("template1")
        assertTrue(retrievedFields.isEmpty())
    }

    @Test
    fun getAllTemplatesWithFields_returnsFlowOfTemplates() = runTest {
        // Given: 여러 템플릿 삽입
        val template1 = createTestTemplate(id = "template1", name = "A Template", order = 1)
        val template2 = createTestTemplate(id = "template2", name = "B Template", order = 0)
        val fields1 =
            listOf(createTestField(templateId = "template1", fieldId = "field1", order = 0))
        val fields2 =
            listOf(createTestField(templateId = "template2", fieldId = "field2", order = 0))

        templateDao.insertTemplateWithFields(template1, fields1)
        templateDao.insertTemplateWithFields(template2, fields2)

        // When: Flow에서 첫 값 가져오기
        val templates = templateDao.getAllTemplatesWithFields().first()

        // Then: order 순서로 정렬되어 반환됨
        assertEquals(2, templates.size)
        assertEquals("template2", templates[0].template.id) // order = 0
        assertEquals("template1", templates[1].template.id) // order = 1
    }

    @Test
    fun getAllTemplatesWithFields_ordersCorrectly() = runTest {
        // Given: 같은 order를 가진 템플릿들
        val template1 = createTestTemplate(id = "template1", name = "Zebra", order = 0)
        val template2 = createTestTemplate(id = "template2", name = "Apple", order = 0)
        val template3 = createTestTemplate(id = "template3", name = "Banana", order = 1)

        templateDao.insertTemplate(template1)
        templateDao.insertTemplate(template2)
        templateDao.insertTemplate(template3)

        // When: 조회
        val templates = templateDao.getAllTemplatesWithFields().first()

        // Then: order -> name 순으로 정렬
        assertEquals(3, templates.size)
        assertEquals("Apple", templates[0].template.name)  // order=0, name=Apple
        assertEquals("Zebra", templates[1].template.name)  // order=0, name=Zebra
        assertEquals("Banana", templates[2].template.name) // order=1, name=Banana
    }

    @Test
    fun getTemplateWithFields_nonExistentId_returnsNull() = runTest {
        // When: 존재하지 않는 ID로 조회
        val result = templateDao.getTemplateWithFields("nonexistent")

        // Then: null 반환
        assertNull(result)
    }

    @Test
    fun insertFields_emptyList_doesNothing() = runTest {
        // Given: 템플릿만 삽입
        val template = createTestTemplate(id = "template1")
        templateDao.insertTemplate(template)

        // When: 빈 필드 리스트 삽입
        templateDao.insertFields(emptyList())

        // Then: 에러 없이 처리됨
        val result = templateDao.getTemplateWithFields("template1")
        assertNotNull(result)
        assertTrue(result?.fields?.isEmpty() == true)
    }

    @Test
    fun insertMultipleTemplates_withSameOrder_maintainsAll() = runTest {
        // Given: 같은 order를 가진 여러 템플릿
        val templates = (1..5).map { i ->
            createTestTemplate(id = "template$i", name = "Template $i", order = 0)
        }

        // When: 모두 삽입
        templates.forEach { templateDao.insertTemplate(it) }

        // Then: 모든 템플릿이 유지됨
        val result = templateDao.getAllTemplatesWithFields().first()
        assertEquals(5, result.size)
    }

    private fun createTestTemplate(
        id: String,
        name: String = "Test Template",
        icon: String = "📝",
        template: String = "안녕하세요 {name}님",
        order: Int = 0
    ) = TemplateEntity(
        id = id,
        name = name,
        icon = icon,
        template = template,
        order = order,
        createdAt = System.currentTimeMillis(),
        updatedAt = System.currentTimeMillis()
    )

    private fun createTestField(
        templateId: String,
        fieldId: String,
        fieldName: String = "테스트 필드",
        fieldType: String = "TEXT",
        placeholder: String = "입력하세요",
        isRequired: Boolean = true,
        order: Int
    ) = TemplateFieldEntity(
        templateId = templateId,
        fieldId = fieldId,
        fieldName = fieldName,
        fieldType = fieldType,
        placeholder = placeholder,
        isRequired = isRequired,
        order = order
    )
}