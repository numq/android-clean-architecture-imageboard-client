package com.numq.androidgrpcimageboard.presentation.features.thread

import com.numq.androidgrpcimageboard.domain.entity.Post
import com.numq.androidgrpcimageboard.domain.entity.Thread

data class ThreadData(
    val thread: Thread? = null,
    val posts: List<Post> = emptyList()
)

sealed class ThreadState {
    object Loading : ThreadState()
    data class Success(val data: ThreadData) : ThreadState()
    data class Failure(val exception: Exception) : ThreadState()
}