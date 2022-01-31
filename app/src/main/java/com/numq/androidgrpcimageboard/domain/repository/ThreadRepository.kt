package com.numq.androidgrpcimageboard.domain.repository

import arrow.core.Either
import com.numq.androidgrpcimageboard.domain.entity.Thread
import kotlinx.coroutines.flow.Flow

interface ThreadRepository {

    suspend fun createThread(thread: Thread): Either<Exception, String>

    suspend fun getThreadById(id: String): Either<Exception, Thread>

    suspend fun getThreads(
        boardId: String,
        skip: Long,
        limit: Long
    ): Either<Exception, Flow<Thread>>

    fun getHotThreads(): Either<Exception, Flow<Thread>>

    fun getLatestThreads(): Either<Exception, Flow<Thread>>

}