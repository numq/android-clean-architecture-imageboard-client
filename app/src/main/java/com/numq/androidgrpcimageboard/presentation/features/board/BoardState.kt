package com.numq.androidgrpcimageboard.presentation.features.board

import com.numq.androidgrpcimageboard.domain.entity.Board
import com.numq.androidgrpcimageboard.domain.entity.Thread

data class BoardData(
    val board: Board? = null,
    val threads: List<Thread> = emptyList()
)

sealed class BoardState {
    object Loading : BoardState()
    data class Success(val data: BoardData) : BoardState()
    data class Failure(val exception: Exception) : BoardState()
}
