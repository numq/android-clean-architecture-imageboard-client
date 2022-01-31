package com.numq.androidgrpcimageboard.usecase.post

import com.numq.androidgrpcimageboard.domain.entity.Post
import com.numq.androidgrpcimageboard.domain.repository.PostRepository
import com.numq.androidgrpcimageboard.platform.interactor.UseCase
import javax.inject.Inject

class CreatePost @Inject constructor(private val repository: PostRepository) :
    UseCase<Post, String>() {

    override suspend fun execute(param: Post) = repository.createPost(param)

}
