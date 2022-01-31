package com.numq.androidgrpcimageboard.usecase.thread

import com.numq.androidgrpcimageboard.domain.entity.Thread
import com.numq.androidgrpcimageboard.domain.repository.ThreadRepository
import com.numq.androidgrpcimageboard.platform.interactor.UseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetHotThreads @Inject constructor(private val repository: ThreadRepository) :
    UseCase<Unit, Flow<Thread>>() {

    override suspend fun execute(param: Unit) = repository.getHotThreads()

}
