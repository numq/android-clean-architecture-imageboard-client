package com.numq.androidgrpcimageboard.presentation.features.home

import com.numq.androidgrpcimageboard.domain.entity.Board
import com.numq.androidgrpcimageboard.domain.entity.Thread

data class HomeData(
    var boards: List<Board> = emptyList(),
    val hotThreads: List<Thread> = emptyList(),
    val latestThreads: List<Thread> = emptyList()
)

sealed class HomeState {
    object Loading : HomeState()
    data class Success(val data: HomeData) : HomeState()
    data class Failure(val exception: Exception) : HomeState()
}
