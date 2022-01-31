package com.numq.androidgrpcimageboard.usecase.thread

import com.numq.androidgrpcimageboard.domain.entity.Thread
import com.numq.androidgrpcimageboard.domain.repository.ThreadRepository
import com.numq.androidgrpcimageboard.platform.interactor.UseCase
import javax.inject.Inject

class GetThreadById @Inject constructor(private val repository: ThreadRepository) :
    UseCase<String, Thread>() {

    override suspend fun execute(param: String) = repository.getThreadById(param)

}