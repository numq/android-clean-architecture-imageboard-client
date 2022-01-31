package com.numq.androidgrpcimageboard.data.post.remote

import kotlinx.coroutines.flow.Flow
import proto.PostOuterClass

interface PostApi {

    fun getPosts(threadId: String, skip: Long, limit: Long): Flow<PostOuterClass.GetPostsRes>
    suspend fun getPostById(id: String): PostOuterClass.GetPostByIdRes
    suspend fun createPost(post: PostOuterClass.Post): PostOuterClass.CreatePostRes

}