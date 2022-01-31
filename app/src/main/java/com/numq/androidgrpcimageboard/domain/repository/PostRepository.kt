package com.numq.androidgrpcimageboard.domain.repository

import arrow.core.Either
import com.numq.androidgrpcimageboard.domain.entity.Post
import kotlinx.coroutines.flow.Flow

interface PostRepository {

    suspend fun getPosts(threadId: String, skip: Long, limit: Long): Either<Exception, Flow<Post>>

    suspend fun getPostById(id: String): Either<Exception, Post>

    suspend fun createPost(post: Post): Either<Exception, String>

}