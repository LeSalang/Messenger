package com.lesa.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.lesa.app.data.local.entities.StreamEntity

@Dao
interface StreamDao {
    @Transaction
    suspend fun updateStreams(streams: List<StreamEntity>) {
        deleteAllStreams()
        insertAllStreams(streams)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllStreams(streams: List<StreamEntity>)

    @Query("DELETE FROM streams")
    suspend fun deleteAllStreams()

    @Query("SELECT * FROM streams")
    suspend fun getAll(): List<StreamEntity>
}