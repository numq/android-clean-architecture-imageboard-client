package com.numq.androidgrpcimageboard.data.thread.remote

import io.grpc.ManagedChannel
import proto.ThreadOuterClass
import proto.ThreadServiceGrpcKt
import javax.inject.Inject


class ThreadService @Inject constructor(channel: ManagedChannel) : ThreadApi {

    private val api by lazy {
        ThreadServiceGrpcKt.ThreadServiceCoroutineStub(channel).withWaitForReady()
    }

    override suspend fun createThread(thread: ThreadOuterClass.Thread) = api.createThread(
        ThreadOuterClass.CreateThreadReq.newBuilder().setThread(thread).build()
    )

    override suspend fun getThreadById(id: String) = api.getThreadById(
        ThreadOuterClass.GetThreadByIdReq.newBuilder().setId(id).build()
    )

    override fun getThreads(boardId: String, skip: Long, limit: Long) = api.getThreads(
        ThreadOuterClass.GetThreadsReq.newBuilder().setBoardId(boardId).setSkip(skip)
            .setLimit(limit).build()
    )

    override fun getHotThreads() =
        api.getHotThreads(ThreadOuterClass.GetHotThreadsReq.getDefaultInstance())

    override fun getLatestThreads() =
        api.getLatestThreads(ThreadOuterClass.GetLatestThreadsReq.getDefaultInstance())
}