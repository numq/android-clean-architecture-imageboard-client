package com.numq.androidgrpcimageboard.data.thread.remote

import kotlinx.coroutines.flow.Flow
import proto.ThreadOuterClass

interface ThreadApi {

    fun getThreads(boardId: String, skip: Long, limit: Long): Flow<ThreadOuterClass.GetThreadsRes>
    fun getHotThreads(): Flow<ThreadOuterClass.GetHotThreadsRes>
    fun getLatestThreads(): Flow<ThreadOuterClass.GetLatestThreadsRes>
    suspend fun createThread(thread: ThreadOuterClass.Thread): ThreadOuterClass.CreateThreadRes
    suspend fun getThreadById(id: String): ThreadOuterClass.GetThreadByIdRes

}