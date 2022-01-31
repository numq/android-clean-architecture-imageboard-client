package com.numq.androidgrpcimageboard.usecase.board

import arrow.core.Either
import com.numq.androidgrpcimageboard.domain.entity.Board
import com.numq.androidgrpcimageboard.domain.repository.BoardRepository
import com.numq.androidgrpcimageboard.platform.interactor.UseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetBoards @Inject constructor(private val repository: BoardRepository) :
    UseCase<Pair<Long, Long>, Flow<Board>>() {

    override suspend fun execute(param: Pair<Long, Long>): Either<Exception, Flow<Board>> {
        val (skip, limit) = param
        return repository.getBoards(skip, limit)
    }

}