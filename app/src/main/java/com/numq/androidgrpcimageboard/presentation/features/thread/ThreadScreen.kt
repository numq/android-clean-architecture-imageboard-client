package com.numq.androidgrpcimageboard.presentation.features.thread

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.numq.androidgrpcimageboard.domain.entity.Post
import com.numq.androidgrpcimageboard.domain.entity.Thread
import com.numq.androidgrpcimageboard.platform.constant.AppConstants
import com.numq.androidgrpcimageboard.platform.extension.copyText
import com.numq.androidgrpcimageboard.platform.extension.prefs
import com.numq.androidgrpcimageboard.presentation.common.chip.ChipGroup
import com.numq.androidgrpcimageboard.presentation.common.extension.context
import com.numq.androidgrpcimageboard.presentation.features.failure.ShowException
import com.numq.androidgrpcimageboard.presentation.features.loading.ShowLoading
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ThreadScreen(scaffoldState: ScaffoldState, navController: NavController) {

    val vm = hiltViewModel<ThreadViewModel>()

    val prefs = context.prefs
    val threadId = prefs.getString(AppConstants.Args.ACTIVE_THREAD_ID, "")

    if (threadId.isNullOrBlank()) {
        navController.navigateUp()
    } else {

        vm.threadId = threadId

        LaunchedEffect(vm.uiState.value) {
            vm.getThreadById(threadId)
        }

        when (val state = vm.uiState.collectAsState().value) {
            is ThreadState.Failure -> {
                ShowException(scaffoldState, state.exception)
                navController.navigateUp()
            }
            is ThreadState.Success -> BuildThread(
                scaffoldState,
                state.data,
                vm,
                navController
            )
            else -> ShowLoading()
        }
    }
}


@Composable
fun ThreadTopBar(thread: Thread, onBack: () -> Unit) {
    TopAppBar(title = {
        Text(text = thread.title)
    }, navigationIcon = {
        IconButton(onClick = { onBack() }) {
            Icon(Icons.Rounded.ArrowBack, "")
        }
    })
}

