package com.numq.androidgrpcimageboard.data.board

import arrow.core.Either
import arrow.core.left
import arrow.core.rightIfNotNull
import com.numq.androidgrpcimageboard.data.board.local.BoardDao
import com.numq.androidgrpcimageboard.data.board.remote.BoardApi
import com.numq.androidgrpcimageboard.domain.entity.Board
import com.numq.androidgrpcimageboard.domain.repository.BoardRepository
import com.numq.androidgrpcimageboard.platform.exception.AppExceptions
import com.numq.androidgrpcimageboard.platform.extension.mapping.entity
import com.numq.androidgrpcimageboard.platform.service.network.NetworkHandler
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BoardData @Inject constructor(
    private val networkHandler: NetworkHandler,
    private val cache: BoardDao,
    private val remote: BoardApi
) : BoardRepository {

    override suspend fun getBoardById(id: String): Either<java.lang.Exception, Board> {
        return if (networkHandler.isNetworkAvailable) remote.getBoardById(id).board.entity.rightIfNotNull { AppExceptions.DataException.RemoteException }
        else cache.getBoardById(id).entity.rightIfNotNull { AppExceptions.DataException.LocalException }
    }

    override suspend fun getBoards(skip: Long, limit: Long) =
        if (networkHandler.isNetworkAvailable) {
            remote.getBoards(skip, limit).map { it.board.entity }
                .rightIfNotNull { AppExceptions.DataException.RemoteException }
        } else {
            AppExceptions.ServiceException.NetworkException.left()
/*
            cache.getBoards(skip, limit).map { it.entity }.asFlow()
                .rightIfNotNull { AppExceptions.DataException.LocalException }
            TODO cache
*/
        }

}