package com.numq.androidgrpcimageboard.usecase.post

import arrow.core.Either
import com.numq.androidgrpcimageboard.domain.entity.Post
import com.numq.androidgrpcimageboard.domain.repository.PostRepository
import com.numq.androidgrpcimageboard.platform.interactor.UseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GetPosts @Inject constructor(private val repository: PostRepository) :
    UseCase<Triple<String, Long, Long>, Flow<Post>>() {

    override suspend fun execute(param: Triple<String, Long, Long>): Either<Exception, Flow<Post>> {
        val (threadId, skip, limit) = param
        return repository.getPosts(threadId, skip, limit)
    }

}