@Composable
fun BuildThread(
    scaffoldState: ScaffoldState,
    data: ThreadData,
    vm: ThreadViewModel,
    navController: NavController
) {

    if (data.thread != null) {
        if (data.thread.id.isBlank()) {
            navController.navigateUp()
        } else {

            val scope = rememberCoroutineScope()
            val listState = rememberLazyListState()

            LaunchedEffect(Unit) {
                vm.pollPosts()
                withContext(Dispatchers.Main) {
                    listState.animateScrollToItem(data.posts.lastIndex)
                }
            }

            var quoteIds by remember {
                mutableStateOf(listOf<String>())
            }

            val scrolled by remember {
                derivedStateOf { listState.firstVisibleItemIndex > 0 }
            }

            Scaffold(topBar = {
                ThreadTopBar(data.thread) {
                    navController.navigateUp()
                }
            }) { values ->

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(values),
                    verticalArrangement = Arrangement.Bottom
                ) {

                    ShowPosts(
                        data,
                        vm,
                        scaffoldState,
                        listState,
                        Modifier.weight(1f),
                        onQuoteIdClick = {
                            scope.launch(Dispatchers.Main) {
                                listState.animateScrollToItem(it)
                            }
                        },
                        onLoadMore = {
                            vm.skip = vm.limit
                            vm.limit += AppConstants.Paging.DEFAULT_LIMIT
                            vm.pollPosts()
                        }) { post ->
                        quoteIds = if (quoteIds.contains(post.id)) {
                            quoteIds.filter { it != post.id }
                        } else {
                            quoteIds.plus(post.id)
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

                    CreatePost(vm.threadId, quoteIds, updateQuiteIds = { id ->
                        quoteIds = if (quoteIds.contains(id)) {
                            quoteIds.filter { it != id }
                        } else {
                            quoteIds.plus(id)
                        }
                    }) {
                        vm.createPost(it).apply {
                            invokeOnCompletion {
                                vm.getPosts()
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PostItem(
    scaffoldState: ScaffoldState,
    post: Post,
    onQuoteIdClick: (Int) -> Unit,
    onPostClick: (String) -> Unit
) {

    val context = context
    val scope = rememberCoroutineScope()

    var markedAsQuote by remember {
        mutableStateOf(false)
    }
    val postColor by remember {
        mutableStateOf(if (markedAsQuote) LightGray else Color.Unspecified)
    }
    var imageUri by remember {
        mutableStateOf("")
    }
    var imageDialogState by remember {
        mutableStateOf(false)
    }

    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.Start) {
        Card(modifier = Modifier
            .background(postColor)
            .clickable {
                onPostClick(post.id)
                markedAsQuote = !markedAsQuote
            }) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = post.id)
                    Divider()
                    if (post.quoteIds.isNotEmpty()) {
                        LazyRow {
                            itemsIndexed(post.quoteIds) { idx, id ->
                                if (id.isNotBlank()) {
                                    Card(
                                        modifier = Modifier
                                            .padding(4.dp)
                                            .clickable { onQuoteIdClick(idx) }) {
                                        Text(text = "${id.take(5)}..")
                                    }
                                }
                            }
                        }
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(text = post.text)
                    }
                }
            }
        }

        Box {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.Top
            ) {
                if (post.imageUrl.isNotBlank()) {
                    Card(
                        modifier = Modifier
                            .padding(4.dp)
                            .combinedClickable(
                                onClick = {
                                    imageUri = post.imageUrl
                                    imageDialogState = true
                                },
                                onLongClick = {
                                    context.copyText(post.imageUrl)
                                    scope.launch {
                                        scaffoldState.snackbarHostState.showSnackbar("Copied.")
                                    }
                                })
                    ) {
                        Icon(Icons.Rounded.Image, "")
                    }
                }

                if (post.videoUrl.isNotBlank()) {

                    val templateUri =
                        AppConstants.UI.IMAGE_TEMPLATE_URI.format(post.videoUrl.split("v=")[1])

                    Card(
                        modifier = Modifier
                            .padding(4.dp)
                            .combinedClickable(
                                onClick = {
                                    imageUri = templateUri
                                    imageDialogState = true
                                },
                                onLongClick = {
                                    context.copyText(templateUri)
                                    scope.launch {
                                        scaffoldState.snackbarHostState.showSnackbar("Copied.")
                                    }
                                })
                    ) {
                        Icon(Icons.Rounded.VideoLabel, "")
                    }
                }
            }
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Bottom
            ) {
                Text(
                    text = SimpleDateFormat.getInstance().format(Date(post.createdAt))
                        .toString()
                )
            }
        }
    }

    if (imageDialogState && imageUri.isNotBlank()) {
        ShowImageDialog(imageUri, {
            context.copyText(imageUri)
            scope.launch {
                scaffoldState.snackbarHostState.showSnackbar("Copied.")
            }
        }, {
            imageDialogState = false
            imageUri = ""
        })
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ShowImageDialog(uri: String, onCopyUri: () -> Unit, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = {
        onDismiss()
    }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
                .combinedClickable(
                    onClick = {
                        onDismiss()
                    },
                    onLongClick = {
                        onCopyUri()
                        onDismiss()
                    }),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = rememberImagePainter(uri),
                contentDescription = "",
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
fun ShowVideoDialog() {
    /*TODO*/
}

@Composable
fun ThreadInput(
    onCreate: (String) -> Unit
) {

    var inputText by remember {
        mutableStateOf("")
    }

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        TextField(
            value = inputText, onValueChange = {
                inputText = it
            }, modifier = Modifier
                .wrapContentHeight()
                .weight(1f), maxLines = 5
        )
        IconButton(onClick = {
            onCreate(inputText)
        }, enabled = inputText.isNotBlank()) {
            Icon(Icons.Rounded.Add, "")
        }
    }
}

@Composable
fun ShowPosts(
    data: ThreadData,
    vm: ThreadViewModel,
    scaffoldState: ScaffoldState,
    listState: LazyListState,
    modifier: Modifier = Modifier,
    onQuoteIdClick: (Int) -> Unit,
    onLoadMore: () -> Unit,
    onPostClick: (Post) -> Unit
) {



    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(
            4.dp,
            alignment = Alignment.Bottom
        ),
        state = listState,
        reverseLayout = true
    ) {
        itemsIndexed(data.posts) { idx, post ->
            PostItem(scaffoldState, post, onQuoteIdClick) {
                onPostClick(post)
            }
            if (idx > vm.limit - 2) {
                onLoadMore()
            }
        }
    }
}

@Composable
fun CreatePost(
    threadId: String,
    quoteIds: List<String>,
    updateQuiteIds: (String) -> Unit,
    onCreatePost: (Post) -> Unit
) {

    var createDialogState by remember {
        mutableStateOf(false)
    }

    var postText by remember {
        mutableStateOf("")
    }

    if (createDialogState) {
        CreatePostDialog(threadId, quoteIds, updateQuiteIds = {
            updateQuiteIds(it)
        }, postText, onCreatePost = {
            onCreatePost(it)
            createDialogState = false
        }, onDismiss = {
            createDialogState = it
        })
    } else {
        if (quoteIds.isNotEmpty()) {
            ChipGroup(quoteIds) {
                updateQuiteIds(it)
            }
        }
        ThreadInput {
            postText = it
            createDialogState = true
        }
    }

}

@Composable
fun CreatePostDialog(
    threadId: String,
    quoteIds: List<String>,
    updateQuiteIds: (String) -> Unit,
    text: String,
    onCreatePost: (Post) -> Unit,
    onDismiss: (Boolean) -> Unit
) {

    var postDescription by remember {
        mutableStateOf("")
    }
    var postText by remember {
        mutableStateOf(text)
    }
    var imageUrl by remember {
        mutableStateOf("")
    }
    var videoUrl by remember {
        mutableStateOf("")
    }

    fun clear() {
        postDescription = ""
        postText = ""
        imageUrl = ""
        videoUrl = ""
    }

    Dialog(onDismissRequest = {
        onDismiss(false)
        clear()
    }) {
        Card(modifier = Modifier.padding(4.dp)) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.End
            ) {
                ChipGroup(quoteIds) {
                    updateQuiteIds(it)
                }
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = postDescription,
                    onValueChange = {
                        postDescription = it
                    },
                    placeholder = {
                        Text(text = "Anon")
                    },
                    singleLine = true,
                    trailingIcon = {
                        IconButton(onClick = { postDescription = "" }) {
                            Icon(Icons.Rounded.Clear, "")
                        }
                    }
                )
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = postText,
                    onValueChange = {
                        postText = it
                    },
                    placeholder = {
                        Text(text = "Text")
                    }, maxLines = 10,
                    trailingIcon = {
                        IconButton(onClick = { postText = "" }) {
                            Icon(Icons.Rounded.Clear, "")
                        }
                    }
                )
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = imageUrl,
                    onValueChange = {
                        imageUrl = it
                    },
                    placeholder = {
                        Text(text = "Image url")
                    },
                    singleLine = true,
                    trailingIcon = {
                        IconButton(onClick = { imageUrl = "" }) {
                            Icon(Icons.Rounded.Clear, "")
                        }
                    }
                )
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = videoUrl,
                    onValueChange = {
                        videoUrl = it
                    },
                    placeholder = {
                        Text(text = "Video url")
                    },
                    singleLine = true,
                    trailingIcon = {
                        IconButton(onClick = { videoUrl = "" }) {
                            Icon(Icons.Rounded.Clear, "")
                        }
                    }
                )
                IconButton(
                    modifier = Modifier.wrapContentSize(),
                    enabled = text.isNotBlank(),
                    onClick = {
                        onCreatePost(
                            Post(
                                threadId = threadId,
                                description = postDescription,
                                text = text,
                                quoteIds = quoteIds,
                                imageUrl = imageUrl,
                                videoUrl = videoUrl
                            )
                        )
                    }) {
                    Icon(Icons.Rounded.Done, "")
                }
            }
        }
    }
}