package com.numq.androidgrpcimageboard.data.thread.local

import androidx.room.*

@Dao
interface ThreadDao {

    @Query("SELECT * FROM threads WHERE boardId=(:boardId) LIMIT (:limit) OFFSET (:offset)")
    suspend fun getThreads(boardId: String, offset: Long, limit: Long): List<ThreadDataModel>

    @Query("SELECT * FROM threads WHERE id=(:id)")
    suspend fun getThreadById(id: String): ThreadDataModel

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertThread(thread: ThreadDataModel): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllThreads(threads: List<ThreadDataModel>): Array<Long>

    @Update
    suspend fun updateThread(thread: ThreadDataModel)

    @Delete
    suspend fun deleteThread(thread: ThreadDataModel): Int

}