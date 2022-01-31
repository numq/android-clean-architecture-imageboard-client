package com.numq.androidgrpcimageboard.domain.repository

import arrow.core.Either
import com.numq.androidgrpcimageboard.domain.entity.Board
import kotlinx.coroutines.flow.Flow

interface BoardRepository {

    suspend fun getBoards(skip: Long, limit: Long): Either<Exception, Flow<Board>>

    suspend fun getBoardById(id: String): Either<Exception, Board>

}