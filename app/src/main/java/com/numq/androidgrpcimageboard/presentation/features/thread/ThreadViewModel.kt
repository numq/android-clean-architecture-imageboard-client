package com.numq.androidgrpcimageboard.presentation.features.thread

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.numq.androidgrpcimageboard.domain.entity.Post
import com.numq.androidgrpcimageboard.platform.constant.AppConstants
import com.numq.androidgrpcimageboard.usecase.post.CreatePost
import com.numq.androidgrpcimageboard.usecase.post.GetPosts
import com.numq.androidgrpcimageboard.usecase.thread.GetThreadById
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ThreadViewModel @Inject constructor(
    private val createPost: CreatePost,
    private val getPosts: GetPosts,
    private val getThreadById: GetThreadById
) : ViewModel() {

    private var _uiState = MutableStateFlow<ThreadState>(ThreadState.Loading)
    val uiState = _uiState.asStateFlow()

    var threadId by mutableStateOf("")
    var skip by mutableStateOf(AppConstants.Paging.DEFAULT_SKIP)
    var limit by mutableStateOf(AppConstants.Paging.DEFAULT_LIMIT)

    private fun fetchPosts(threadId: String, skip: Long, limit: Long) =
        getPosts.invoke(Triple(threadId, skip, limit), onLeft = { e ->
            _uiState.update { ThreadState.Failure(e) }
        }, onRight = { data ->
            _uiState.update {
                when (it) {
                    is ThreadState.Success -> it.copy(
                        data = it.data.copy(
                            posts = data.toList()
                        )
                    )
                    else -> ThreadState.Success(ThreadData(posts = data.toList()))
                }
            }
        })

    fun getPosts() = fetchPosts(threadId, skip, limit)

    fun pollPosts() = viewModelScope.launch {
        while (true) {
            fetchPosts(threadId, skip, limit)
            delay(2000L)
        }
    }

    fun getThreadById(id: String) = getThreadById.invoke(id, onLeft = { e ->
        _uiState.update { ThreadState.Failure(e) }
    }, onRight = { data ->
        _uiState.update {
            when (it) {
                is ThreadState.Success -> it.copy(
                    data = it.data.copy(
                        thread = data
                    )
                )
                else -> ThreadState.Success(ThreadData(thread = data))
            }
        }
    })

    fun createPost(post: Post) = createPost.invoke(post)

}