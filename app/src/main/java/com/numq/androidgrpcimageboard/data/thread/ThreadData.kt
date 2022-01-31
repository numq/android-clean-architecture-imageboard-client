package com.numq.androidgrpcimageboard.data.thread

import arrow.core.left
import arrow.core.rightIfNotNull
import com.numq.androidgrpcimageboard.data.thread.local.ThreadDao
import com.numq.androidgrpcimageboard.data.thread.remote.ThreadApi
import com.numq.androidgrpcimageboard.domain.entity.Thread
import com.numq.androidgrpcimageboard.domain.repository.ThreadRepository
import com.numq.androidgrpcimageboard.platform.exception.AppExceptions
import com.numq.androidgrpcimageboard.platform.extension.mapping.entity
import com.numq.androidgrpcimageboard.platform.extension.mapping.proto
import com.numq.androidgrpcimageboard.platform.service.network.NetworkHandler
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ThreadData @Inject constructor(
    private val networkHandler: NetworkHandler,
    private val cache: ThreadDao,
    private val remote: ThreadApi
) : ThreadRepository {

    override suspend fun createThread(thread: Thread) =
        remote.createThread(thread.proto).id.rightIfNotNull { AppExceptions.DataException.RemoteException }

    override suspend fun getThreadById(id: String) =
        if (networkHandler.isNetworkAvailable) {
            remote.getThreadById(id).thread.entity.rightIfNotNull { AppExceptions.DataException.RemoteException }
        } else {
            AppExceptions.ServiceException.NetworkException.left()
/*
            cache.getThreadById(id).entity.rightIfNotNull { AppExceptions.DataException.LocalException }
            TODO cache
*/
        }

    override suspend fun getThreads(boardId: String, skip: Long, limit: Long) =
        if (networkHandler.isNetworkAvailable) {
            remote.getThreads(boardId, skip, limit).map { it.thread.entity }
                .rightIfNotNull { AppExceptions.DataException.RemoteException }
        } else {
            AppExceptions.ServiceException.NetworkException.left()
/*
            cache.getThreads(boardId, skip, limit).map { it.entity }.asFlow()
                .rightIfNotNull { AppExceptions.DataException.LocalException }
            TODO cache
*/
        }

    override fun getHotThreads() = remote.getHotThreads().map { it.thread.entity }
        .rightIfNotNull { AppExceptions.DataException.RemoteException }

    override fun getLatestThreads() = remote.getLatestThreads().map { it.thread.entity }
        .rightIfNotNull { AppExceptions.DataException.RemoteException }

}