package com.numq.androidgrpcimageboard.data.board.remote

import kotlinx.coroutines.flow.Flow
import proto.BoardOuterClass

interface BoardApi {

    fun getBoards(skip: Long, limit: Long): Flow<BoardOuterClass.GetBoardsRes>
    suspend fun getBoardById(id: String): BoardOuterClass.GetBoardByIdRes

}