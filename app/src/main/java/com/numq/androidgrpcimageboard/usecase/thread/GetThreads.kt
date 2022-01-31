package com.numq.androidgrpcimageboard.usecase.thread

import arrow.core.Either
import com.numq.androidgrpcimageboard.domain.entity.Thread
import com.numq.androidgrpcimageboard.domain.repository.ThreadRepository
import com.numq.androidgrpcimageboard.platform.interactor.UseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetThreads @Inject constructor(private val repository: ThreadRepository) :
    UseCase<Triple<String, Long, Long>, Flow<Thread>>() {

    override suspend fun execute(param: Triple<String, Long, Long>): Either<Exception, Flow<Thread>> {
        val (boardId, skip, limit) = param
        return repository.getThreads(boardId, skip, limit)
    }
}