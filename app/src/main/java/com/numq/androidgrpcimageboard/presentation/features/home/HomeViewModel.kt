package com.numq.androidgrpcimageboard.presentation.features.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.numq.androidgrpcimageboard.platform.constant.AppConstants
import com.numq.androidgrpcimageboard.usecase.board.GetBoards
import com.numq.androidgrpcimageboard.usecase.thread.GetHotThreads
import com.numq.androidgrpcimageboard.usecase.thread.GetLatestThreads
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getBoards: GetBoards,
    private val getHotThreads: GetHotThreads,
    private val getLatestThreads: GetLatestThreads
) : ViewModel() {

    private var _uiState = MutableStateFlow<HomeState>(HomeState.Loading)
    val uiState = _uiState.asStateFlow()

    var skip by mutableStateOf(AppConstants.Paging.DEFAULT_SKIP)
    var limit by mutableStateOf(AppConstants.Paging.DEFAULT_LIMIT)

    private fun fetchBoards(skip: Long, limit: Long) =
        getBoards.invoke(Pair(skip, limit), onLeft = { e ->
            _uiState.update { HomeState.Failure(e) }
        }, onRight = { data ->
            _uiState.update {
                when (it) {
                    is HomeState.Success -> it.copy(
                        data = it.data.copy(
                            boards = data.toList()
                        )
                    )
                    else -> HomeState.Success(HomeData(boards = data.toList()))
                }
            }
        })

    fun getBoards() = fetchBoards(skip, limit)

    fun pollBoards() = viewModelScope.launch {
        while (true) {
            fetchBoards(skip, limit)
            delay(2000L)
        }
    }

    fun getHotThreads() = getHotThreads.invoke(Unit, onLeft = { e ->
        _uiState.update { HomeState.Failure(e) }
    }, onRight = { data ->
        _uiState.update {
            when (it) {
                is HomeState.Success -> it.copy(
                    data = it.data.copy(
                        hotThreads = data.toList()
                    )
                )
                else -> HomeState.Success(HomeData(hotThreads = data.toList()))
            }
        }
    })

    fun getLatestThreads() = getLatestThreads.invoke(Unit, onLeft = { e ->
        _uiState.update { HomeState.Failure(e) }
    }, onRight = { data ->
        _uiState.update {
            when (it) {
                is HomeState.Success -> it.copy(
                    data = it.data.copy(
                        latestThreads = data.toList()
                    )
                )
                else -> HomeState.Success(HomeData(latestThreads = data.toList()))
            }
        }
    })

    fun pollThreads(isLatest: Boolean) = viewModelScope.launch {
        while (true) {
            if (isLatest) getLatestThreads() else getHotThreads()
            delay(2000L)
        }
    }

}