package com.numq.androidgrpcimageboard.presentation.features.board

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.content.edit
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.numq.androidgrpcimageboard.domain.entity.Board
import com.numq.androidgrpcimageboard.domain.entity.Thread
import com.numq.androidgrpcimageboard.platform.constant.AppConstants
import com.numq.androidgrpcimageboard.platform.extension.navigateSafe
import com.numq.androidgrpcimageboard.platform.extension.prefs
import com.numq.androidgrpcimageboard.presentation.common.extension.context
import com.numq.androidgrpcimageboard.presentation.features.failure.ShowException
import com.numq.androidgrpcimageboard.presentation.features.loading.ShowLoading
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun BoardScreen(scaffoldState: ScaffoldState, navController: NavController) {

    val vm = hiltViewModel<BoardViewModel>()

    val prefs = context.prefs
    val boardId = prefs.getString(AppConstants.Args.ACTIVE_BOARD_ID, "")

    if (boardId.isNullOrBlank()) {
        navController.navigateUp()
    } else {

        vm.boardId = boardId

        LaunchedEffect(vm.uiState.value) {
            vm.getBoardById(boardId)
        }

        when (val state = vm.uiState.collectAsState().value) {
            is BoardState.Failure -> {
                ShowException(scaffoldState, state.exception)
                navController.navigateUp()
            }
            is BoardState.Success -> BuildBoard(
                state.data,
                vm,
                navController
            )
            else -> ShowLoading()
        }
    }
}

@Composable
fun BuildBoard(
    data: BoardData, vm: BoardViewModel, navController: NavController
) {

    val prefs = context.prefs

    if (data.board != null) {
        if (data.board.id.isBlank()) {
            navController.navigateUp()
        } else {

            val scope = rememberCoroutineScope()
            val listState = rememberLazyListState()

            val scrolled by remember {
                derivedStateOf { listState.firstVisibleItemIndex > 0 }
            }

            LaunchedEffect(Unit) {
                vm.pollThreads()
                withContext(Dispatchers.Main) {
                    listState.animateScrollToItem(0)
                }
            }

            Scaffold(topBar = {
                BoardTopBar(data.board) {
                    navController.navigateUp()
                }
            }) { values ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(values),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Bottom
                ) {
                    ShowThreads(
                        data,
                        vm,
                        listState,
                        modifier = Modifier.weight(1f),
                        onLoadMore = {
                            vm.skip = vm.limit
                            vm.limit += AppConstants.Paging.DEFAULT_LIMIT
                            vm.pollThreads()
                        }, {
                            prefs.edit {
                                putString(AppConstants.Args.ACTIVE_THREAD_ID, it.id)
                                navController.navigateSafe(AppConstants.Nav.THREAD)
                            }
                        })
                    if (scrolled) {
                        Card(modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp)
                            .clickable {
                                scope.launch(Dispatchers.Main) {
                                    listState.animateScrollToItem(0)
                                }
                            }) {
                            Icon(
                                Icons.Rounded.KeyboardReturn,
                                "",
                                modifier = Modifier.height(24.dp)
                            )
                        }
                    }
                    CreateThread(vm.boardId) { thread ->
                        vm.createThread(thread.copy(boardId = vm.boardId)).also {
                            it.invokeOnCompletion {
                                vm.getThreads()
                            }
                        }.isCompleted
                    }
                }
            }
        }
    } else {
        navController.navigateUp()
    }
}

@Composable
fun BoardTopBar(board: Board, onBack: () -> Unit) {

    TopAppBar(title = {
        Text(text = board.title)
    }, navigationIcon = {
        IconButton(onClick = { onBack() }) {
            Icon(Icons.Rounded.ArrowBack, "")
        }
    })
}

@Composable
fun ShowThreads(
    data: BoardData,
    vm: BoardViewModel,
    listState: LazyListState,
    modifier: Modifier = Modifier,
    onLoadMore: () -> Unit,
    onThreadClick: (Thread) -> Unit
) {

    Column(modifier = modifier) {
        if (data.threads.isNullOrEmpty()) {
            NoThreads()
        } else {
            LazyColumn(modifier = Modifier.fillMaxWidth(), state = listState) {
                itemsIndexed(data.threads) { idx, thread ->
                    ThreadItem(thread, onThreadClick)
                    if (idx > vm.limit - 2) {
                        onLoadMore()
                    }
                }
            }
        }
    }
}

@Composable
fun NoThreads() {

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Card(modifier = Modifier.padding(4.dp)) {
            Column(
                modifier = Modifier.padding(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "No threads in this board,\nbut you able to create your own.",
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun ThreadItem(thread: Thread, onThreadClick: (Thread) -> Unit) {

    Card(modifier = Modifier
        .padding(4.dp)
        .fillMaxWidth()
        .clickable { onThreadClick(thread) }) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(text = thread.title)
            Text(text = SimpleDateFormat.getInstance().format(Date(thread.bumpedAt)).toString())
        }
    }
}

@Composable
fun CreateThread(
    boardId: String,
    onCreateThread: (Thread) -> Boolean
) {

    var createDialogState by remember {
        mutableStateOf(false)
    }

    if (createDialogState) {
        CreateThreadDialog(boardId, { thread ->
            onCreateThread(thread)
            createDialogState = false
        }, {
            createDialogState = it
        })
    }

    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.BottomCenter) {
        Card(modifier = Modifier
            .padding(4.dp)
            .clickable { createDialogState = true }) {
            Row {
                Icon(imageVector = Icons.Rounded.Add, contentDescription = "")
                Text("NEW THREAD")
            }
        }
    }
}

@Composable
fun CreateThreadDialog(
    boardId: String,
    onCreateThread: (Thread) -> Unit,
    onDismiss: (Boolean) -> Unit
) {

    var threadTitle by remember {
        mutableStateOf("")
    }

    fun clear() {
        threadTitle = ""
    }

    Dialog(onDismissRequest = {
        onDismiss(false)
        clear()
    }) {
        Card(modifier = Modifier.padding(4.dp)) {
            Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.End) {
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = threadTitle,
                    onValueChange = {
                        threadTitle = it
                    },
                    placeholder = {
                        Text(text = "Title")
                    },
                    singleLine = true,
                    trailingIcon = {
                        IconButton(onClick = { threadTitle = "" }) {
                            Icon(Icons.Rounded.Clear, "")
                        }
                    }
                )
                IconButton(
                    enabled = threadTitle.isNotBlank(),
                    onClick = {
                        onCreateThread(Thread(boardId = boardId, title = threadTitle))
                    }) {
                    Icon(Icons.Rounded.Done, "")
                }
            }
        }
    }
}
