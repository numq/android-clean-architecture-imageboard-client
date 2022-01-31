package com.numq.androidgrpcimageboard.platform.interactor

import arrow.core.Either
import kotlinx.coroutines.*

abstract class UseCase<in T, out R> {

    protected abstract suspend fun execute(param: T): Either<Exception, R>

    operator fun invoke(
        param: T,
        scope: CoroutineScope = GlobalScope,
        onLeft: suspend (Exception) -> Unit = {},
        onRight: suspend (R) -> Unit = {}
    ): Job {
        return scope.launch(Dispatchers.IO) {
            when (val data = execute(param)) {
                is Either.Left -> onLeft(data.value)
                is Either.Right -> onRight(data.value)
            }
        }
    }

}