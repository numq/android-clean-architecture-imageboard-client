package com.numq.androidgrpcimageboard.usecase.board

import com.numq.androidgrpcimageboard.domain.entity.Board
import com.numq.androidgrpcimageboard.domain.repository.BoardRepository
import com.numq.androidgrpcimageboard.platform.interactor.UseCase
import javax.inject.Inject

class GetBoardById @Inject constructor(private val repository: BoardRepository) :
    UseCase<String, Board>() {

    override suspend fun execute(param: String) = repository.getBoardById(param)

}