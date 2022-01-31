package com.numq.androidgrpcimageboard.data.post

import arrow.core.Either
import arrow.core.left
import arrow.core.rightIfNotNull
import com.numq.androidgrpcimageboard.data.post.local.PostDao
import com.numq.androidgrpcimageboard.data.post.remote.PostApi
import com.numq.androidgrpcimageboard.domain.entity.Post
import com.numq.androidgrpcimageboard.domain.repository.PostRepository
import com.numq.androidgrpcimageboard.platform.exception.AppExceptions
import com.numq.androidgrpcimageboard.platform.extension.mapping.entity
import com.numq.androidgrpcimageboard.platform.extension.mapping.proto
import com.numq.androidgrpcimageboard.platform.service.network.NetworkHandler
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PostData @Inject constructor(
    private val networkHandler: NetworkHandler,
    private val cache: PostDao,
    private val remote: PostApi
) : PostRepository {

    override suspend fun getPosts(threadId: String, skip: Long, limit: Long) =
        if (networkHandler.isNetworkAvailable) {
            remote.getPosts(threadId, skip, limit).map { it.post.entity }
                .rightIfNotNull { AppExceptions.DataException.RemoteException }
        } else {
            AppExceptions.ServiceException.NetworkException.left()
/*
            cache.getPosts(threadId, skip, limit).map { it.entity }.asFlow()
                .rightIfNotNull { AppExceptions.DataException.LocalException }
            TODO cache
*/
        }

    override suspend fun getPostById(id: String): Either<Exception, Post> =
        if (networkHandler.isNetworkAvailable) {
            remote.getPostById(id).post.entity.rightIfNotNull { AppExceptions.DataException.RemoteException }
        } else {
            AppExceptions.ServiceException.NetworkException.left()
/*
            cache.getPostById(id).entity.rightIfNotNull { AppExceptions.DataException.LocalException }
            TODO cache
*/
        }

    override suspend fun createPost(post: Post) =
        remote.createPost(post.proto).id.rightIfNotNull { AppExceptions.DataException.RemoteException }

}