package com.numq.androidgrpcimageboard.usecase.thread

import com.numq.androidgrpcimageboard.domain.entity.Thread
import com.numq.androidgrpcimageboard.domain.repository.ThreadRepository
import com.numq.androidgrpcimageboard.platform.interactor.UseCase
import javax.inject.Inject

class CreateThread @Inject constructor(private val repository: ThreadRepository) :
    UseCase<Thread, String>() {

    override suspend fun execute(param: Thread) = repository.createThread(param)

}