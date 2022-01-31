package com.numq.androidgrpcimageboard.data.post.remote

import io.grpc.ManagedChannel
import proto.PostOuterClass
import proto.PostServiceGrpcKt
import javax.inject.Inject


class PostService @Inject constructor(channel: ManagedChannel) : PostApi {

    private val stub by lazy {
        PostServiceGrpcKt.PostServiceCoroutineStub(channel).withWaitForReady()
    }

    override fun getPosts(threadId: String, skip: Long, limit: Long) =
        stub.getPosts(
            PostOuterClass.GetPostsReq.newBuilder().setThreadId(threadId).setSkip(skip)
                .setLimit(limit).build()
        )

    override suspend fun getPostById(id: String) =
        stub.getPostById(PostOuterClass.GetPostByIdReq.newBuilder().setId(id).build())

    override suspend fun createPost(post: PostOuterClass.Post) = stub.createPost(
        PostOuterClass.CreatePostReq.newBuilder().setPost(post).build()
    )
}