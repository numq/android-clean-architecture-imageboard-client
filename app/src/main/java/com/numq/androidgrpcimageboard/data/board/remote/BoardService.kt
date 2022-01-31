package com.numq.androidgrpcimageboard.data.board.remote

import io.grpc.ManagedChannel
import proto.BoardOuterClass
import proto.BoardServiceGrpcKt
import javax.inject.Inject


class BoardService @Inject constructor(channel: ManagedChannel) : BoardApi {

    private val stub by lazy {
        BoardServiceGrpcKt.BoardServiceCoroutineStub(channel).withWaitForReady()
    }

    override suspend fun getBoardById(id: String) = stub.getBoardById(
        BoardOuterClass.GetBoardByIdReq.newBuilder().setId(id).build()
    )

    override fun getBoards(skip: Long, limit: Long) = stub.getBoards(
        BoardOuterClass.GetBoardsReq.newBuilder().setSkip(skip).setLimit(limit).build()
    )

}