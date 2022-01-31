package com.numq.androidgrpcimageboard.presentation.features.board

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.numq.androidgrpcimageboard.domain.entity.Thread
import com.numq.androidgrpcimageboard.platform.constant.AppConstants
import com.numq.androidgrpcimageboard.usecase.board.GetBoardById
import com.numq.androidgrpcimageboard.usecase.thread.CreateThread
import com.numq.androidgrpcimageboard.usecase.thread.GetThreads
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BoardViewModel @Inject constructor(
    private val createThread: CreateThread,
    private val getThreads: GetThreads,
    private val getBoardById: GetBoardById
) : ViewModel() {

    private var _uiState = MutableStateFlow<BoardState>(BoardState.Loading)
    val uiState = _uiState.asStateFlow()

    var boardId by mutableStateOf("")
    var skip by mutableStateOf(AppConstants.Paging.DEFAULT_SKIP)
    var limit by mutableStateOf(AppConstants.Paging.DEFAULT_LIMIT)

    private fun fetchThreads(boardId: String, skip: Long, limit: Long) =
        getThreads.invoke(Triple(boardId, skip, limit), onLeft = { e ->
            _uiState.update { BoardState.Failure(e) }
        }, onRight = { data ->
            _uiState.update {
                when (it) {
                    is BoardState.Success -> it.copy(
                        data = it.data.copy(
                            threads = data.toList()
                        )
                    )
                    else -> BoardState.Success(
                        BoardData(
                            threads = data.toList()
                        )
                    )
                }
            }
        })

    fun getThreads() = fetchThreads(boardId, skip, limit)

    fun pollThreads() = viewModelScope.launch {
        while (true) {
            fetchThreads(boardId, skip, limit)
            delay(2000L)
        }
    }

    fun getBoardById(id: String) = getBoardById.invoke(id, onLeft = { e ->
        _uiState.update { BoardState.Failure(e) }
    }, onRight = { data ->
        _uiState.update {
            when (it) {
                is BoardState.Success -> it.copy(
                    data = it.data.copy(
                        board = data
                    )
                )
                else -> BoardState.Success(BoardData(board = data))
            }
        }
    })

    fun createThread(thread: Thread) = createThread.invoke(thread)

}