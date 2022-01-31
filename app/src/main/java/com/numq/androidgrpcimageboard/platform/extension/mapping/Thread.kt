package com.numq.androidgrpcimageboard.platform.extension.mapping

import com.numq.androidgrpcimageboard.data.thread.local.ThreadDataModel
import com.numq.androidgrpcimageboard.domain.entity.Thread
import proto.ThreadOuterClass

val Thread.proto: ThreadOuterClass.Thread
    get() = ThreadOuterClass.Thread.newBuilder()
        .setId(id)
        .setBoardId(boardId)
        .setPostCount(postCount)
        .setTitle(title)
        .setCreatedAt(createdAt)
        .setBumpedAt(bumpedAt)
        .build()

val ThreadOuterClass.Thread.entity: Thread
    get() = Thread(id, boardId, postCount, title, createdAt, bumpedAt)

val Thread.model: ThreadDataModel
    get() = ThreadDataModel(id, boardId, postCount, title, createdAt, bumpedAt)

val ThreadDataModel.entity: Thread
    get() = Thread(id, boardId, postCount, title, createdAt, bumpedAt)