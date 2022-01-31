package com.numq.androidgrpcimageboard.presentation.features.home

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Image
import androidx.compose.material.icons.rounded.KeyboardReturn
import androidx.compose.material.icons.rounded.Reorder
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.content.edit
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.numq.androidgrpcimageboard.domain.entity.Board
import com.numq.androidgrpcimageboard.domain.entity.Thread
import com.numq.androidgrpcimageboard.platform.constant.AppConstants
import com.numq.androidgrpcimageboard.platform.extension.navigateSafe
import com.numq.androidgrpcimageboard.platform.extension.prefs
import com.numq.androidgrpcimageboard.presentation.common.expandable.ExpandableCard
import com.numq.androidgrpcimageboard.presentation.common.extension.context
import com.numq.androidgrpcimageboard.presentation.features.failure.ShowException
import com.numq.androidgrpcimageboard.presentation.features.loading.ShowLoading
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(scaffoldState: ScaffoldState, navController: NavController) {

    val vm = hiltViewModel<HomeViewModel>()

    LaunchedEffect(Unit) {
        vm.pollBoards()
    }

    when (val state = vm.uiState.collectAsState().value) {
        is HomeState.Failure -> ShowException(scaffoldState, state.exception)
        is HomeState.Success -> BuildHome(state.data, vm, navController)
        else -> ShowLoading()
    }

}

@ExperimentalFoundationApi
@Composable
fun BuildHome(
    data: HomeData,
    vm: HomeViewModel,
    navController: NavController
) {

    val prefs = context.prefs

    val listState = rememberLazyListState()

    var listOrder by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.Main) {
            listState.animateScrollToItem(0)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.Start
    ) {

        BuildThreads(
            data,
            Modifier
                .wrapContentSize(Alignment.TopCenter)
                .fillMaxWidth()
                .animateContentSize(), vm
        ) {
            prefs.edit {
                putString(AppConstants.Args.ACTIVE_THREAD_ID, it.id)
                navController.navigateSafe(AppConstants.Nav.THREAD)
            }
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IconButton(onClick = { listOrder = !listOrder }) {
                Icon(Icons.Rounded.Reorder, "")
            }
        }

        BuildBoards(data, vm,
            Modifier.weight(1f),
            listState,
            listOrder,
            onLoadMore = {
                vm.skip = vm.limit
                vm.limit += AppConstants.Paging.DEFAULT_LIMIT
                vm.pollBoards()
            }, onBoardClick = {
                prefs.edit {
                    putString(AppConstants.Args.ACTIVE_BOARD_ID, it.id)
                    navController.navigateSafe(AppConstants.Nav.BOARD)
                }
            })
    }
}

@Composable
fun BuildThreads(
    data: HomeData,
    modifier: Modifier = Modifier,
    vm: HomeViewModel,
    onThreadClick: (Thread) -> Unit
) {

    var showHot by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(showHot) {
        vm.pollThreads(!showHot)
    }

    Column(
        modifier = modifier.padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Row(horizontalArrangement = Arrangement.Center) {
            Text(text = "⌛", color = if (showHot) Color.Transparent else Color.Unspecified)
            Switch(checked = showHot, onCheckedChange = {
                showHot = it
            })
            Text(
                text = "\uD83D\uDD25",
                color = if (showHot) Color.Unspecified else Color.Transparent
            )
        }

        Divider()

        if ((!showHot && data.latestThreads.isNotEmpty()).xor(showHot && data.hotThreads.isNotEmpty())
        ) {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                if (showHot) {
                    items(data.hotThreads) { thread ->
                        TopThreadItem(thread, showHot, onThreadClick)
                    }
                } else {
                    items(data.latestThreads) { thread ->
                        TopThreadItem(thread, showHot, onThreadClick)
                    }
                }
            }
        } else {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }
    }
}

@Composable
fun TopThreadItem(thread: Thread, isHot: Boolean, onThreadClick: (Thread) -> Unit) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .clickable {
            onThreadClick(thread)
        }) {
        Row(
            modifier = Modifier.padding(top = 4.dp, bottom = 4.dp, start = 16.dp, end = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = thread.title, modifier = Modifier.fillMaxWidth(.5f))
            if (isHot) {
                Text(text = thread.postCount.toString())
            } else {
                Text(text = SimpleDateFormat.getInstance().format(Date(thread.bumpedAt)))
            }
        }
    }
}

@ExperimentalFoundationApi
@Composable
fun BuildBoards(
    data: HomeData,
    vm: HomeViewModel,
    modifier: Modifier,
    listState: LazyListState,
    listOrder: Boolean,
    onLoadMore: () -> Unit,
    onBoardClick: (Board) -> Unit
) {

    val scope = rememberCoroutineScope()

    val scrolled by remember {
        derivedStateOf { listState.firstVisibleItemIndex > 0 }
    }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.Main) {
            listState.animateScrollToItem(0)
        }
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {

        if (listOrder) {
            LazyColumn(modifier = Modifier.fillMaxWidth(), state = listState) {
                itemsIndexed(data.boards) { idx, board ->
                    BoardItem(Modifier.fillMaxWidth(), board, onBoardClick)
                    if (idx > vm.limit - 2) {
                        onLoadMore()
                    }
                }
            }
        } else {
            LazyVerticalGrid(
                modifier = Modifier.fillMaxWidth(),
                cells = GridCells.Fixed(3),
                contentPadding = PaddingValues(
                    start = 8.dp,
                    top = 12.dp,
                    end = 8.dp,
                    bottom = 12.dp
                ),
                state = listState
            ) {
                itemsIndexed(data.boards) { idx, board ->
                    BoardItem(Modifier, board, onBoardClick)
                    if (idx > vm.limit - 2) {
                        onLoadMore()
                    }
                }
            }
        }
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
    }
}

@Composable
fun BoardItem(modifier: Modifier, board: Board, onBoardClick: (Board) -> Unit = {}) {

    ExpandableCard(modifier, expandable = false, onItemClick = {
        onBoardClick(board)
    }, expandedContent = {
        Row {
            Text(text = board.description)
        }
    }) {
        Text(text = board.title)
        Icon(Icons.Rounded.Image, "")
    }
}
