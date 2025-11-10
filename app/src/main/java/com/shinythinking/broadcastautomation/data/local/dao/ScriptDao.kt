package com.shinythinking.broadcastautomation.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.shinythinking.broadcastautomation.data.local.entity.ScriptEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ScriptDao {
    @Query("SELECT * FROM scripts ORDER BY createdAt DESC")
    fun getScripts(): Flow<List<ScriptEntity>>

    @Query("SELECT * FROM scripts WHERE id = :scriptId")
    suspend fun getScript(scriptId: String): ScriptEntity?

    @Query(
        """
        SELECT * FROM scripts 
        WHERE title LIKE '%' || :query || '%' 
        OR content LIKE '%' || :query || '%'
        ORDER BY createdAt DESC
    """
    )
    fun searchScripts(query: String): Flow<List<ScriptEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScript(script: ScriptEntity)

    @Delete
    suspend fun deleteScript(script: ScriptEntity)

    @Update
    suspend fun updateScript(script: ScriptEntity)

    @Query("UPDATE scripts SET isFavorite = :isFavorite WHERE id = :scriptId")
    suspend fun updateFavoriteStatus(scriptId: String, isFavorite: Boolean)
}