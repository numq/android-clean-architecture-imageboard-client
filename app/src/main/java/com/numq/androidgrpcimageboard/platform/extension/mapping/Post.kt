package com.numq.androidgrpcimageboard.platform.extension.mapping

import com.numq.androidgrpcimageboard.data.post.local.PostDataModel
import com.numq.androidgrpcimageboard.domain.entity.Post
import proto.PostOuterClass

val Post.proto: PostOuterClass.Post
    get() = PostOuterClass.Post.newBuilder()
        .setId(id)
        .setThreadId(threadId)
        .setDescription(description)
        .addAllQuoteIds(quoteIds)
        .setText(text)
        .setImageUrl(imageUrl)
        .setVideoUrl(videoUrl)
        .setCreatedAt(createdAt)
        .build()

val PostOuterClass.Post.entity: Post
    get() = Post(id, threadId, description, quoteIdsList, text, imageUrl, videoUrl, createdAt)

val Post.model: PostDataModel
    get() = PostDataModel(
        id,
        threadId,
        description,
        quoteIds,
        text,
        imageUrl,
        videoUrl,
        createdAt
    )

val PostDataModel.entity: Post
    get() = Post(
        id,
        threadId,
        description,
        quoteIds,
        text,
        imageUrl,
        videoUrl,
        createdAt
    )