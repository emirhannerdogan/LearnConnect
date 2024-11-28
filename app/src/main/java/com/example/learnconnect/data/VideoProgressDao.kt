package com.example.learnconnect.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.learnconnect.data.model.VideoProgressEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VideoProgressDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateProgress(videoProgress: VideoProgressEntity)

    @Query("SELECT progress FROM video_progress WHERE courseId = :courseId AND videoIndex = :videoIndex")
    suspend fun getProgress(courseId: Int, videoIndex: Int): Long?

    @Query("SELECT * FROM video_progress WHERE courseId = :courseId")
    fun getAllProgressForCourse(courseId: Int): Flow<List<VideoProgressEntity>>

}
