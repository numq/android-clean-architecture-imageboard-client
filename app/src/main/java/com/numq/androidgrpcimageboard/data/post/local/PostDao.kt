package com.numq.androidgrpcimageboard.data.post.local

import androidx.room.*

@Dao
interface PostDao {

    @Query("SELECT * FROM posts WHERE threadId=(:threadId) LIMIT (:limit) OFFSET (:offset)")
    suspend fun getPosts(threadId: String, offset: Long, limit: Long): List<PostDataModel>

    @Query("SELECT * FROM posts WHERE id=(:id)")
    suspend fun getPostById(id: String): PostDataModel

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(post: PostDataModel): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllPosts(posts: List<PostDataModel>): Array<Long>

    @Update
    suspend fun updatePost(post: PostDataModel)

    @Delete
    suspend fun deletePost(post: PostDataModel): Int

}